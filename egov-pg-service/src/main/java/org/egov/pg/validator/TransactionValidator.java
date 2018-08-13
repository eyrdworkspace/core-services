package org.egov.pg.validator;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.User;
import org.egov.pg.constants.PgConstants;
import org.egov.pg.models.Bill;
import org.egov.pg.models.BillDetail;
import org.egov.pg.models.Transaction;
import org.egov.pg.repository.BillingRepository;
import org.egov.pg.repository.TransactionRepository;
import org.egov.pg.service.GatewayService;
import org.egov.pg.web.models.TransactionCriteria;
import org.egov.pg.web.models.TransactionRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
@Service
public class TransactionValidator {

    private GatewayService gatewayService;
    private TransactionRepository transactionRepository;
    private BillingRepository billingRepository;


    @Autowired
    public TransactionValidator(GatewayService gatewayService, TransactionRepository transactionRepository,
                                BillingRepository billingRepository) {
        this.gatewayService = gatewayService;
        this.transactionRepository = transactionRepository;
        this.billingRepository = billingRepository;
    }

    /**
     * Validate the transaction,
     * Check if gateway is available and active
     * Check if module specific order id is unique
     *
     * @param transaction txn object to be validated
     */
    public void validateCreateTxn(TransactionRequest transaction) {
        Map<String, String> errorMap = new HashMap<>();
        isUserDetailPresent(transaction, errorMap);
        isGatewayActive(transaction.getTransaction(), errorMap);
        validateBillAndAddModuleId(transaction, errorMap);

        if (!errorMap.isEmpty())
            throw new CustomException(errorMap);

    }

    /**
     * Validate update of transaction
     * Check if transaction id exists in query params provided
     * Check if transaction id exists in system
     *
     * @param requestParams
     * @return
     */
    public Transaction validateUpdateTxn(Map<String, String> requestParams) {

        Optional<String> optional = gatewayService.getTxnId(requestParams);

        if (!optional.isPresent())
            throw new CustomException("MISSING_TXN_ID", "Cannot process request, missing transaction id");

        TransactionCriteria criteria = TransactionCriteria.builder()
                .txnId(optional.get())
                .build();

        List<Transaction> statuses = transactionRepository.fetchTransactions(criteria);

        //TODO Add to error queue
        if (statuses.isEmpty()) {
            throw new CustomException("TXN_NOT_FOUND", "Transaction not found");
        }

        return statuses.get(0);
    }

    public boolean shouldGenerateReceipt(Transaction prevStatus, Transaction newStatus) {
        if(prevStatus.getTxnStatus().equals(Transaction.TxnStatusEnum.SUCCESS))
            return false;

        if (newStatus.getTxnStatus().equals(Transaction.TxnStatusEnum.SUCCESS)) {
            if (new BigDecimal(prevStatus.getTxnAmount()).compareTo(new BigDecimal(newStatus.getTxnAmount())) == 0) {
                newStatus.setTxnStatus(Transaction.TxnStatusEnum.SUCCESS);
                newStatus.setTxnStatusMsg(PgConstants.TXN_SUCCESS);
                return true;
            } else {
                log.error("Transaction Amount mismatch, expected {} got {}", prevStatus.getTxnAmount(), newStatus
                        .getTxnAmount());
                newStatus.setTxnStatus(Transaction.TxnStatusEnum.FAILURE);
                newStatus.setTxnStatusMsg(PgConstants.TXN_FAILURE_AMT_MISMATCH);
                return false;
            }
        } else {
            newStatus.setTxnStatus(Transaction.TxnStatusEnum.FAILURE);
            newStatus.setTxnStatusMsg(PgConstants.TXN_FAILURE_GATEWAY);
            return false;
        }
    }


    private void isUserDetailPresent(TransactionRequest transactionRequest, Map<String, String> errorMap) {
        User user = transactionRequest.getRequestInfo().getUserInfo();
        if (isNull(user) || isNull(user.getUuid()) || isEmpty(user.getName()) || isNull(user.getUserName()) ||
                isNull
                (user.getTenantId()))
            errorMap.put("INVALID_USER_DETAILS", "User UUID, Name, Username and Tenant Id are mandatory");
    }

    private void isGatewayActive(Transaction transaction, Map<String, String> errorMap) {
        if (!gatewayService.isGatewayActive(transaction.getGateway()))
            errorMap.put("INVALID_PAYMENT_GATEWAY", "Invalid or inactive payment gateway provided");
    }

    /**
     * Verify if valid bill exists for provided bill id
     * Verify for existing transactions in the repository for this bill
     * Verify that bill being paid is accurate in all cases
     *
     * @param transactionRequest Request for which validation should happen
     * @param errorMap           Map of errors occurred during validations
     */

    private void validateBillAndAddModuleId(TransactionRequest transactionRequest, Map<String, String> errorMap) {
        Transaction txn = transactionRequest.getTransaction();
        TransactionCriteria criteria = TransactionCriteria.builder()
                .billId(txn.getBillId())
                .module(txn.getModule())
                .build();

        List<Bill> bills = billingRepository.fetchBill(transactionRequest.getRequestInfo(), txn.getTenantId(), txn
                .getBillId());

        if (bills.isEmpty()) {
            log.error("Bill ID provided does not exist " + txn.getBillId());
            errorMap.put("INVALID_BILL_ID", "Bill ID does not exist in billing system");
        } else {
            if (bills.get(0).getBillDetails().isEmpty()) {
                log.error("Bill ID provided does not contain any bill details" + txn.getBillId());
                errorMap.put("INVALID_BILL_ID", "No bill details exist for provided bill");
                return;
            }

            BillDetail billDetail = bills.get(0).getBillDetails().get(0);
            List<Transaction> existingTxnsForBill = transactionRepository.fetchTransactions(criteria);

            if (existingTxnsForBill.isEmpty())
                validateIfTxnForBillAbsent(errorMap, billDetail, txn);
            else
                validateIfTxnExistsForBill(errorMap, billDetail, txn, existingTxnsForBill);
        }
    }

    /**
     * Validations if transaction(s) already exists for this bill
     * If part payment is allowed,
     * Sum of all existing txns in pending / success status and amount being
     * paid should not be greater than total bill amount.
     * <p>
     * If not allowed,
     * No transaction should exists in success / pending state for this bill     *
     *
     * @param errorMap     Map of errors occurred during validations
     * @param billDetail   Bill detail for which payment is being made
     * @param newTxn       Transaction being initiated
     * @param existingTxns List of existing transactions existing in repo for this bill
     */
    private void validateIfTxnExistsForBill(Map<String, String> errorMap, BillDetail billDetail, Transaction newTxn,
                                            List<Transaction> existingTxns) {
        for (Transaction curr : existingTxns) {
            if (curr.getTxnStatus().equals(Transaction.TxnStatusEnum.PENDING) || curr
                    .getTxnStatus().equals(Transaction.TxnStatusEnum.SUCCESS)) {
                errorMap.put("BILL_ALREADY_PAID", "Bill has already been paid or is in pending state");
            }
        }

        validateIfTxnForBillAbsent(errorMap, billDetail, newTxn );
    }

    /**
     * Validations if no transaction exists for this bill
     * If part payment is allowed,
     * Amount being paid should not be greater than bill value
     * <p>
     * If part payment is not allowed,
     * Amount being paid should be equal to bill value
     *
     * @param errorMap   Map of errors occurred during validations
     * @param billDetail Bill detail for which payment is being made
     * @param newTxn     Transaction being initiated
     */

    private void validateIfTxnForBillAbsent(Map<String, String> errorMap, BillDetail billDetail,
                                            Transaction newTxn) {

        BigDecimal txnAmount = new BigDecimal(newTxn.getTxnAmount());
        if (billDetail.getPartPaymentAllowed()) {

            if(txnAmount.compareTo(billDetail.getMinimumAmount()) < 0 ){
                log.error("Amount paid of {} cannot be lesser than minimum payable amount of {} for bill detail " +
                        "{}", billDetail.getAmountPaid(), billDetail.getMinimumAmount(), billDetail.getId());
                errorMap.put("AMOUNT_MISMATCH", "Amount paid cannot be greater than bill amount");
            }

            if (billDetail.getTotalAmount().compareTo(txnAmount) < 0) {
                log.error("Transaction Amount of {} cannot be greater than bill amount of {}", newTxn.getTxnAmount()
                        , billDetail.getTotalAmount());
                errorMap.put("TXN_AMOUNT_MISMATCH", "Transaction Amount cannot be greater than bill amount");
            }

        } else {
            if (!(billDetail.getTotalAmount().compareTo(txnAmount) == 0)) {
                log.error("Transaction Amount of {} has to be equal to bill amount of {}", newTxn.getTxnAmount(),
                        billDetail.getTotalAmount());
                errorMap.put("TXN_AMOUNT_MISMATCH", "Transaction Amount has to be equal to bill amount");
            }
        }
    }

}

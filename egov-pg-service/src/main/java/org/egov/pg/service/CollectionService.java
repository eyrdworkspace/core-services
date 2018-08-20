package org.egov.pg.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pg.models.*;
import org.egov.pg.models.enums.CollectionType;
import org.egov.pg.repository.BillingRepository;
import org.egov.pg.repository.CollectionsRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class CollectionService {

    private CollectionsRepository collectionsRepository;
    private BillingRepository billingRepository;

    CollectionService(CollectionsRepository collectionsRepository, BillingRepository billingRepository) {
        this.collectionsRepository = collectionsRepository;
        this.billingRepository = billingRepository;
    }

    List<Receipt> generateReceipt(RequestInfo requestInfo, Transaction transaction) {

        List<Bill> bills = billingRepository.fetchBill(requestInfo, transaction.getTenantId(), transaction.getBillId
                ());

        if (!bills.isEmpty()) {

            enrichBill(bills.get(0), transaction);

            Instrument instrument = Instrument.builder()
                    .amount(new BigDecimal(transaction.getTxnAmount()))
                    .instrumentType(InstrumentType.builder().name("Online").build())
                    .transactionType(TransactionType.Debit)
                    .transactionDateInput(transaction.getAuditDetails().getCreatedTime())
                    .transactionNumber(transaction.getTxnId())
                    .payee(transaction.getUser().getName())
                    .tenantId(transaction.getTenantId())
                    .build();

            Receipt receipt = Receipt.builder()
                    .bill(bills)
                    .instrument(instrument)
                    .tenantId(transaction.getTenantId())
                    .build();

            ReceiptReq receiptReq = ReceiptReq.builder()
                    .receipt(Collections.singletonList(receipt))
                    .tenantId(transaction.getTenantId())
                    .requestInfo(requestInfo)
                    .build();

            List<Receipt> receipts = collectionsRepository.generateReceipt(receiptReq).getReceipts();
            if (receipts.isEmpty()) {
                log.error("Unable to generate receipt");
                throw new CustomException("RECEIPT_GEN_COLLECTION_ERROR", "Receipt generation failed");
            } else
                return receipts;
        } else {
            throw new CustomException("RECEIPT_GEN_INVALID_BILL", "Bill not found");
        }
    }

    private void enrichBill(Bill bill, Transaction transaction) {
        bill.setPaidBy(transaction.getUser().getName());
        bill.setMobileNumber(transaction.getUser().getMobileNumber());
        List<BillDetail> billDetails = bill.getBillDetails();
        billDetails.get(0).setAmountPaid(new BigDecimal(transaction.getTxnAmount()));
        billDetails.get(0).setCollectionType(CollectionType.ONLINE);
    }

}

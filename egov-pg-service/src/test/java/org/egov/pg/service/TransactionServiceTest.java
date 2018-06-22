package org.egov.pg.service;

import org.egov.pg.config.AppProperties;
import org.egov.pg.models.Transaction;
import org.egov.pg.producer.Producer;
import org.egov.pg.repository.TransactionRepository;
import org.egov.pg.validator.TransactionValidator;
import org.egov.pg.web.models.RequestInfo;
import org.egov.pg.web.models.TransactionCriteria;
import org.egov.pg.web.models.TransactionRequest;
import org.egov.pg.web.models.User;
import org.egov.tracer.model.CustomException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.TransientDataAccessResourceException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    private TransactionService transactionService;

    @Mock
    private Producer producer;

    @Mock
    private GatewayService gatewayService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private IdGenService idGenService;

    @Mock
    private AppProperties appProperties;

    @Mock
    private TransactionValidator validator;


    private User user;
    private RequestInfo requestInfo;

    @Before
    public void setUp() {
        user = User.builder().userName("USER001").mobileNumber("9XXXXXXXXX").name("XYZ").tenantId("pb").emailId("").build();
        requestInfo = new RequestInfo("", "", 0L, "", "", "", "", "", "", null, "");


        when(gatewayService.getTxnId(any(Map.class))).thenReturn(Optional.of("ORDERID"));

        Mockito.doNothing().when(producer).push(any(String.class), any(Object.class));

        this.transactionService = new TransactionService(validator, gatewayService, producer, transactionRepository,
                idGenService,
                appProperties);
    }

    /**
     * Valid test for initiating a transaction
     * @throws URISyntaxException
     */

    @Test
    public void initiateTransactionSuccessTest() throws URISyntaxException {
        String redirectUrl = "https://paytm.com";


        Transaction txn = Transaction.builder().txnAmount("100")
                .orderId("ORDER0012")
                .productInfo("Property Tax Payment")
                .gateway("PAYTM")
                .build();
        TransactionRequest transactionRequest = new TransactionRequest(requestInfo, txn);

        Mockito.doNothing().when(validator).validateCreateTxn(any(Transaction.class));
        when(idGenService.generateTxnId(transactionRequest)).thenReturn("PT_001");
        when(gatewayService.initiateTxn(any(Transaction.class))).thenReturn(new URI(redirectUrl));

        URI resp = transactionService.initiateTransaction(transactionRequest);

        assertTrue(resp.toString().equalsIgnoreCase(redirectUrl));

    }

    /**
     * Test for invalid or inactive gateway
     */
    @Test(expected = CustomException.class)
    public void initiateTransactionFailTest(){
        Transaction txn = Transaction.builder().txnAmount("100")
                .orderId("ORDER0012")
                .productInfo("Property Tax Payment")
                .gateway("ABCD123")
                .build();
        TransactionRequest transactionRequest = new TransactionRequest(requestInfo, txn);


        when(validator.validateUpdateTxn(any(Map.class))).thenThrow(new CustomException("INVALID_GATEWAY", "Invalid " +
                "Gateway"));
        when(gatewayService.initiateTxn(any(Transaction.class))).thenThrow(new CustomException());

        URI resp = transactionService.initiateTransaction(transactionRequest);


    }

    /**
     * Test for fetching transactions based on criteria
     */
    @Test
    public void getTransactionsSuccessTest(){
        Transaction txn = Transaction.builder().txnId("PT_001")
                .txnAmount("100")
                .tenantId("pb")
                .orderId("ORDER0012")
                .productInfo("Property Tax Payment")
                .gateway("ABCD123")
                .build();
        TransactionCriteria criteria = TransactionCriteria.builder().tenantId("pb").txnId("PT_001").build();

        when(transactionRepository.fetchTransactions(criteria)).thenReturn(Collections.singletonList(txn));
        assertEquals(1, transactionService.getTransactions(criteria).size());

        when(transactionRepository.fetchTransactions(criteria)).thenReturn(Collections.emptyList());
        assertEquals(0, transactionService.getTransactions(criteria).size());
    }

    /**
     * DB error occurs while running fetch
     */
    @Test(expected = CustomException.class)
    public void getTransactionsFailTest(){
        TransactionCriteria criteria = TransactionCriteria.builder().tenantId("pb").txnId("PT_001").build();
        when(transactionRepository.fetchTransactions(criteria)).thenThrow(new TransientDataAccessResourceException("test"));

        transactionService.getTransactions(criteria);
    }

    @Test
    public void updateTransactionSuccessTest() {

        Transaction txnStatus = Transaction.builder().txnId("PT_001")
                .txnAmount("100")
                .orderId("ORDER0012")
                .txnStatus(Transaction.TxnStatusEnum.PENDING)
                .productInfo("Property Tax Payment")
                .gateway("PAYTM")
                .build();

        Transaction finalTxnStatus = Transaction.builder().txnId("PT_001")
                .txnAmount("100.00")
                .orderId("ORDER0012")
                .txnStatus(Transaction.TxnStatusEnum.SUCCESS)
                .productInfo("Property Tax Payment")
                .gateway("PAYTM")
                .build();

        when(validator.validateUpdateTxn(any(Map.class))).thenReturn(txnStatus);
        when(gatewayService.getLiveStatus(txnStatus, Collections.singletonMap("ORDERID", "PT_001"))).thenReturn(finalTxnStatus);

        assertEquals(transactionService.updateTransaction(requestInfo, Collections.singletonMap
                ("ORDERID", "PT_001")).getTxnStatus(), Transaction.TxnStatusEnum.SUCCESS);
    }

    /**
     * Invalid transaction id key,
     *  ex, ORDERID, specific to gateway
     */
    @Test(expected = CustomException.class)
    public void updateTransactionFailTest(){

        when(validator.validateUpdateTxn(any(Map.class))).thenThrow(new CustomException("MISSING_TXN_ID", "Cannot process request, missing transaction id"));

        transactionService.updateTransaction(requestInfo, Collections.singletonMap("abc", "PT_001"));

    }

    /**
     * No record of the Transaction exists in DB
     */
    @Test(expected = CustomException.class)
    public void updateTransactionInvalidTxnIdTest() {

        when(validator.validateUpdateTxn(any(Map.class))).thenThrow(new CustomException("TXN_NOT_FOUND", "Transaction not found"));

        transactionService.updateTransaction(requestInfo, Collections.singletonMap("abc", "PT_001"));
    }
}

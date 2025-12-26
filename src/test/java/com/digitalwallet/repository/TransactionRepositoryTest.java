package com.digitalwallet.repository;

import com.digitalwallet.entity.TransactionStatus;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;
    @Test
    public void testFindBySenderWalletId() {
        assertEquals(21,transactionRepository.findBySenderWalletWalletId(1).size());
    }

    @Test
    public void testFindByReceiverWalletId() {
        assertEquals(4,transactionRepository.findByReceiverWalletWalletId(1).size());
    }

    @Test
    public  void testFindByStatusForSUCCESS() {
        assertEquals(16,transactionRepository.findByStatus(TransactionStatus.SUCCESS).size());
    }


    @Test
    public void testFindByStatusForFAILED() {
        assertEquals(2,transactionRepository.findByStatus(TransactionStatus.FAILED).size());
    }

    @Test
    public void testFindByStatusForPENDING() {
        assertEquals(16,transactionRepository.findByStatus(TransactionStatus.PENDING).size());
    }
    @Test
    public void testTransactionsBetween() {
        assertEquals(21,transactionRepository.findTransactionsBetween(1,2).size());
    }

    @Test
    public void testFindTransactionWithAmountGreaterThan() {
        assertEquals(28,transactionRepository.findTransactionWithAmountGreaterThan(new BigDecimal(100)).size());
    }
}
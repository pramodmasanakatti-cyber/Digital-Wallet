package com.digitalwallet.repository;

import com.digitalwallet.entity.Transaction;
import com.digitalwallet.entity.enums.TransactionStatus;
import com.digitalwallet.entity.Wallet;
import com.digitalwallet.entity.enums.WalletType;
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
    public void testFindById() {

        assertNotNull(transactionRepository.findById(1));
    }

}
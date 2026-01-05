package com.digitalwallet.repository;

import com.digitalwallet.entity.Transaction;
import com.digitalwallet.entity.TransactionStatus;
import com.digitalwallet.entity.Wallet;
import com.digitalwallet.entity.WalletType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.event.WindowListener;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;
@Test
public void testFindTransactionByAmount() {
    assertEquals(2,transactionRepository.findTransactionByAmount(new BigDecimal(100)).size());
}


@Test
public void testFindAllByWalletId() {
    assertEquals(3,transactionRepository.findAllByWalletId(1).size());
}

    @Test
    public void testSave() {
        Transaction transaction=new Transaction();
        Wallet senderWallet=new Wallet();
        senderWallet.setWalletId(1);
        senderWallet.setBalance(new BigDecimal(100));
        senderWallet.setWalletType(WalletType.SAVINGS);

        Wallet reveiverWallet=new Wallet();
        reveiverWallet.setWalletId(2);
        reveiverWallet.setWalletType(WalletType.SAVINGS);
        reveiverWallet.setBalance(new BigDecimal(100));

        transaction.setAmount(new BigDecimal(100));
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setSenderWallet(senderWallet);
        transaction.setReceiverWallet(reveiverWallet);
        transactionRepository.save(transaction);
        assertNotNull(transaction.getTransactionId());
    }

    @Test
    public void testFindById() {

        assertNotNull(transactionRepository.findById(1));
    }

}
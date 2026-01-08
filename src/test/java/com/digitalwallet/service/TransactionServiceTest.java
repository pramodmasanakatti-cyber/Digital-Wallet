package com.digitalwallet.service;

import com.digitalwallet.dto.request.TransactionRequestDTO;
import com.digitalwallet.dto.response.TransactionResponseDTO;
import com.digitalwallet.entity.User;
import com.digitalwallet.entity.Wallet;
import com.digitalwallet.entity.enums.TransactionType;
import com.digitalwallet.entity.enums.WalletStatus;
import com.digitalwallet.entity.enums.WalletType;
import com.digitalwallet.exception.InsufficientFundException;
import com.digitalwallet.exception.WalletNotFoundException;
import com.digitalwallet.repository.TransactionRepository;
import com.digitalwallet.repository.UserRepository;
import com.digitalwallet.repository.WalletRepository;
import com.digitalwallet.service.interfaces.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TransactionServiceTest {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionService transactionService;

    private Wallet senderWallet;
    private Wallet receiverWallet;

    @BeforeEach
    void setUp() {
        User user1=new User();
        user1.setPhone("2345");
        user1.setEmail("ghfbnjds@gmail.com");
        user1.setFullName("Abc");
        user1.setAge(20);

        User user2=new User();
        user2.setPhone("345654");
        user2.setEmail("hj@gmail.com");
        user2.setFullName("Xyz");
        user2.setAge(34);

        User sender=userRepository.save(user1);
        User receiver=userRepository.save(user2);

        senderWallet=new Wallet();

        senderWallet.setBalance(new BigDecimal("1000"));
        senderWallet.setWalletType(WalletType.SAVINGS);
        senderWallet.setStatus(WalletStatus.ACTIVE);
        senderWallet.setUser(sender);
        walletRepository.save(senderWallet);

       receiverWallet=new Wallet();
       receiverWallet.setBalance(new BigDecimal("500"));
       receiverWallet.setWalletType(WalletType.SAVINGS);
       receiverWallet.setStatus(WalletStatus.ACTIVE);
       receiverWallet.setUser(receiver);
       walletRepository.save(receiverWallet);

    }

    // Test successful credit
    @Test
    public void testCredit_SUCCESS() {
        TransactionRequestDTO dto=new TransactionRequestDTO();
        dto.setAmount(new BigDecimal("100"));
        dto.setReceiverWalletId(receiverWallet.getWalletId());
        dto.setExternalTxId("TX123");

        TransactionResponseDTO response=transactionService.credit(dto);
        Wallet updatedWallet=walletRepository.findById(receiverWallet.getWalletId()).get();

        assertEquals(new BigDecimal("600"),updatedWallet.getBalance());
        assertEquals("TX123",response.getExternalTxId());
        assertEquals(TransactionType.CREDIT,response.getTransactionType());
    }

    //Test duplicate transaction rollback
    @Test
    public void testCredit_DuplicateTransaction_ShouldRollback() {
        TransactionRequestDTO dto1=new TransactionRequestDTO();
        dto1.setExternalTxId("TXN1");
        dto1.setAmount(new BigDecimal("100"));
        dto1.setReceiverWalletId(receiverWallet.getWalletId());

        transactionService.credit(dto1);

        TransactionRequestDTO dto2=new TransactionRequestDTO();
        dto1.setExternalTxId("TXN1");
        dto1.setAmount(new BigDecimal("100"));
        dto1.setReceiverWalletId(receiverWallet.getWalletId());

        assertThrows(org.springframework.dao.InvalidDataAccessApiUsageException.class,()->transactionService.credit(dto2));

    }

    
    // Test for transfer for insufficient funds
    @Test
    public void testTransfer_Insufficient_Fund() {
        TransactionRequestDTO dto=new TransactionRequestDTO();
        dto.setAmount(new BigDecimal("1500"));
        dto.setSenderWalletId(senderWallet.getWalletId());
        dto.setReceiverWalletId(receiverWallet.getWalletId());
        dto.setExternalTxId("TXN1");

        assertThrows(InsufficientFundException.class,()->transactionService.transfer(dto));

    }

    // Test for successful transfer
    @Test
    public void testTransfer_SUCCESS() {
        TransactionRequestDTO dto=new TransactionRequestDTO();
        dto.setExternalTxId("TXN1");
        dto.setAmount(new BigDecimal("900"));
        dto.setSenderWalletId(senderWallet.getWalletId());
        dto.setReceiverWalletId(receiverWallet.getWalletId());

        TransactionResponseDTO response=transactionService.transfer(dto);
        Wallet wallet=walletRepository.findById(senderWallet.getWalletId()).orElseThrow(()->new WalletNotFoundException("Wallet not found"));
        assertEquals(new BigDecimal("100"),wallet.getBalance());
        assertEquals(TransactionType.TRANSFER,response.getTransactionType());
        assertEquals("TXN1",response.getExternalTxId());

    }

    // Test successful debit
    @Test
    public void testDebit_SUCCESS() {
        TransactionRequestDTO dto=new TransactionRequestDTO();
        dto.setSenderWalletId(senderWallet.getWalletId());
        dto.setAmount(new BigDecimal("400"));
        dto.setExternalTxId("TXN1");

        TransactionResponseDTO response=transactionService.debit(dto);
        Wallet wallet=walletRepository.findById(senderWallet.getWalletId()).orElseThrow(()->new WalletNotFoundException("Wallet not found"));
        assertEquals(new BigDecimal("600"),wallet.getBalance());
        assertEquals(TransactionType.DEBIT,response.getTransactionType());
        assertEquals("TXN1",response.getExternalTxId());
    }
}
package com.digitalwallet.service;

import com.digitalwallet.dto.request.TransactionRequestDTO;
import com.digitalwallet.dto.response.TransactionResponseDTO;

import com.digitalwallet.entity.Wallet;
import com.digitalwallet.entity.enums.TransactionType;
import com.digitalwallet.entity.enums.WalletStatus;
import com.digitalwallet.entity.enums.WalletType;
import com.digitalwallet.exception.transaction.InsufficientFundException;
import com.digitalwallet.exception.transaction.TransactionLimitExceedException;
import com.digitalwallet.exception.wallet.WalletNotFoundException;
import com.digitalwallet.repository.TransactionRepository;
import com.digitalwallet.repository.WalletRepository;
import com.digitalwallet.service.interfaces.TransactionServiceForUser;
import com.digitalwallet.service.modificationsservices.TransactionDeleteService;
import com.digitalwallet.service.modificationsservices.WalletDeleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceTest {

  @Autowired
  private WalletRepository walletRepository;

 @Autowired
  private TransactionServiceForUser transactionServiceForUser;

@Autowired
private TransactionRepository transactionRepository;

@Autowired
private TransactionDeleteService transactionDeleteService;

@Autowired
private WalletDeleteService walletDeleteService;
private Wallet sender;

private Wallet receiver;
    @BeforeEach
    void setUp() {

        walletDeleteService.deleteWallets();
        transactionDeleteService.deleteTransactions();
        Wallet wallet1,wallet2;
        wallet1=new Wallet();
        wallet1.setStatus(WalletStatus.ACTIVE);
        wallet1.setBalance(new BigDecimal("1000"));
        wallet1.setWalletType(WalletType.SAVINGS);
        wallet1.setUserId(1);


        wallet2=new Wallet();
        wallet2.setStatus(WalletStatus.ACTIVE);
        wallet2.setBalance(new BigDecimal("600"));
        wallet2.setWalletType(WalletType.SAVINGS);
        wallet2.setUserId(2);

        sender=walletRepository.save(wallet1);
        receiver=walletRepository.save(wallet2);
    }

    // Test successful credit
    @Test
    public void testCredit_SUCCESS() {

        TransactionRequestDTO dto1=new TransactionRequestDTO();
        String externalTxId=UUID.randomUUID().toString();
        dto1.setExternalTxId(externalTxId);
        dto1.setAmount(new BigDecimal("100"));
        dto1.setWalletId(sender.getWalletId());

        TransactionResponseDTO responseDTO= transactionServiceForUser.credit(dto1);
        sender=walletRepository.findById(sender.getWalletId()).orElseThrow(()->new WalletNotFoundException("Wallet not found"));

        assertEquals(new BigDecimal("1100.00"),sender.getBalance());

    }

    //Test duplicate transaction rollback
    @Test
    public void testCredit_DuplicateExternalKey_ShouldRollback() {
        TransactionRequestDTO dto1=new TransactionRequestDTO();
        String externalTxId=UUID.randomUUID().toString();
        dto1.setExternalTxId(externalTxId);
        dto1.setAmount(new BigDecimal("100"));
        dto1.setWalletId(sender.getWalletId());

        TransactionResponseDTO responseDTO1= transactionServiceForUser.credit(dto1);

        TransactionRequestDTO dto2=new TransactionRequestDTO();
        dto2.setExternalTxId(externalTxId);
        dto2.setAmount(new BigDecimal("100"));
        dto2.setWalletId(sender.getWalletId());

        TransactionResponseDTO responseDTO2= transactionServiceForUser.credit(dto2);
        assertEquals(responseDTO1.getTransactionId(),responseDTO2.getTransactionId());

    }


    // Test for transfer for insufficient funds
    @Test
    public void testTransfer_Insufficient_Fund() {
        TransactionRequestDTO dto=new TransactionRequestDTO();
        String externalTxId=UUID.randomUUID().toString();
        dto.setAmount(new BigDecimal("1500"));
        dto.setSenderWalletId(sender.getWalletId());
        dto.setReceiverWalletId(receiver.getWalletId());
        dto.setExternalTxId(externalTxId);

        assertThrows(InsufficientFundException.class,()-> transactionServiceForUser.transfer(dto));

    }

    // Test for successful transfer
    @Test
    public void testTransfer_SUCCESS() {

        TransactionRequestDTO dto=new TransactionRequestDTO();
        String externalTxId=UUID.randomUUID().toString();
        dto.setExternalTxId(externalTxId);
        dto.setAmount(new BigDecimal("900"));
        dto.setSenderWalletId(sender.getWalletId());
        dto.setReceiverWalletId(receiver.getWalletId());

        TransactionResponseDTO response= transactionServiceForUser.transfer(dto);
        Wallet wallet=walletRepository.findById(sender.getWalletId()).orElseThrow(()->new WalletNotFoundException("Wallet not found"));
        assertEquals(new BigDecimal("100.00"),wallet.getBalance());
        assertEquals(TransactionType.TRANSFER,response.getTransactionType());
    }

    // Test successful debit
    @Test
    public void testDebit_SUCCESS() {

        TransactionRequestDTO dto=new TransactionRequestDTO();
        String externalTxId=UUID.randomUUID().toString();
        dto.setWalletId(sender.getWalletId());
        dto.setAmount(new BigDecimal("400"));
        dto.setExternalTxId(externalTxId);

        TransactionResponseDTO response= transactionServiceForUser.debit(dto);
        Wallet wallet=walletRepository.findById(sender.getWalletId()).orElseThrow(()->new WalletNotFoundException("Wallet not found"));
        assertEquals(new BigDecimal("600.00"),wallet.getBalance());
        assertEquals(TransactionType.DEBIT,response.getTransactionType());
    }

    @Test
    public void testTransactionLimitExceed() {

        TransactionRequestDTO dto=new TransactionRequestDTO();
        String externalTxId=UUID.randomUUID().toString();
        dto.setWalletId(receiver.getWalletId());
        dto.setAmount(new BigDecimal("20000"));
        dto.setExternalTxId(externalTxId);

        assertThrows(TransactionLimitExceedException.class,()-> transactionServiceForUser.debit(dto));
    }
}
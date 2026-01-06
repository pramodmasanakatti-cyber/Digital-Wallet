package com.digitalwallet.service;

import com.digitalwallet.dto.request.TransactionRequestDTO;
import com.digitalwallet.dto.response.TransactionResponseDTO;
import com.digitalwallet.entity.Transaction;
import com.digitalwallet.entity.Wallet;
import com.digitalwallet.entity.enums.TransactionStatus;
import com.digitalwallet.entity.enums.TransactionType;
import com.digitalwallet.exception.DuplicateTransactionException;

import com.digitalwallet.exception.WalletNotFoundException;
import com.digitalwallet.mapper.TransactionMapper;
import com.digitalwallet.repository.TransactionRepository;
import com.digitalwallet.repository.WalletRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import java.math.BigDecimal;

@Service
public class TransactionService {
    private final WalletRepository walletRepository;

    public TransactionService(WalletRepository walletRepository, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    // Credit transaction
    @Transactional(propagation = Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
    public TransactionResponseDTO credit(TransactionRequestDTO transactionRequestDTO) {
        if (transactionRepository.existsByExternalTxId(transactionRequestDTO.getExternalTxId())) {
           throw new DuplicateTransactionException("Duplicate transaction for same externatTransactionId: " + transactionRequestDTO.getExternalTxId());
        }


        // Check wallet exist or not
        Wallet wallet = walletRepository.findById(transactionRequestDTO.getReceiverWalletId()).orElseThrow(() -> new WalletNotFoundException("Wallet not found walletId:" + transactionRequestDTO.getReceiverWalletId()));

        //credit amount fom wallet
        wallet.credit(transactionRequestDTO.getAmount());

        // save credit changes
        walletRepository.save(wallet);


        Transaction transaction=transactionMapper.toEntity(transactionRequestDTO);
        transaction.setTransactionType(TransactionType.CREDIT);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    // Debit transaction
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public TransactionResponseDTO debit(TransactionRequestDTO transactionRequestDTO) {

        if(transactionRepository.existsByExternalTxId(transactionRequestDTO.getExternalTxId())) {
       throw new DuplicateTransactionException("Duplicate transaction for same externatTransactionId: " + transactionRequestDTO.getExternalTxId());
        }

        // Check wallet exist or not
        Wallet wallet=walletRepository.findById(transactionRequestDTO.getSenderWalletId()).orElseThrow(()->new WalletNotFoundException("Wallet not found walletId: " + transactionRequestDTO.getSenderWalletId()));
        wallet.debit(transactionRequestDTO.getAmount());
        walletRepository.save(wallet);
        Transaction transaction=transactionMapper.toEntity(transactionRequestDTO);
        transaction.setTransactionType(TransactionType.DEBIT);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    // Transfer transaction
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.SERIALIZABLE)
    public TransactionResponseDTO transfer(TransactionRequestDTO transactionRequestDTO) {
        if(transactionRepository.existsByExternalTxId(transactionRequestDTO.getExternalTxId())) {
          throw new DuplicateTransactionException("Duplicate transaction for same externatTransactionId: " + transactionRequestDTO.getExternalTxId());

        }

        // Check senderWallet exist
        Wallet senderWallet=walletRepository.findById(transactionRequestDTO.getSenderWalletId()).orElseThrow(()->new WalletNotFoundException("Sender Wallet not found senderWalletId: " + transactionRequestDTO.getSenderWalletId()));

        // Check receiverWallet exist
        Wallet receiverWallet=walletRepository.findById(transactionRequestDTO.getReceiverWalletId()).orElseThrow(()->new WalletNotFoundException("Receiver Wallet not found receiverWalletId: " + transactionRequestDTO.getReceiverWalletId()));

        senderWallet.debit(transactionRequestDTO.getAmount());
        receiverWallet.credit(transactionRequestDTO.getAmount());

        // Save debit changes from sender wallet
        walletRepository.save(senderWallet);

        // Save credit changes to receiver wallet
        walletRepository.save(receiverWallet);


        Transaction transaction=transactionMapper.toEntity(transactionRequestDTO);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setTransactionType(TransactionType.TRANSFER);
        transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }
}

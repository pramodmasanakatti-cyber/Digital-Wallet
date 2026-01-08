package com.digitalwallet.service;

import com.digitalwallet.dto.request.TransactionRequestDTO;
import com.digitalwallet.dto.response.TransactionResponseDTO;
import com.digitalwallet.entity.Transaction;
import com.digitalwallet.entity.Wallet;
import com.digitalwallet.entity.enums.TransactionStatus;
import com.digitalwallet.entity.enums.TransactionType;
import com.digitalwallet.exception.DuplicateTransactionException;

import com.digitalwallet.exception.TransactionLimitExceedException;
import com.digitalwallet.mapper.TransactionMapper;
import com.digitalwallet.repository.TransactionRepository;
import com.digitalwallet.repository.WalletRepository;
import com.digitalwallet.service.interfaces.TransactionService;
import com.digitalwallet.service.interfaces.WalletService;
import com.digitalwallet.specification.TransactionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.SpringTransactionAnnotationParser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TransactionServiceImplementation implements TransactionService {
    private final WalletRepository walletRepository;
    private final WalletService walletService;

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImplementation(WalletRepository walletRepository, WalletService walletService, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.walletRepository = walletRepository;
        this.walletService = walletService;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    // Credit transaction
    @Transactional(propagation = Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
    public TransactionResponseDTO credit(TransactionRequestDTO transactionRequestDTO) {

        // Check the transaction is currently processing (Idempotency)
       validateDuplicateExternalKey(transactionRequestDTO.getExternalTxId());

       // Get wallet
       Wallet wallet=walletService.getWalletById(transactionRequestDTO.getWalletId());

       // Credit amount to wallet
       walletService.credit(wallet,transactionRequestDTO.getAmount());

       // Create transaction object
       Transaction transaction=buildTransaction(transactionRequestDTO,TransactionType.CREDIT);

       // Save transaction
       transactionRepository.save(transaction);

        return transactionMapper.toDto(transaction);
    }


    // Debit transaction
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public TransactionResponseDTO debit(TransactionRequestDTO transactionRequestDTO) {

        // Check the transaction is currently processing (Idempotency)
        validateDuplicateExternalKey(transactionRequestDTO.getExternalTxId());

        // Get wallet
        Wallet wallet=walletService.getWalletById(transactionRequestDTO.getWalletId());

        // Validate transaction limit before debiting
        validateTransactionLimit(wallet,transactionRequestDTO.getAmount());

        // Debit amount from wallet
        walletService.debit(wallet,transactionRequestDTO.getAmount());

        // Create transaction object
        Transaction transaction=buildTransaction(transactionRequestDTO,TransactionType.DEBIT);

        // Save transaction
        transactionRepository.save(transaction);

        return transactionMapper.toDto(transaction);
    }

    // Transfer transaction
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.SERIALIZABLE)
    public TransactionResponseDTO transfer(TransactionRequestDTO transactionRequestDTO) {

        // Check the transaction is currently processing (Idempotency)
        validateDuplicateExternalKey(transactionRequestDTO.getExternalTxId());

        // Get sender wallet
        Wallet senderWallet=walletService.getWalletById(transactionRequestDTO.getSenderWalletId());

        // Get receiver wallet
        Wallet receiverWallet=walletService.getWalletById(transactionRequestDTO.getReceiverWalletId());

        // Validate transaction limit for sender before transaction
        validateTransactionLimit(senderWallet,transactionRequestDTO.getAmount());

        // Debit money from sender
        walletService.debit(senderWallet,transactionRequestDTO.getAmount());

        // Credit money to receiver
        walletService.credit(receiverWallet,transactionRequestDTO.getAmount());

        // Create transaction object
        Transaction transaction=buildTransaction(transactionRequestDTO,TransactionType.TRANSFER);

        // Save transaction
        transactionRepository.save(transaction);

        return transactionMapper.toDto(transaction);
    }

    @Override
    public Page<Transaction> getTransactionHistory(Integer walletId, TransactionType type, LocalDateTime from, LocalDateTime to, TransactionStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionDate").descending());

        Specification<Transaction> specification=
                Specification.where(TransactionSpecification.byWallet(walletId))
                        .and(TransactionSpecification.byType(type))
                        .and(TransactionSpecification.byStatus(status))
                        .and(TransactionSpecification.byDateRange(from,to));
        return transactionRepository.findAll(specification,pageable);
    }


    private void validateTransactionLimit(Wallet wallet, BigDecimal amount) {

        BigDecimal dailyTotal=transactionRepository.sumByWalletAndDate(wallet.getWalletId(), LocalDate.now());
        if(dailyTotal.add(amount).compareTo(new BigDecimal("10000"))>0) {
            throw new TransactionLimitExceedException("You have crossed transaction limit");
        }
    }



    private Transaction buildTransaction(TransactionRequestDTO transactionRequestDTO, TransactionType transactionType) {
        Transaction transaction=transactionMapper.toEntity(transactionRequestDTO);
        transaction.setTransactionType(transactionType);
        transaction.setStatus(TransactionStatus.COMPLETED);
        return  transaction;
    }

    private void validateDuplicateExternalKey(String externalTxId) {
        if(transactionRepository.existsByExternalTxId(externalTxId)) {
            throw new DuplicateTransactionException("Duplicate transaction for same externalTransactionId: " + externalTxId);
        }
    }


}

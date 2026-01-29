package com.digitalwallet.service;

import com.digitalwallet.dto.request.TransactionRequestDTO;
import com.digitalwallet.dto.response.TransactionResponseDTO;
import com.digitalwallet.entity.Transaction;
import com.digitalwallet.entity.enums.TransactionStatus;
import com.digitalwallet.entity.enums.TransactionType;
import com.digitalwallet.exception.*;

import com.digitalwallet.exception.transaction.InvalidTransactionExceptionCustom;
import com.digitalwallet.exception.transaction.TransactionLimitExceedException;
import com.digitalwallet.mapper.TransactionMapper;
import com.digitalwallet.repository.TransactionRepository;
import com.digitalwallet.service.interfaces.TransactionServiceForAdmin;
import com.digitalwallet.service.interfaces.TransactionServiceForUser;
import com.digitalwallet.service.interfaces.WalletServiceForTransaction;
import com.digitalwallet.specification.TransactionSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionServiceForUser, TransactionServiceForAdmin {
    private final WalletServiceForTransaction walletService;
    private final TransactionMapper transactionMapper;
    private final TransactionLifeCycleService transactionLifeCycleService;
    private final TransactionIdempotencyService transactionIdempotencyService;
    private final TransactionRepository transactionRepository;

    // Credit transaction
    @Transactional(propagation = Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
    public TransactionResponseDTO credit(TransactionRequestDTO transactionRequestDTO) {
        transactionRequestDTO.setSenderWalletId(null);
        transactionRequestDTO.setReceiverWalletId(null);

        // Check the transaction is currently processing (Idempotency)
        TransactionResponseDTO idempotency=transactionIdempotencyService.checkIdempotency(transactionRequestDTO);

        if(idempotency!=null) {
            return idempotency;
        }

        // Check wallet exist or not
        walletService.checkWallet(transactionRequestDTO.getWalletId());

        // Create transaction object and save as pending
        Transaction transaction = transactionLifeCycleService.markPendingTransaction(transactionRequestDTO, TransactionType.CREDIT);

        try {

            // Credit amount to wallet
            walletService.credit(transactionRequestDTO.getWalletId(), transactionRequestDTO.getAmount());

            // Mark transaction as completed and return response
            return transactionMapper.toDto(transactionLifeCycleService.markCompletedTransaction(transaction));

        }  catch (BusinessException exception) {

            // Mark transaction as failed
            transactionLifeCycleService.markFailedTransaction(transaction);

            throw exception;
        }
    }

    // Debit transaction
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public TransactionResponseDTO debit(TransactionRequestDTO transactionRequestDTO) {

        transactionRequestDTO.setSenderWalletId(null);
        transactionRequestDTO.setReceiverWalletId(null);

        // Check the transaction is currently processing (Idempotency)
        TransactionResponseDTO idempotency=transactionIdempotencyService.checkIdempotency(transactionRequestDTO);
        if(idempotency!=null) {
            return idempotency;
        }

        // Check wallet exist or not
        walletService.checkWallet(transactionRequestDTO.getWalletId());

        // Create transaction object and save as pending
        Transaction transaction=transactionLifeCycleService.markPendingTransaction(transactionRequestDTO,TransactionType.DEBIT);

      try {

          // Validate transaction limit before debiting
          validateTransactionLimit(transactionRequestDTO.getWalletId(), transactionRequestDTO.getAmount());

          // Debit amount from wallet
          walletService.debit(transactionRequestDTO.getWalletId(), transactionRequestDTO.getAmount());

          // Mark transaction as completed and return response
          return transactionMapper.toDto(transactionLifeCycleService.markCompletedTransaction(transaction));


      }  catch (BusinessException exception) {

          // Mark transaction as failed
          transactionLifeCycleService.markFailedTransaction(transaction);
          throw exception;
      }

    }

    // Transfer transaction
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public TransactionResponseDTO transfer(TransactionRequestDTO transactionRequestDTO) {
         transactionRequestDTO.setWalletId(null);

        if(transactionRequestDTO.getSenderWalletId().equals(transactionRequestDTO.getReceiverWalletId())) throw new InvalidTransactionExceptionCustom("Transaction cannot possible because senderWallet and receiverWallet are same");

        // Check the transaction is currently processing (Idempotency)
        TransactionResponseDTO idempotency=transactionIdempotencyService.checkIdempotency(transactionRequestDTO);

        if(idempotency!=null) {
            return idempotency;
        }

        // Check sender wallet exist or not
        walletService.checkWallet(transactionRequestDTO.getSenderWalletId());

        // Check receiver wallet exist or not
        walletService.checkWallet(transactionRequestDTO.getReceiverWalletId());

        // Create transaction object and save as pending
        Transaction transaction=transactionLifeCycleService.markPendingTransaction(transactionRequestDTO,TransactionType.TRANSFER);

        try {

            // Validate transaction limit for sender before transaction
            validateTransactionLimit(transactionRequestDTO.getSenderWalletId(), transactionRequestDTO.getAmount());

            // Debit money from sender
            walletService.debit(transactionRequestDTO.getSenderWalletId(), transactionRequestDTO.getAmount());

            // Credit money to receiver
            walletService.credit(transactionRequestDTO.getReceiverWalletId(), transactionRequestDTO.getAmount());

            // Mark transaction as completed and return response
            return transactionMapper.toDto(transactionLifeCycleService.markCompletedTransaction(transaction));

        }  catch (BusinessException exception) {

            // Mark transaction as failed
            transactionLifeCycleService.markFailedTransaction(transaction);
            throw exception;
        }
    }

    // Retriving transaction history based on different parameters

    @Override
    public Page<TransactionResponseDTO> getTransactionHistory(Integer walletId, TransactionType type, LocalDateTime from, LocalDateTime to, TransactionStatus status, int page, int size) {
        walletService.checkWallet(walletId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionDate").descending());
        Specification<Transaction> specification=
                Specification.where(TransactionSpecification.byWallet(walletId))
                        .and(TransactionSpecification.byType(type))
                        .and(TransactionSpecification.byStatus(status))
                        .and(TransactionSpecification.byDateRange(from,to));
        return transactionRepository.findAll(specification,pageable).map(transactionMapper::toDto);
    }


    // Retriving all transactions (This is for ADMIN only)
    @Override
    public List<TransactionResponseDTO> getAllTransactions() {
        return
                transactionRepository.findAll().stream().map(transactionMapper::toDto).toList();
    }

    // Validating transaction limit before doing transaction
    private void validateTransactionLimit(Integer walletId, BigDecimal amount) {
        BigDecimal dailyTotal=transactionRepository.sumByWalletAndDate(walletId, LocalDate.now());
        if(dailyTotal.add(amount).compareTo(new BigDecimal("10000"))>0) {
            throw new TransactionLimitExceedException("You have crossed transaction limit");
        }
    }
}

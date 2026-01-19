package com.digitalwallet.service;

import com.digitalwallet.dto.request.TransactionRequestDTO;
import com.digitalwallet.dto.response.TransactionResponseDTO;
import com.digitalwallet.entity.Transaction;
import com.digitalwallet.entity.Wallet;
import com.digitalwallet.entity.enums.TransactionStatus;
import com.digitalwallet.entity.enums.TransactionType;
import com.digitalwallet.exception.*;

import com.digitalwallet.mapper.TransactionMapper;
import com.digitalwallet.repository.TransactionRepository;
import com.digitalwallet.repository.WalletRepository;
import com.digitalwallet.service.interfaces.TransactionService;
import com.digitalwallet.service.interfaces.WalletService;
import com.digitalwallet.specification.TransactionSpecification;
import jakarta.transaction.InvalidTransactionException;
import org.springframework.dao.OptimisticLockingFailureException;
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

@Service
public class TransactionServiceImpl implements TransactionService {
    private final WalletRepository walletRepository;
    private final WalletService walletService;

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionFailureService transactionFailureService;
    private final TransactionPendingService transactionPendingService;

    public TransactionServiceImpl(WalletRepository walletRepository, WalletService walletService, TransactionRepository transactionRepository, TransactionMapper transactionMapper, TransactionFailureService transactionFailureService, TransactionPendingService transactionPendingService) {
        this.walletRepository = walletRepository;
        this.walletService = walletService;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.transactionFailureService = transactionFailureService;
        this.transactionPendingService = transactionPendingService;
    }

    // Credit transaction
    @Transactional(propagation = Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED)
    public TransactionResponseDTO credit(TransactionRequestDTO transactionRequestDTO) {

        // Check the transaction is currently processing (Idempotency)
        if(transactionRepository.existsByExternalTxId(transactionRequestDTO.getExternalTxId())) {
           return transactionMapper.toDto(transactionRepository.findByExternalTxId(transactionRequestDTO.getExternalTxId()).orElseThrow(()->new TransactionNotFoundException("Transaction not found for externalTxId: " + transactionRequestDTO.getExternalTxId())));
        }

        // Get wallet
        Wallet wallet = walletService.getWalletById(transactionRequestDTO.getWalletId());

        // Create transaction object and save as pending
        Transaction transaction = transactionPendingService.savePendingTransaction(transactionRequestDTO, TransactionType.CREDIT);
        try {
            // Credit amount to wallet
            walletService.credit(wallet, transactionRequestDTO.getAmount());
            transaction.setStatus(TransactionStatus.COMPLETED);
            transactionRepository.save(transaction);
            return transactionMapper.toDto(transaction);
        }  catch (WalletInactiveException exception) {
            transactionFailureService.markFailedTransaction(transaction);
            throw exception;
        } catch (InsufficientFundException exception) {
            transactionFailureService.markFailedTransaction(transaction);
           throw exception;
        } catch (InvalidAmountException exception) {
            transactionFailureService.markFailedTransaction(transaction);
            throw exception;
        }catch (TransactionLimitExceedException exception) {
            transactionFailureService.markFailedTransaction(transaction);
            throw exception;
        }catch (Exception exception) {
            transactionFailureService.markFailedTransaction(transaction);
            throw exception;
        }
    }


    // Debit transaction
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public TransactionResponseDTO debit(TransactionRequestDTO transactionRequestDTO) {

        // Check the transaction is currently processing (Idempotency)
        if(transactionRepository.existsByExternalTxId(transactionRequestDTO.getExternalTxId())) {
            return transactionMapper.toDto(transactionRepository.findByExternalTxId(transactionRequestDTO.getExternalTxId()).orElseThrow(()->new TransactionNotFoundException("Transaction not found for externalTxId: " + transactionRequestDTO.getExternalTxId())));
        }
        // Get wallet
        Wallet wallet = walletService.getWalletById(transactionRequestDTO.getWalletId());

        // Create transaction object and save as pending
        Transaction transaction=transactionPendingService.savePendingTransaction(transactionRequestDTO,TransactionType.DEBIT);
      try {

          // Validate transaction limit before debiting
          validateTransactionLimit(wallet, transactionRequestDTO.getAmount());

          // Debit amount from wallet
          walletService.debit(wallet, transactionRequestDTO.getAmount());

          transaction.setStatus(TransactionStatus.COMPLETED);
          transactionRepository.save(transaction);
          return transactionMapper.toDto(transaction);

      }  catch (WalletInactiveException exception) {
          transactionFailureService.markFailedTransaction(transaction);
          throw exception;
      } catch (InsufficientFundException exception) {
          transactionFailureService.markFailedTransaction(transaction);
          throw exception;
      } catch (InvalidAmountException exception) {
          transactionFailureService.markFailedTransaction(transaction);
          throw exception;
      }catch (TransactionLimitExceedException exception) {
          transactionFailureService.markFailedTransaction(transaction);
          throw exception;
      }catch (Exception exception) {
          transactionFailureService.markFailedTransaction(transaction);
          throw exception;
      }
    }

    // Transfer transaction
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public TransactionResponseDTO transfer(TransactionRequestDTO transactionRequestDTO) {

        if(transactionRequestDTO.getSenderWalletId().equals(transactionRequestDTO.getReceiverWalletId())) throw new InvalidTransactionExceptionCustom("Transaction cannot possible because senderWallet and receiverWallet are same");

        // Check the transaction is currently processing (Idempotency)
        if(transactionRepository.existsByExternalTxId(transactionRequestDTO.getExternalTxId())) {
            return transactionMapper.toDto(transactionRepository.findByExternalTxId(transactionRequestDTO.getExternalTxId()).orElseThrow(()->new TransactionNotFoundException("Transaction not found for externalTxId: " + transactionRequestDTO.getExternalTxId())));
        }


        // Get sender wallet
        Wallet senderWallet = walletService.getWalletById(transactionRequestDTO.getSenderWalletId());

        // Get receiver wallet
        Wallet receiverWallet = walletService.getWalletById(transactionRequestDTO.getReceiverWalletId());

        // Create transaction object and save as pending
        Transaction transaction=transactionPendingService.savePendingTransaction(transactionRequestDTO,TransactionType.TRANSFER);

        try {

            // Validate transaction limit for sender before transaction
            validateTransactionLimit(senderWallet, transactionRequestDTO.getAmount());

            // Debit money from sender
            walletService.debit(senderWallet, transactionRequestDTO.getAmount());

            // Credit money to receiver
            walletService.credit(receiverWallet, transactionRequestDTO.getAmount());

            transaction.setStatus(TransactionStatus.COMPLETED);
            transactionRepository.save(transaction);
            return transactionMapper.toDto(transaction);
        }  catch (WalletInactiveException exception) {
            transactionFailureService.markFailedTransaction(transaction);
            throw exception;
        } catch (InsufficientFundException exception) {
            transactionFailureService.markFailedTransaction(transaction);
            throw exception;
        } catch (InvalidAmountException exception) {
            transactionFailureService.markFailedTransaction(transaction);
            throw exception;
        } catch (TransactionLimitExceedException exception) {
            transactionFailureService.markFailedTransaction(transaction);
            throw exception;
        } catch (Exception exception) {
            transactionFailureService.markFailedTransaction(transaction);
            throw exception;
        }
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

//    private void validateDuplicateExternalKey(String externalTxId) {
//        if(transactionRepository.existsByExternalTxId(externalTxId)) {
//            throw new DuplicateExternalKeyException("Duplicate transaction for same externalTransactionId: " + externalTxId);
//        }
//    }
}

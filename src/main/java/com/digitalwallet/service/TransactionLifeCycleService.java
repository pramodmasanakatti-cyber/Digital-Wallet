package com.digitalwallet.service;

import com.digitalwallet.dto.request.TransactionRequestDTO;
import com.digitalwallet.entity.Transaction;
import com.digitalwallet.entity.enums.TransactionStatus;
import com.digitalwallet.entity.enums.TransactionType;
import com.digitalwallet.mapper.TransactionMapper;
import com.digitalwallet.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionLifeCycleService {

   private final TransactionRepository transactionRepository;
   private final TransactionMapper transactionMapper;
    public TransactionLifeCycleService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Transaction markFailedTransaction(Transaction transaction) {
        transaction.setStatus(TransactionStatus.FAILED);
        return transactionRepository.save(transaction);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Transaction markPendingTransaction(TransactionRequestDTO transactionRequestDTO, TransactionType transactionType) {
        Transaction transaction=transactionMapper.toEntity(transactionRequestDTO);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setTransactionType(transactionType);
        return transactionRepository.save(transaction);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Transaction markCompletedTransaction(Transaction transaction) {
        transaction.setStatus(TransactionStatus.COMPLETED);
        return transactionRepository.save(transaction);
    }

}

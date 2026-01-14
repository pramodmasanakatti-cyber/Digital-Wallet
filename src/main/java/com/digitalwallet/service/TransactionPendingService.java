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
public class TransactionPendingService {
    public TransactionPendingService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Transaction savePendingTransaction(TransactionRequestDTO transactionRequestDTO, TransactionType transactionType) {
        Transaction transaction=transactionMapper.toEntity(transactionRequestDTO);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setTransactionType(transactionType);
        return transactionRepository.save(transaction);
    }
}

package com.digitalwallet.service;

import com.digitalwallet.entity.Transaction;
import com.digitalwallet.entity.enums.TransactionStatus;
import com.digitalwallet.repository.TransactionRepository;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionFailureService {

    public TransactionFailureService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    private final TransactionRepository transactionRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailedTransaction(Transaction transaction) {
        transaction.setStatus(TransactionStatus.FAILED);
        transactionRepository.save(transaction);
    }
}

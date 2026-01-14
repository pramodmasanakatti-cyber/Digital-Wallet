package com.digitalwallet.service;

import com.digitalwallet.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionDeleteService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteTransactions() {
        transactionRepository.deleteAll();
    }
}

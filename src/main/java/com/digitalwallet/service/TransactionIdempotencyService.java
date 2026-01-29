package com.digitalwallet.service;

import com.digitalwallet.dto.request.TransactionRequestDTO;
import com.digitalwallet.dto.response.TransactionResponseDTO;
import com.digitalwallet.mapper.TransactionMapper;
import com.digitalwallet.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionIdempotencyService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionIdempotencyService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    public TransactionResponseDTO checkIdempotency(TransactionRequestDTO dto) {
        return transactionRepository.findByExternalTxId(dto.getExternalTxId())
                .map(transactionMapper::toDto)
                .orElse(null);
    }
}

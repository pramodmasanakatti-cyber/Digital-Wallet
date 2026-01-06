package com.digitalwallet.exception;

import com.digitalwallet.dto.response.TransactionResponseDTO;

public class DuplicateTransactionException extends RuntimeException {
    public DuplicateTransactionException(String message) {
        super(message);
    }
}

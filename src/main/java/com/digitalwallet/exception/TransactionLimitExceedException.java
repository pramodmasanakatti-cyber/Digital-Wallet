package com.digitalwallet.exception;

public class TransactionLimitExceedException extends RuntimeException {
    public TransactionLimitExceedException(String message) {
        super(message);
    }
}

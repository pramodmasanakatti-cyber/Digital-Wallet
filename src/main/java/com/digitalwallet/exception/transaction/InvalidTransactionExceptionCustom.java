package com.digitalwallet.exception.transaction;

public class InvalidTransactionExceptionCustom extends RuntimeException {
    public InvalidTransactionExceptionCustom(String message) {
        super(message);
    }
}

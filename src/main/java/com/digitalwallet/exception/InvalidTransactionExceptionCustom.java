package com.digitalwallet.exception;

public class InvalidTransactionExceptionCustom extends RuntimeException {
    public InvalidTransactionExceptionCustom(String message) {
        super(message);
    }
}

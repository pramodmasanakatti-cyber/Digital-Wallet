package com.digitalwallet.exception.transaction;

public class DuplicateExternalKeyException extends RuntimeException {
    public DuplicateExternalKeyException(String message) {
        super(message);
    }
}

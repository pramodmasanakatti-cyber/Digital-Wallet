package com.digitalwallet.exception;

public class DuplicateExternalKeyException extends RuntimeException {
    public DuplicateExternalKeyException(String message) {
        super(message);
    }
}

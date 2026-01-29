package com.digitalwallet.exception.transaction;

import com.digitalwallet.exception.BusinessException;

public class InsufficientFundException extends BusinessException {
    public InsufficientFundException(String message) {
        super(message);
    }
}

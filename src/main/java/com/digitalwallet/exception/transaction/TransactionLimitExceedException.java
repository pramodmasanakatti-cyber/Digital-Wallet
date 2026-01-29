package com.digitalwallet.exception.transaction;

import com.digitalwallet.exception.BusinessException;

public class TransactionLimitExceedException extends BusinessException {
    public TransactionLimitExceedException(String message) {
        super(message);
    }
}

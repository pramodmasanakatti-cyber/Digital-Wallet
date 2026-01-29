package com.digitalwallet.exception.wallet;

import com.digitalwallet.exception.BusinessException;

public class WalletInactiveException extends BusinessException {
    public WalletInactiveException(String message) {
        super(message);
    }
}

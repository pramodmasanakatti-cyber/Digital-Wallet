package com.digitalwallet.service.interfaces;

import java.math.BigDecimal;

public interface WalletServiceForTransaction {
    void credit(Integer walletId, BigDecimal amount);
    void debit(Integer walletId, BigDecimal amount);
    void checkWallet(Integer walletId);
}

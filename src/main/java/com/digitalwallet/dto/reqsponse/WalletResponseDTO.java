package com.digitalwallet.dto.reqsponse;

import com.digitalwallet.entity.WalletType;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class WalletResponseDTO {
    private Integer walletId;

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public WalletType getWalletType() {
        return walletType;
    }

    public void setWalletType(WalletType walletType) {
        this.walletType = walletType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    private BigDecimal balance;
    private WalletType walletType;
    private Integer userId;
}

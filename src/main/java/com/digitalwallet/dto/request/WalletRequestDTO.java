package com.digitalwallet.dto.request;

import com.digitalwallet.entity.WalletType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WalletRequestDTO {

    @NotNull
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public WalletType getWalletType() {
        return walletType;
    }

    public void setWalletType(WalletType walletType) {
        this.walletType = walletType;
    }

    @NotNull
    private WalletType walletType;
}

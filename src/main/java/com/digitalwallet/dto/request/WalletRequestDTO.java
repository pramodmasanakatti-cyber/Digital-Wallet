package com.digitalwallet.dto.request;

import com.digitalwallet.entity.WalletType;
import com.digitalwallet.validation.annotation.ValidWalletBalance;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletRequestDTO {

    @NotNull(message = "User id is required")
    private Integer userId;

    @NotNull(message = "Wallet type is required")
    private WalletType walletType;

    @ValidWalletBalance
    private BigDecimal balance;
}

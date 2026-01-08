package com.digitalwallet.dto.request;

import com.digitalwallet.entity.enums.WalletStatus;
import com.digitalwallet.entity.enums.WalletType;
import com.digitalwallet.validation.annotation.ValidWalletBalance;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WalletRequestDTO {

    @NotNull(message = "User id is required")
    private Integer userId;

    @NotNull(message = "Wallet type is required")
    private WalletType walletType;

    @ValidWalletBalance
    private BigDecimal balance;


    private WalletStatus status;
}

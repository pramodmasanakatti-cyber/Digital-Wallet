package com.digitalwallet.dto.request;

import com.digitalwallet.entity.enums.WalletStatus;
import com.digitalwallet.entity.enums.WalletType;
import com.digitalwallet.validation.annotation.ValidWalletBalance;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WalletRequestDTO {

    @Schema(
            description = "User id for creating wallet for a user",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "User id is required")
    private Integer userId;


    @Schema(
            description = "Wallet type (SAVINGS, CASH) to create wallet",
            example = "SAVINGS",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Wallet type is required")
    private WalletType walletType;

    @ValidWalletBalance
    @Schema(
            description = "Initial balance while creating wallet",
            example = "1000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal balance;

    @Schema(
            description = "Wallet status",
            example = "ACTIVE",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private WalletStatus status;
}

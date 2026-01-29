package com.digitalwallet.dto.response;

import com.digitalwallet.entity.enums.WalletStatus;
import com.digitalwallet.entity.enums.WalletType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Wallet response")
public class WalletResponseDTO {

    @Schema(
            description = "wallet id generated automatically from server side",
    example = "1")
    private Integer walletId;

    @Schema(
            description = "Wallet balance",
            example = "1000"
    )
    private BigDecimal balance;

    @Schema(
            description = "Type of wallet",
            example = "SAVINGS"
    )
    private WalletType walletType;

    @Schema(
            description = "Wallet status",
            example = "ACTIVE"
    )
    private WalletStatus status;

    @Schema(
            description = "User id which the wallet belongs to",
            example = "1"
    )
    private Integer userId;
}

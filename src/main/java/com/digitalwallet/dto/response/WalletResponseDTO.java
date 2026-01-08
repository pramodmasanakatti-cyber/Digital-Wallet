package com.digitalwallet.dto.response;

import com.digitalwallet.entity.enums.WalletStatus;
import com.digitalwallet.entity.enums.WalletType;
import lombok.*;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WalletResponseDTO {
    private Integer walletId;
    private BigDecimal balance;
    private WalletType walletType;
    private WalletStatus status;
    private Integer userId;
}

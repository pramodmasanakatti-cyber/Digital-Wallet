package com.digitalwallet.dto.response;

import com.digitalwallet.entity.WalletType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletResponseDTO {
    private Integer walletId;
    private BigDecimal balance;
    private WalletType walletType;
    private Integer userId;
}

package com.digitalwallet.dto.request;

import com.digitalwallet.validation.annotation.ValidTransactionAmount;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDTO {

    @NotNull(message = "Sender wallet id is required")
    private Integer senderWalletId;

    @NotNull(message = "Receiver wallet id is required")
    private Integer receiverWalletId;

    @ValidTransactionAmount
    private BigDecimal amount;
}

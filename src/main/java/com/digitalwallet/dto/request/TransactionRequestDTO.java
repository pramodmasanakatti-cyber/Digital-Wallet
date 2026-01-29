package com.digitalwallet.dto.request;

import com.digitalwallet.validation.annotation.ValidTransactionAmount;
import com.digitalwallet.validation.groups.Credit;
import com.digitalwallet.validation.groups.Debit;
import com.digitalwallet.validation.groups.Transfer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TransactionRequestDTO {

    @Schema(
            description = "external transaction id",
            example = "et6g567nmkdhgyujn67hnmg7j",
            requiredMode = Schema.RequiredMode.AUTO)
    @NotNull(groups = {Credit.class, Debit.class, Transfer.class})
    private String externalTxId;

    @Schema(
            description ="walletId is required only when CREDIT,DEBIT operations are performed",
            example = "1"
    )
    @NotNull(groups = {Credit.class,Debit.class})
    private Integer walletId;

    @Schema(
            description ="senderWalletId is required only when TRANSFER operation is performed",
            example = "1"
    )
    @NotNull(message = "Sender wallet id is required",groups = Transfer.class)
    private Integer senderWalletId;

    @Schema(
            description ="receiverWalletId is required only when TRANSFER operation is performed",
            example = "2"
    )
    @NotNull(message = "Receiver wallet id is required",groups =Transfer.class)
    private Integer receiverWalletId;

    @Schema(
            description = "Amount for CREDIT, DEBIT and TRANSFER operation and it should be greater than 0",
            example = "100",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ValidTransactionAmount(groups = {Credit.class,Debit.class,Transfer.class})
    private BigDecimal amount;
}

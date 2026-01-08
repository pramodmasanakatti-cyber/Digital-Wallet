package com.digitalwallet.dto.request;

import com.digitalwallet.validation.annotation.ValidTransactionAmount;
import com.digitalwallet.validation.groups.Credit;
import com.digitalwallet.validation.groups.Debit;
import com.digitalwallet.validation.groups.Transfer;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TransactionRequestDTO {
    @NotNull(groups = {Credit.class, Debit.class, Transfer.class})
    private String externalTxId;

    @NotNull(groups = {Credit.class,Debit.class})
    private Integer walletId;

    @NotNull(message = "Sender wallet id is required",groups = Transfer.class)
    private Integer senderWalletId;

    @NotNull(message = "Receiver wallet id is required",groups =Transfer.class)
    private Integer receiverWalletId;

    @ValidTransactionAmount(groups = {Credit.class,Debit.class,Transfer.class})
    private BigDecimal amount;
}

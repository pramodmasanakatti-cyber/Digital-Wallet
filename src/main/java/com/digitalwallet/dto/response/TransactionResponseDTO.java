package com.digitalwallet.dto.response;

import com.digitalwallet.entity.enums.TransactionStatus;
import com.digitalwallet.entity.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionResponseDTO {
    private Integer transactionId;
    private String externalTxId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
}

package com.digitalwallet.dto.response;

import com.digitalwallet.entity.enums.TransactionStatus;
import com.digitalwallet.entity.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Transaction response")
public class TransactionResponseDTO {
    @Schema(
            description = "transaction id generated automatically from server side",
            example = "1"
    )
    private Integer transactionId;

    @Schema(
            description = "External transaction id for idempotency",
            example = "rytfyguh12ghjnsaij"
    )
    private String externalTxId;

    @Schema(
            description = "Transaction amount",
            example = "1000"
    )
    private BigDecimal amount;

    @Schema(
            description = "Type of transaction",
            example = "CREDIT"
    )
    private TransactionType transactionType;

    @Schema(
            description = "Wallet id",
            example = "1"
    )
    private Integer walletId;

    @Schema(
            description = "Sender wallet id only when TRANSFER operation performed otherwise null",
            example = "1"
    )
    private Integer senderWalletId;

    @Schema(
            description = "Receiver wallet id only when TRANSFER operation performed otherwise null",
            example = "1"
    )
    private Integer receiverWalletId;

    @Schema(
            description = "Transaction status",
            example = "COMPLETED"
    )
    private TransactionStatus status;

    @Schema(
            description = "Transaction date",
            example = "2026-01-29 12:55:13.622367"
    )
    private LocalDateTime transactionDate;
}

package com.digitalwallet.entity;

import com.digitalwallet.entity.enums.TransactionStatus;
import com.digitalwallet.entity.enums.TransactionType;
import com.digitalwallet.validation.annotation.ValidTransactionAmount;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transactions")
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    @Column(name="external_tx_id",nullable = false,unique = true,updatable = false)
    private String externalTxId;

    @Column(nullable = false)
    @ValidTransactionAmount
    private BigDecimal amount;

    private Integer walletId;


    private Integer senderWalletId;


    private Integer receiverWalletId;

    @Enumerated(EnumType.STRING)
    @Column(name="Status",nullable = false)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,updatable = false)
    private TransactionType transactionType;

    private LocalDateTime transactionDate;




    @PrePersist
    void onCreate() {
        this.transactionDate=LocalDateTime.now();
        if(this.status==null) {
            this.status = TransactionStatus.PENDING;
        }
    }
}

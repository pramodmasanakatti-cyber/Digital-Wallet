package com.digitalwallet.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    @Column(nullable = false)
    @DecimalMin(value = "1.0", inclusive = true)
    private BigDecimal amount;


    public Wallet getSenderWallet() {
        return senderWallet;
    }

    public void setSenderWallet(Wallet senderWalletId) {
        this.senderWallet = senderWalletId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Wallet getReceiverWallet() {
        return receiverWallet;
    }

    public void setReceiverWallet(Wallet receiverWalletId) {
        this.receiverWallet = receiverWalletId;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    @ManyToOne
    @JoinColumn(name = "Sender_wallet_id")
    private Wallet senderWallet;

    @ManyToOne
    @JoinColumn(name = "Receiver_wallet_id")
    private Wallet receiverWallet;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private LocalDateTime transactionDate;

}

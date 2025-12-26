package com.digitalwallet.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer walletId;

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public WalletType getWalletType() {
        return walletType;
    }

    public void setWalletType(WalletType walletType) {
        this.walletType = walletType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @ManyToOne
    @JoinColumn(name = "User_id",nullable = false)
    private User user;

    @Column(nullable = false)
    @DecimalMin(value="0.0", inclusive=true)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletType walletType;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public List<Transaction> getSentTransactions() {
        return sentTransactions;
    }

    public void setSentTransactions(List<Transaction> sentTransactions) {
        this.sentTransactions = sentTransactions;
    }

    public List<Transaction> getReceivedTransaction() {
        return receivedTransaction;
    }

    public void setReceivedTransaction(List<Transaction> receivedTransaction) {
        this.receivedTransaction = receivedTransaction;
    }

    @OneToMany(mappedBy = "senderWallet",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Transaction> sentTransactions=new ArrayList<>();

    @OneToMany(mappedBy = "receiverWallet",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private  List<Transaction> receivedTransaction=new ArrayList<>();

}

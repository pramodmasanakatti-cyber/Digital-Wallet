package com.digitalwallet.entity;


import com.digitalwallet.validation.annotation.ValidWalletBalance;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Wallets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer walletId;

    @ManyToOne
    @JoinColumn(name = "User_id",nullable = false)
    private User user;

    @Column(nullable = false)
    @ValidWalletBalance
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletType walletType;

    @Column(nullable = false)
    private LocalDateTime createdAt;



    @OneToMany(mappedBy = "senderWallet",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Transaction> sentTransactions=new ArrayList<>();

    @OneToMany(mappedBy = "receiverWallet",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private  List<Transaction> receivedTransaction=new ArrayList<>();

}

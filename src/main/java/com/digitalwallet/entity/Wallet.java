package com.digitalwallet.entity;


import com.digitalwallet.entity.enums.WalletType;
import com.digitalwallet.exception.InsufficientFundException;
import com.digitalwallet.exception.InvalidAmountException;
import com.digitalwallet.validation.annotation.ValidWalletBalance;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Wallets")
@Data
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

    @Version   // optimistic locking for concurrent transactions
    private Long version;



    // Credit to wallet
    public void credit(BigDecimal amount) {
        this.balance=this.balance.add(amount);
    }

    //Debit from wallet
    public void debit(BigDecimal amount) {
        validateAmount(amount);
        if(this.balance.compareTo(amount)<0) {
            throw new InsufficientFundException("Insufficient balance: " + this.balance);
        }
        this.balance=this.balance.subtract(amount);
    }

    private void validateAmount(BigDecimal amount) {
        if(amount==null || amount.signum()<=0) {
            throw new InvalidAmountException("Invalid amount amount must be greater that 0 amount: " + amount);
        }
    }

}

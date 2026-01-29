package com.digitalwallet.entity;


import com.digitalwallet.entity.enums.WalletStatus;
import com.digitalwallet.entity.enums.WalletType;
import com.digitalwallet.validation.annotation.ValidWalletBalance;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Wallets")
@Data
@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer walletId;

   private Integer userId;

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

    @Enumerated(EnumType.STRING)
    private WalletStatus status;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}

package com.digitalwallet.service;

import com.digitalwallet.entity.User;
import com.digitalwallet.entity.Wallet;
import com.digitalwallet.entity.WalletType;
import com.digitalwallet.repository.UserRepository;
import com.digitalwallet.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {
private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;

    public Wallet createWallet(Integer userId,Wallet wallet) {
        User user= userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        wallet.setUser(user);
        try {
            wallet.setWalletType(wallet.getWalletType());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid wallet type. Allowed values: SAVINGS, CASH");
        }
        wallet.setCreatedAt(LocalDateTime.now());
        wallet.setBalance(wallet.getBalance()!=null?wallet.getBalance(): BigDecimal.ZERO);
        return walletRepository.save(wallet);
    }

    public List<Wallet> getWalletsByUser(Integer userId) {
        return walletRepository.findByUserUserId(userId);
    }

    public BigDecimal getWalletBalance(Integer walletId) {
        Wallet wallet=walletRepository.findById(walletId).orElseThrow(()->new RuntimeException("Wallet not exist"));
       return wallet.getBalance();
    }
}

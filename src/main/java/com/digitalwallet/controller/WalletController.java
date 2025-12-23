package com.digitalwallet.controller;

import com.digitalwallet.entity.Wallet;
import com.digitalwallet.service.WalletService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/user/{userId}")
    public Wallet createWallet(@PathVariable Integer userId, @RequestBody Wallet wallet) {

      return walletService.createWallet(userId,wallet);
    }

    @GetMapping("/user/{userId}")
    public List<Wallet> getWallets(@PathVariable Integer userId) {
        return walletService.getWalletsByUser(userId);
    }

    @GetMapping("/balance/{walletId}")
    public BigDecimal getWalletBalance(@PathVariable Integer walletId) {
        return walletService.getWalletBalance(walletId);
    }
}

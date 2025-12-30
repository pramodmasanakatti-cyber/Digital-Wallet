package com.digitalwallet.controller;

import com.digitalwallet.dto.reqsponse.WalletResponseDTO;
import com.digitalwallet.dto.request.WalletRequestDTO;
import com.digitalwallet.entity.Wallet;
import com.digitalwallet.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<WalletResponseDTO> createWallet(@RequestBody WalletRequestDTO walletDto) {

      return ResponseEntity.status(HttpStatus.OK).body(walletService.createWallet(walletDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletResponseDTO> getWallets(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(walletService.getWallet(id));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<String> getWalletBalance(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body("Balance: " + walletService.getWalletBalance(id));
    }
}

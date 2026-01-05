package com.digitalwallet.controller;

import com.digitalwallet.dto.response.WalletResponseDTO;
import com.digitalwallet.dto.request.WalletRequestDTO;
import com.digitalwallet.entity.Wallet;
import com.digitalwallet.service.WalletService;
import com.digitalwallet.validation.groups.Create;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<WalletResponseDTO> createWallet(@Valid @Validated(Create.class) @RequestBody WalletRequestDTO walletDto) {
      log.debug("Received wallet creation request: {}",walletDto);
        WalletResponseDTO wallet=walletService.createWallet(walletDto);
        log.info("Wallet created successfully: walletId={}",wallet.getWalletId());
      return ResponseEntity.status(HttpStatus.OK).body(wallet);
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

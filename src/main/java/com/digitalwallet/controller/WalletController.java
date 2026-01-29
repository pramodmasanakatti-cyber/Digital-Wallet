package com.digitalwallet.controller;

import com.digitalwallet.dto.response.WalletResponseDTO;
import com.digitalwallet.dto.request.WalletRequestDTO;

import com.digitalwallet.service.interfaces.WalletServiceForUser;
import com.digitalwallet.validation.groups.Create;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/wallets")
@Tag(name = "Wallet",description = "Wallet management APIs")
public class WalletController {
    private final WalletServiceForUser walletService;

    public WalletController(WalletServiceForUser walletService) {
        this.walletService = walletService;
    }

    @Operation(summary = "Create a new wallet",description = "Creates a new wallet")
    @ApiResponses({
            @ApiResponse(responseCode = "201",description = "Wallet created")
    })
    @PostMapping
    public ResponseEntity<WalletResponseDTO> createWallet(@Valid @Validated(Create.class) @RequestBody WalletRequestDTO walletDto) {
      log.debug("Received wallet creation request: {}",walletDto);
        WalletResponseDTO wallet=walletService.createWallet(walletDto);
        log.info("Wallet created successfully: walletId={}",wallet.getWalletId());
      return ResponseEntity.status(HttpStatus.CREATED).body(wallet);
    }

    @Operation(summary = "Get wallet",description = "Get a wallet with wallet Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Wallet details retrieved"),
            @ApiResponse(responseCode = "404",description = "Wallet not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<WalletResponseDTO> getWallets(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(walletService.getWallet(id));
    }

    @Operation(summary = "Get wallet balance",description = "Get a wallet balance with wallet Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Balance retrieved successfully"),
            @ApiResponse(responseCode = "404",description = "Wallet not found")
    })
    @GetMapping("/{id}/balance")
    public ResponseEntity<String> getWalletBalance(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body("Balance: " + walletService.getWalletBalance(id));
    }

    @Operation(summary = "Activate wallet",description = "Activate wallet with walletId")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Wallet activated successfully"),
            @ApiResponse(responseCode = "404",description = "Wallet not found")
    })
    @PostMapping("/{id}/activate")
    public ResponseEntity<String> activateWallet(@PathVariable Integer id) {
        walletService.activateWallet(id);
       return ResponseEntity.status(HttpStatus.OK).body("Wallet activated sucessfully walletId: " +id);
    }

    @Operation(summary = "Inactivate wallet",description = "Inactivate wallet with wallet Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Wallet inactivated successfully"),
            @ApiResponse(responseCode = "404",description = "Wallet not found")
    })
    @PostMapping("{id}/inactivate")
    public ResponseEntity<String> inactivateWallet(@PathVariable Integer id) {
        walletService.inactivateWallet(id);
        return ResponseEntity.status(HttpStatus.OK).body("Wallet inactivated successfully walletId: " + id);
    }



}

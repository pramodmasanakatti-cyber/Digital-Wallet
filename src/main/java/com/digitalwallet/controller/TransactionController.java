package com.digitalwallet.controller;

import com.digitalwallet.dto.request.TransactionRequestDTO;
import com.digitalwallet.dto.response.TransactionResponseDTO;
import com.digitalwallet.entity.Transaction;
import com.digitalwallet.entity.enums.TransactionStatus;
import com.digitalwallet.entity.enums.TransactionType;
import com.digitalwallet.service.interfaces.TransactionServiceForAdmin;
import com.digitalwallet.service.interfaces.TransactionServiceForUser;
import com.digitalwallet.validation.groups.Credit;
import com.digitalwallet.validation.groups.Debit;
import com.digitalwallet.validation.groups.Transfer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction",description = "Transaction management APIs")
public class TransactionController {

    public TransactionController(TransactionServiceForUser transactionServiceForUser, TransactionServiceForAdmin transactionServiceForAdmin) {
        this.transactionServiceForUser = transactionServiceForUser;
        this.transactionServiceForAdmin = transactionServiceForAdmin;
    }

    private final TransactionServiceForUser transactionServiceForUser;
    private final TransactionServiceForAdmin transactionServiceForAdmin;


    @Operation(summary = "Credit operation on wallet",description = "Crediting amount to a wallet")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Amount credited to wallet"),
            @ApiResponse(responseCode = "404",description = "Wallet not found"),
            @ApiResponse(responseCode = "403",description = "Wallet is inactive please activate it")
    })
    @PostMapping("/credit")
    public ResponseEntity<TransactionResponseDTO> credit(@Valid @Validated(Credit.class) @RequestBody TransactionRequestDTO transactionRequestDTO) {
        System.out.println(transactionRequestDTO);
        TransactionResponseDTO transactionResponseDTO= transactionServiceForUser.credit(transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDTO);
    }

    @Operation(summary = "Debit operation on wallet",description = "Debiting amount from a wallet")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Amount debited from wallet"),
            @ApiResponse(responseCode = "404",description = "Wallet not found"),
            @ApiResponse(responseCode = "403",description = "Wallet is inactive please activate it")
    })
    @PostMapping("/debit")
    public ResponseEntity<TransactionResponseDTO> debit(@Valid @Validated(Debit.class) @RequestBody TransactionRequestDTO transactionRequestDTO) {
        TransactionResponseDTO transactionResponseDTO= transactionServiceForUser.debit(transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDTO);
    }

    @Operation(summary = "Transfer operation",description = "Transfering amoutn between wallets")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Transfer successful"),
            @ApiResponse(responseCode = "404",description = "Wallet not found"),
            @ApiResponse(responseCode = "403",description = "Wallet is inactive please activate it")
    })
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(@Valid @Validated(Transfer.class) @RequestBody TransactionRequestDTO transactionRequestDTO) {
        TransactionResponseDTO transactionResponseDTO= transactionServiceForUser.transfer(transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDTO);
    }

    @Operation(summary = "Transaction history",description = "Transaction history for wallet")
    @ApiResponses({
            @ApiResponse(responseCode = "404",description = "Wallet not found"),
    })
    @GetMapping("/history")
    public Page<TransactionResponseDTO> getHistory(
            @RequestParam Integer walletId,
            @RequestParam(required = false)TransactionType type,
            @RequestParam(required = false)LocalDateTime from,
            @RequestParam(required = false)LocalDateTime to,
            @RequestParam(required = false)TransactionStatus status,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        return transactionServiceForUser.getTransactionHistory(walletId,type,from,to,status,page,size);
    }

    @Operation(summary = "Getting all transaction history",description = "Get all transaction (this is for ADMIN only)")
    @GetMapping("/all")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {

        return ResponseEntity.ok(transactionServiceForAdmin.getAllTransactions());
    }
}

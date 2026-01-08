package com.digitalwallet.controller;

import com.digitalwallet.dto.request.TransactionRequestDTO;
import com.digitalwallet.dto.response.TransactionResponseDTO;
import com.digitalwallet.entity.Transaction;
import com.digitalwallet.entity.enums.TransactionStatus;
import com.digitalwallet.entity.enums.TransactionType;
import com.digitalwallet.service.interfaces.TransactionService;
import com.digitalwallet.validation.groups.Credit;
import com.digitalwallet.validation.groups.Debit;
import com.digitalwallet.validation.groups.Transfer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    private final TransactionService transactionService;

    @PostMapping("/credit")
    public ResponseEntity<TransactionResponseDTO> credit(@Valid @Validated(Credit.class) @RequestBody TransactionRequestDTO transactionRequestDTO) {
        System.out.println(transactionRequestDTO);
        TransactionResponseDTO transactionResponseDTO=transactionService.credit(transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDTO);
    }

    @PostMapping("/debit")
    public ResponseEntity<TransactionResponseDTO> debit(@Valid @Validated(Debit.class) @RequestBody TransactionRequestDTO transactionRequestDTO) {
        TransactionResponseDTO transactionResponseDTO=transactionService.debit(transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDTO);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(@Valid @Validated(Transfer.class) @RequestBody TransactionRequestDTO transactionRequestDTO) {
        TransactionResponseDTO transactionResponseDTO=transactionService.transfer(transactionRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDTO);
    }

    @GetMapping("/history")
    public Page<Transaction> getHistory(
            @RequestParam Integer walletId,
            @RequestParam(required = false)TransactionType type,
            @RequestParam(required = false)LocalDateTime from,
            @RequestParam(required = false)LocalDateTime to,
            @RequestParam(required = false)TransactionStatus status,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        return transactionService.getTransactionHistory(walletId,type,from,to,status,page,size);
    }
}

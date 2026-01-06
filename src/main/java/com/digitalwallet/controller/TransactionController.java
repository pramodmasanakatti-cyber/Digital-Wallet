package com.digitalwallet.controller;

import com.digitalwallet.dto.request.TransactionRequestDTO;
import com.digitalwallet.dto.response.TransactionResponseDTO;
import com.digitalwallet.entity.Transaction;
import com.digitalwallet.service.TransactionService;
import com.digitalwallet.validation.groups.Create;
import com.digitalwallet.validation.groups.Credit;
import com.digitalwallet.validation.groups.Debit;
import com.digitalwallet.validation.groups.Transfer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

package com.digitalwallet.controller;

import com.digitalwallet.dto.request.TransactionRequestDTO;
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

    @PostMapping
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
     return ResponseEntity.status(HttpStatus.OK).body("Transaction successful");
    }
}

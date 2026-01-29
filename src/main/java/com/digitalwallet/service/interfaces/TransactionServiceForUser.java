package com.digitalwallet.service.interfaces;

import com.digitalwallet.dto.request.TransactionRequestDTO;
import com.digitalwallet.dto.response.TransactionResponseDTO;
import com.digitalwallet.entity.Transaction;
import com.digitalwallet.entity.enums.TransactionStatus;
import com.digitalwallet.entity.enums.TransactionType;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface TransactionServiceForUser {
    TransactionResponseDTO credit(TransactionRequestDTO transactionRequestDTO);
    TransactionResponseDTO debit(TransactionRequestDTO transactionRequestDTO);
    TransactionResponseDTO transfer(TransactionRequestDTO transactionRequestDTO);
    Page<TransactionResponseDTO> getTransactionHistory(Integer walletId,
                                            TransactionType type,
                                            LocalDateTime from,
                                            LocalDateTime to,
                                            TransactionStatus status,
                                            int page,
                                            int size);
}

package com.digitalwallet.service.interfaces;

import com.digitalwallet.dto.response.TransactionResponseDTO;

import java.util.List;

public interface TransactionServiceForAdmin extends TransactionServiceForUser{
    List<TransactionResponseDTO> getAllTransactions();
}

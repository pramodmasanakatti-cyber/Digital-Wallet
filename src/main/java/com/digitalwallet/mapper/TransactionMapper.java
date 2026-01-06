package com.digitalwallet.mapper;

import com.digitalwallet.dto.request.TransactionRequestDTO;
import com.digitalwallet.dto.response.TransactionResponseDTO;
import com.digitalwallet.entity.Transaction;
import com.digitalwallet.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


    @Mapper(componentModel = "spring")
    public interface TransactionMapper {
        @Mapping(source = "amount",target = "amount")
        Transaction toEntity(TransactionRequestDTO transactionRequestDTO);

        TransactionResponseDTO toDto(Transaction transaction);
    }

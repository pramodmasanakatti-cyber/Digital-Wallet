package com.digitalwallet.mapper;

import com.digitalwallet.dto.reqsponse.WalletResponseDTO;
import com.digitalwallet.dto.request.WalletRequestDTO;
import com.digitalwallet.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    Wallet toEntity(WalletRequestDTO dto);

    @Mapping(source = "user.userId",target = "userId")
    WalletResponseDTO toResponseDTO(Wallet wallet);
}

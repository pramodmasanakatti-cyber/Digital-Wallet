package com.digitalwallet.mapper;

import com.digitalwallet.dto.response.WalletResponseDTO;
import com.digitalwallet.dto.request.WalletRequestDTO;
import com.digitalwallet.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WalletMapper {
    @Mapping(source = "userId",target = "userId")
    @Mapping(source = "walletType",target = "walletType")
    @Mapping(source = "balance",target = "balance")
    @Mapping(source = "status",target="status")
    Wallet toEntity(WalletRequestDTO dto);


    @Mapping(source = "walletId",target = "walletId")
    @Mapping(source = "balance",target = "balance")
    @Mapping(source = "walletType",target = "walletType")
    @Mapping(source = "userId",target = "userId")
    @Mapping(source = "status",target = "status")
    WalletResponseDTO toResponseDTO(Wallet wallet);

}

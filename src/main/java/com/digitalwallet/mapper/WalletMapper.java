package com.digitalwallet.mapper;

import com.digitalwallet.dto.response.WalletResponseDTO;
import com.digitalwallet.dto.request.WalletRequestDTO;
import com.digitalwallet.entity.User;
import com.digitalwallet.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WalletMapper {
    @Mapping(source = "existingUser",target = "user")
    @Mapping(source = "dto.walletType",target = "walletType")
    @Mapping(source = "dto.balance",target = "balance")
    @Mapping(source = "dto.status",target="status")
    Wallet toEntity(WalletRequestDTO dto, User existingUser);


    @Mapping(source = "walletId",target = "walletId")
    @Mapping(source = "balance",target = "balance")
    @Mapping(source = "walletType",target = "walletType")
    @Mapping(source = "user.userId",target = "userId")
    @Mapping(source = "status",target = "status")
    WalletResponseDTO toResponseDTO(Wallet wallet);
}

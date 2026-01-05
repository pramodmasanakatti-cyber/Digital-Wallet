package com.digitalwallet.mapper;

import com.digitalwallet.dto.response.UserResponseDTO;
import com.digitalwallet.dto.request.UserRequestDTO;
import com.digitalwallet.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "fullName",target = "fullName")
    @Mapping(source = "email",target = "email")
    @Mapping(source = "phone",target = "phone")
    User toEntity(UserRequestDTO dto);

    @Mapping(source = "userId",target = "userId")
    @Mapping(source = "fullName",target = "fullName")
    UserResponseDTO toResponseDTO(User user);
}

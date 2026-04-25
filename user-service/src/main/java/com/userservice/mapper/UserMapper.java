package com.userservice.mapper;

import com.userservice.dto.response.UserResponseDTO;
import com.userservice.dto.request.UserRequestDTO;
import com.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "fullName",target = "fullName")
    @Mapping(source = "email",target = "email")
    @Mapping(source = "phone",target = "phone")
    @Mapping(source = "password",target="password")
    User toEntity(UserRequestDTO dto);

    @Mapping(source = "userId",target = "userId")
    @Mapping(source = "fullName",target = "fullName")
    UserResponseDTO toResponseDTO(User user);
}

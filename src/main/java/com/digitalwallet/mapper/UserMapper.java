package com.digitalwallet.mapper;

import com.digitalwallet.dto.reqsponse.UserResponseDTO;
import com.digitalwallet.dto.request.UserRequestDTO;
import com.digitalwallet.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDTO dto);
    UserResponseDTO toResponseDTO(User user);
}

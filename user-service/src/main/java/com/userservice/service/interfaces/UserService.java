package com.userservice.service.interfaces;

import com.userservice.dto.request.UserRequestDTO;
import com.userservice.dto.response.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO getUser(Integer userId);
}

package com.digitalwallet.service.interfaces;

import com.digitalwallet.dto.request.UserRequestDTO;
import com.digitalwallet.dto.response.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO getUser(Integer userId);
}

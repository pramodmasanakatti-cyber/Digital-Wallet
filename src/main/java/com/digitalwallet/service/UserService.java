package com.digitalwallet.service;

import com.digitalwallet.dto.reqsponse.UserResponseDTO;
import com.digitalwallet.dto.request.UserRequestDTO;
import com.digitalwallet.entity.User;
import com.digitalwallet.exception.DuplicateEmailException;
import com.digitalwallet.exception.UserNotFoundException;
import com.digitalwallet.mapper.UserMapper;
import com.digitalwallet.repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    private final UserMapper userMapper;

    public UserResponseDTO createUser(UserRequestDTO dto) {
      boolean userExist=userRepository.findByEmail(dto.getEmail()).isPresent();
      if (userExist) {
          throw new DuplicateEmailException("User already exist with this email: " + dto.getEmail());
      }
        User user=userMapper.toEntity(dto);
        user=userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO getUser(Integer userId) {
     User user=userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User Not found with id: " + userId));
     return userMapper.toResponseDTO(user);
    }

}

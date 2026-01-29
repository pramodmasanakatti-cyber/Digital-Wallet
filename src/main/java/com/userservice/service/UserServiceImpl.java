package com.userservice.service;

import com.userservice.dto.response.UserResponseDTO;
import com.userservice.dto.request.UserRequestDTO;
import com.userservice.entity.Role;
import com.userservice.entity.User;
import com.userservice.exception.DuplicateEmailException;
import com.userservice.exception.UserNotFoundException;
import com.userservice.mapper.UserMapper;
import com.userservice.repository.UserRepository;
import com.userservice.service.interfaces.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.PasswordAuthentication;

@Slf4j
@Service
@Data
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO createUser(UserRequestDTO dto) {
        log.debug("Checking user already exist with email: {}",dto.getEmail());
        boolean userExist=userRepository.findByEmail(dto.getEmail()).isPresent();
        if (userExist) {
            throw new DuplicateEmailException("User already exist with this email: " + dto.getEmail());
        }
        log.debug("Mapping UserDTO to User entity");
        User user=userMapper.toEntity(dto);

        // encoding password
        user.setPassword(passwordEncoder.encode(dto.getPassword()));


        log.debug("Saving User entity");
        user=userRepository.save(user);
        log.info("User saved successfully");
        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO getUser(Integer userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User Not found with id: " + userId));
        return userMapper.toResponseDTO(user);
    }

}

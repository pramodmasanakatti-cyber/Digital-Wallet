package com.digitalwallet.controller;

import com.digitalwallet.dto.response.UserResponseDTO;
import com.digitalwallet.dto.request.UserRequestDTO;
import com.digitalwallet.service.UserService;
import com.digitalwallet.validation.groups.Create;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @Validated(Create.class) @RequestBody UserRequestDTO userDto) {
        log.debug("Received user registration request: {}",userDto);
        UserResponseDTO user=userService.createUser(userDto);
        log.debug("User created successfully with userId= {}",user.getUserId());
       return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    @GetMapping("{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(id));
    }
}

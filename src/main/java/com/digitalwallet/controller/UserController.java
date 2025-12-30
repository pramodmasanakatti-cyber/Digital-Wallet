package com.digitalwallet.controller;

import com.digitalwallet.dto.reqsponse.UserResponseDTO;
import com.digitalwallet.dto.request.UserRequestDTO;
import com.digitalwallet.repository.UserRepository;
import com.digitalwallet.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO userDto) {
       return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }
    @GetMapping("{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(id));
    }
}

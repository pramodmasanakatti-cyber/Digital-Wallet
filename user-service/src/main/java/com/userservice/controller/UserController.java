package com.userservice.controller;
import com.userservice.dto.response.UserResponseDTO;
import com.userservice.dto.request.UserRequestDTO;
import com.userservice.service.interfaces.UserService;
import com.userservice.validation.groups.Create;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users",description = "User management APIs")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user",description = "Creates a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201",description = "User created"),
            @ApiResponse(responseCode = "409",description = "User with email already exist")
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @Validated(Create.class) @RequestBody UserRequestDTO userDto) {
        log.debug("Received user registration request: {}",userDto);
        UserResponseDTO user=userService.createUser(userDto);
        log.debug("User created successfully with userId= {}",user.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(summary = "Get user",description = "Get a user with user id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "User details retrived"),
            @ApiResponse(responseCode = "404",description = "User not found")
    })
    @GetMapping("{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(id));
    }


    @Hidden
    @GetMapping("/internal/{id}")
    public ResponseEntity<UserResponseDTO> getUserForWalleService(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(id));
    }
}

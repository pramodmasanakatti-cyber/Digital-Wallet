package com.userservice.controller;

import com.userservice.dto.request.LoginRequest;
import com.userservice.dto.response.JwtResponse;
import com.userservice.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth",description = "Authentication management APIs")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Login",description="Login with userName and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Login successfull and will recieve JWT token")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid  @RequestBody LoginRequest loginRequest) {
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
        );
        UserDetails userDetails=(UserDetails) authentication.getPrincipal();
        String jwtToken=jwtUtil.generateToken(userDetails);
        JwtResponse jwtResponse=new JwtResponse(jwtToken);
        return ResponseEntity.ok(jwtResponse);
    }
 }

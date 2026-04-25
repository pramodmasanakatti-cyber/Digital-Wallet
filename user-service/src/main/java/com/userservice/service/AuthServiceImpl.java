package com.userservice.service;

import com.userservice.dto.request.LoginRequest;
import com.userservice.dto.response.JwtResponse;
import com.userservice.security.JwtUtil;
import com.userservice.service.interfaces.AuthService;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Data
public class AuthServiceImpl implements AuthService {
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse login(LoginRequest loginRequest) {

        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
        );
        UserDetails userDetails=(UserDetails) authentication.getPrincipal();
        String jwtToken=jwtUtil.generateToken(userDetails);
        JwtResponse jwtResponse=new JwtResponse(jwtToken);
        return jwtResponse;
    }
}

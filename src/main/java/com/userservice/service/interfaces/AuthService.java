package com.userservice.service.interfaces;

import com.userservice.dto.request.LoginRequest;
import com.userservice.dto.response.JwtResponse;

public interface AuthService {
    public JwtResponse login(LoginRequest loginRequest);
}

package com.digitalwallet.security.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        response.getWriter().write(
                """
                      {
                      "status",401,
                      "error":"UNAUTHORIZED",
                      "message":"%s",
                      "path":"%s",
                      "timestamp":"%s"
                      }  """.formatted(
                              authException.getMessage(),
                        request.getRequestURI(),
                        Instant.now()
                )

        );
    }
}

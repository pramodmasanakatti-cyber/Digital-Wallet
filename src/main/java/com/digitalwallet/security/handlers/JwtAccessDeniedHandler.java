package com.digitalwallet.security.handlers;

import jakarta.persistence.Access;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        response.getWriter().write("""
               {
               "status":403,
               "error":"FORBIDDEN",
               "message":"%s",
               "path":"%s",
               "timestamp":"%s"
               } 
           """.formatted(
                   accessDeniedException.getMessage(),
                request.getRequestURI(),
                Instant.now()
        ));
    }
}

package com.digitalwallet.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final String SECRET;

    public JwtAuthenticationFilter( @Value("${jwt.secret}")String secret) {
        SECRET = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
     String authHeader=request.getHeader("Authorization");

     if(authHeader!=null && authHeader.startsWith("Bearer ")) {
        String token=authHeader.substring(7);

        try {
            Claims claims= Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String userId=claims.getSubject();

            // Create an authentication object
            UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(userId,null, Collections.emptyList());

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set authentication in context
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch(Exception exception) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid token");
            return;
        }
     } else {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Missing token");
      return;
     }
     filterChain.doFilter(request,response);
    }
}

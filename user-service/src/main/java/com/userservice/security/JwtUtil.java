package com.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final String SECRET;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        SECRET = secret;
    }

    // Generate token
    public String generateToken(UserDetails userDetails) {
        List<String> roles=userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role->role.startsWith("ROLE_")?role.substring(5):role)
                .toList();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60)) // 1 hour
                .claim("roles",roles)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate token
    public boolean validateToken(String token,UserDetails userDetails) {
     return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpeired(token);
    }

    private boolean isTokenExpeired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public String  extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        Claims claims=null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException exception) {
            throw exception;
        } catch (io.jsonwebtoken.security.SignatureException exception) {
            throw exception;
        } catch (Exception exception) {
            throw exception;
        }
        return claims;
    }
}

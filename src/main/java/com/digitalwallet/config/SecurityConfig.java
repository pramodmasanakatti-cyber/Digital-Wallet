package com.digitalwallet.config;

import com.digitalwallet.security.JwtAuthenticationFilter;
import com.digitalwallet.security.handlers.JwtAccessDeniedHandler;
import com.digitalwallet.security.handlers.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

private final JwtAuthenticationFilter jwtFilter;
private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPointl;
private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    public SecurityConfig(JwtAuthenticationFilter jwtFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPointl, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.jwtFilter = jwtFilter;
        this.jwtAuthenticationEntryPointl = jwtAuthenticationEntryPointl;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return  http.csrf(csrf -> csrf.disable())
                .exceptionHandling(ex->ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPointl)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger").permitAll()
                        .requestMatchers("/api/transactions/all").hasRole("ADMIN") // ROLE_ADMIN
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

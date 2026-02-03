package com.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(
                16,  // (16 bytes) Salt length (random salt)
                32,            // (32 bytes) Hash length (Length of final hash output)
                1,             // Parallelism (Number of CPU threads used per hash)
                10000,         // Memory (10 MB) (Amount of memory uses per hash operation)
                3              //  Iterations (Number of times the hashing process is repeated)
        );
    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}

package com.userservice.controller;

import com.userservice.entity.Role;
import com.userservice.entity.User;
import com.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Container
    static MySQLContainer<?> mySql=
            new MySQLContainer<>("mysql:8.4")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",mySql::getJdbcUrl);
        registry.add("spring.datasource.username",mySql::getUsername);
        registry.add("spring.datasource.password",mySql::getPassword);
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;


    @Autowired
     PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        User user=new User();
        user.setRole(Role.USER);
        user.setEmail("pramod@gmail.com");
        user.setPassword(passwordEncoder.encode("password@123"));
        user.setFullName("Pramod Masanakatti");
        user.setAge(20);
        user.setPhone("12345678909");
        userRepository.save(user);
    }

    @Test
    public void loginTestSuccess() throws Exception {
        String payLoad="""
                {
                "email":"pramod@gmail.com",
                "password":"password@123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoad))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

    }

    @Test
    public void loginTestFail() throws Exception{
        String payLoad="""
                {
                "email":"pramod@gmail.com",
                "password":"123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payLoad))
                .andExpect(status().isUnauthorized());

    }
}
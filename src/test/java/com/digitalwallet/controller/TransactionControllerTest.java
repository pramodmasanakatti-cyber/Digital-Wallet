package com.digitalwallet.controller;

import com.digitalwallet.entity.Wallet;
import com.digitalwallet.entity.enums.WalletStatus;
import com.digitalwallet.entity.enums.WalletType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class TransactionControllerTest {

    @Container
    static MySQLContainer<?> mySql=
            new MySQLContainer<>("mysql:8.0.34")
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
    RestTemplate restTemplate;

    @Value("${user-service.base-url}")
    private String userServiceBaseUrl;

    private String token;

    @BeforeEach
    void setUp() throws IOException {
        String payload= """
                {
                "email":"pramod@gmail.com",
                "password":"password@123"
                }""";
        ResponseEntity<String> response=restTemplate.postForEntity(
                userServiceBaseUrl+"/api/auth/login",
                payload,
                String.class
        );
        // Parse the esponse JSON
        ObjectMapper mapper=new ObjectMapper();
        JsonNode root= mapper.readTree(response.getBody());
        token=root.get("token").asText();
    }

    @Test
    void creditSuccess() throws Exception {
        String payLoad= """
                "externalTxId":"dfgh52ghhjnd672gjhw",
                "walletId":"1",
                "amount":"1000"
                """;
        mockMvc.perform(post("/api/transaction/credit")
                .header("Authorization","Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payLoad))
                .andExpect(status().isOk());
    }

    @Test
    void debit() throws Exception{
        String payLoad= """
                "externalTxId":"dfgh52ghhjnd672gjhw",
                "walletId":"1",
                "amount":"1000"
                """;
        mockMvc.perform(post("/api/transaction/debit")
                        .header("Authorization","Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payLoad))
                .andExpect(status().isOk());
    }

    @Test
    void transfer() throws Exception{
        String payLoad= """
                "externalTxId":"dfgh52ghhjnd672gjhw",
                "senderWalletId":"1",
                "receiverWalletId":"2",
                "amount":"1000"
                """;
        mockMvc.perform(post("/api/transaction/credit")
                        .header("Authorization","Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payLoad))
                .andExpect(status().isOk());
    }

    @Test
    void getHistory() throws Exception{
        mockMvc.perform(get("/api/transaction/history?status=COMPLETED&walletId=1")
                        .header("Authorization","Bearer "+token))
                .andExpect(status().isOk());
    }

    @Test
    void getAllTransactions() throws Exception{
        mockMvc.perform(get("/api/transaction/all")
                        .header("Authorization","Bearer "+token))
                .andExpect(status().isOk());
    }
}
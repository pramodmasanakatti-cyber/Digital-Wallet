package com.digitalwallet.controller;

import com.digitalwallet.entity.Wallet;
import com.digitalwallet.entity.enums.WalletStatus;
import com.digitalwallet.entity.enums.WalletType;
import com.digitalwallet.repository.WalletRepository;
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

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class WalletControllerTest {

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
    WalletRepository walletRepository;

    @Autowired
    RestTemplate restTemplate;

    @Value("${user-service.base-url}")
    private String userServiceBaseUrl;

    private String token;

    private Wallet existingWallet;
    @BeforeEach
    void setUp() throws IOException {
        Wallet wallet=new Wallet();
        wallet.setBalance(new BigDecimal("1000"));
        wallet.setStatus(WalletStatus.ACTIVE);
        wallet.setWalletType(WalletType.SAVINGS);
        wallet.setUserId(1);
       existingWallet=walletRepository.save(wallet);

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
    void createWalletSuccess() throws Exception{

        String payLoad= """
                {
                "userId":"1",
                "walletType":"SAVINGS",
                "balance":"1000",
                "status":"ACTIVE"
                }""";
        mockMvc.perform(post("/api/users")
                        .header("Authorization","Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payLoad))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").exists());


    }

    @Test
    void getWalletsSuccess() throws Exception {
        String uri="/api/wallets/"+existingWallet.getWalletId();
        mockMvc.perform(get(uri)
                .header("Authoriztion","Bearer "+token))
                .andExpect(status().isOk());
    }
    @Test
    void getWalletsFail() throws Exception {
        String uri="/api/wallets/"+1000;
        mockMvc.perform(get(uri)
                        .header("Authoriztion","Bearer "+token))
                .andExpect(status().isNotFound());
    }

    @Test
    void getWalletBalance() throws Exception {
      String uri="/api/wallets/"+existingWallet.getWalletId()+"/balance";
      mockMvc.perform(get(uri)
              .header("Authorization","Bearer "+token))
              .andExpect(status().isOk());
    }

    @Test
    void activateWallet() throws Exception{
        String uri="/api/wallets/"+existingWallet.getWalletId()+"/activate";
        mockMvc.perform(post(uri)
                .header("Authorization","Bearer "+token))
                .andExpect(status().isOk());
    }

    @Test
    void inactivateWallet() throws Exception {
        String uri="/api/wallets/"+existingWallet.getWalletId()+"/inactivate";
        mockMvc.perform(post(uri)
                        .header("Authorization","Bearer "+token))
                .andExpect(status().isOk());
    }
}
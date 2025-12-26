package com.digitalwallet.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Test
    public void testFindByEmail() {
        assertEquals(2,userRepository.findByEmail("bob@example.com").orElseThrow(()->new RuntimeException("User not found")).getUserId());
    }

    @Test
    public void testFIndFullNameContaining() {
        assertEquals(2,userRepository.findByFullNameContaining("Bob").size());
    }

    @Test
    public void getTotalBalanceByUser() {
        assertEquals(new BigDecimal(6300.00), userRepository.getTotalBalanceByUser(1).setScale(0,RoundingMode.DOWN));
    }
 }
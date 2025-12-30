package com.digitalwallet.repository;

import com.digitalwallet.entity.User;
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
    public void testFindFullNameContaining() {
        assertEquals(2,userRepository.findByFullNameContaining("Bob").size());
    }

    @Test
    public void testgetTotalBalanceByUser() {
        assertEquals(new BigDecimal(6300.00), userRepository.getTotalBalanceByUser(1).setScale(0,RoundingMode.DOWN));
    }

    @Test
    public void testSave() {
        User user=new User();
        user.setFullName("Pramod");
        user.setPhone("123");
        user.setEmail("pramod@gmail.com");
        assertNotNull(userRepository.save(user));
    }

    @Test
    public void testFindById() {
        assertEquals("Alice Johnson",userRepository.findById(1).orElseThrow(()->new RuntimeException("USer not found")).getFullName());
    }

    @Test
    public void testFindAll() {
        assertEquals(11,userRepository.findAll().size());
    }

 }
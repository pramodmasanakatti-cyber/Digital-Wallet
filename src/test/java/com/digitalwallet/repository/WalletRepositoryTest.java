package com.digitalwallet.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class WalletRepositoryTest {
    @Autowired
    private WalletRepository walletRepository;
@Test
    public void testFindByUserId() {
    assertEquals(8,walletRepository.findByUserId(1).size());
}
@Test
    public void testFindByBalanceGreaterThan() {
    assertEquals(15,walletRepository.findByBalanceGreaterThan(BigDecimal.valueOf(100)).size());
}
}
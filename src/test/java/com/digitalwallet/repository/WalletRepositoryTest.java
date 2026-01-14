package com.digitalwallet.repository;


import com.digitalwallet.entity.Wallet;
import com.digitalwallet.entity.enums.WalletType;
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
    public void testSave() {
    Wallet wallet=new Wallet();

    wallet.setWalletType(WalletType.SAVINGS);
    wallet.setBalance(new BigDecimal(10));
    wallet.setUserId(1);
    walletRepository.save(wallet);
    assertNotNull(wallet.getWalletId());
}


@Test
    public void testFindById() {
    assertEquals(1,walletRepository.findById(1).orElseThrow(()->new RuntimeException("Wallet not found")).getUserId());
}

}
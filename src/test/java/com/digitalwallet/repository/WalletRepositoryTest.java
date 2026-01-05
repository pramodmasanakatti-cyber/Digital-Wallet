package com.digitalwallet.repository;

import com.digitalwallet.entity.User;
import com.digitalwallet.entity.Wallet;
import com.digitalwallet.entity.WalletType;
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
    assertEquals(18,walletRepository.findByUserId(1).size());
}
@Test
    public void testFindByBalance() {
    assertEquals(8,walletRepository.findByBalance(BigDecimal.valueOf(100)).size());
}

@Test
    public void testSave() {
    Wallet wallet=new Wallet();
    User user=new User();
    user.setUserId(11);
    user.setEmail("pramod@gmail.com");
    user.setPhone("12341234");
    user.setFullName("Pramod");
    wallet.setWalletType(WalletType.SAVINGS);
    wallet.setBalance(new BigDecimal(10));
    wallet.setUser(user);
    walletRepository.save(wallet);
    assertNotNull(wallet.getWalletId());
}

@Test
    public void testFindAll() {
    assertEquals(34,walletRepository.findAll().size());
}
@Test
    public void testFindById() {
    assertEquals(1,walletRepository.findById(1).orElseThrow(()->new RuntimeException("Wallet not found")).getUser().getUserId());
}

}
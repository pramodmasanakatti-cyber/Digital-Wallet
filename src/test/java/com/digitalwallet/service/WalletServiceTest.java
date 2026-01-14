package com.digitalwallet.service;


import com.digitalwallet.entity.Wallet;
import com.digitalwallet.entity.enums.WalletStatus;
import com.digitalwallet.entity.enums.WalletType;
import com.digitalwallet.exception.WalletNotFoundException;
import com.digitalwallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet=new Wallet();
        wallet.setWalletType(WalletType.SAVINGS);
        wallet.setWalletId(1);
        wallet.setBalance(new BigDecimal("1000"));
        wallet.setStatus(WalletStatus.ACTIVE);
        wallet.setUserId(1);
        wallet.setCreatedAt(LocalDateTime.now());
    }
    @Test
    public void testGetWalletByIdForSUCCESS() {
        Mockito.when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
      Wallet wallet1=walletService.getWalletById(1);
     assertEquals(new BigDecimal("1000"),wallet1.getBalance());
     assertEquals(1,wallet1.getWalletId());
    }

    @Test
    public void testGetWalletByIdForNotFound() {
        Mockito.when(walletRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class,()->walletService.getWalletById(2));
    }
}

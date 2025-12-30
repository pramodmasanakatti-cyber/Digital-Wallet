package com.digitalwallet.service;

import com.digitalwallet.dto.reqsponse.WalletResponseDTO;
import com.digitalwallet.dto.request.WalletRequestDTO;
import com.digitalwallet.entity.User;
import com.digitalwallet.entity.Wallet;
import com.digitalwallet.exception.UserNotFoundException;
import com.digitalwallet.exception.WalletNotFoundException;
import com.digitalwallet.mapper.WalletMapper;
import com.digitalwallet.repository.UserRepository;
import com.digitalwallet.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletService {
private final WalletRepository walletRepository;
private final WalletMapper walletMapper;
    public WalletService(WalletRepository walletRepository, WalletMapper walletMapper, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;

    public WalletResponseDTO createWallet(WalletRequestDTO walletDto) {
        User user=userRepository.findById(walletDto.getUserId()).orElseThrow(()-> new UserNotFoundException("User not found with the id: " + walletDto.getUserId()));
        Wallet wallet=new Wallet();
        wallet.setWalletType(walletDto.getWalletType());
        wallet.setUser(user);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCreatedAt(LocalDateTime.now());

        Wallet saved=walletRepository.save(wallet);
        return walletMapper.toResponseDTO(saved);
    }

    public WalletResponseDTO getWallet(Integer walletId) {
        Wallet wallet=walletRepository.findById(walletId).orElseThrow(()-> new WalletNotFoundException("Wallet not found with id: " + walletId));
        return walletMapper.toResponseDTO(wallet);
    }
    public List<Wallet> getWalletsByUser(Integer userId) {
        return walletRepository.findByUserId(userId);
    }

    public BigDecimal getWalletBalance(Integer walletId) {
        Wallet wallet=walletRepository.findById(walletId).orElseThrow(()->new WalletNotFoundException("Wallet not found with id: " + walletId));
       return wallet.getBalance();
    }
}

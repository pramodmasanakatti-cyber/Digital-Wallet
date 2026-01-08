package com.digitalwallet.service;

import com.digitalwallet.dto.response.WalletResponseDTO;
import com.digitalwallet.dto.request.WalletRequestDTO;
import com.digitalwallet.entity.User;
import com.digitalwallet.entity.Wallet;
import com.digitalwallet.entity.enums.WalletStatus;
import com.digitalwallet.exception.*;
import com.digitalwallet.mapper.WalletMapper;
import com.digitalwallet.repository.UserRepository;
import com.digitalwallet.repository.WalletRepository;
import com.digitalwallet.service.interfaces.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class WalletServiceImplementation implements WalletService {
private final WalletRepository walletRepository;
private final WalletMapper walletMapper;

    public WalletServiceImplementation(WalletRepository walletRepository, WalletMapper walletMapper, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;

    public WalletResponseDTO createWallet(WalletRequestDTO walletDto) {

        User user=userRepository.findById(walletDto.getUserId()).orElseThrow(()-> new UserNotFoundException("User not found with the id: " + walletDto.getUserId()));
        log.debug("Mapping WalletRequestDTO to Wallet entity: {}",walletDto);
        Wallet wallet=walletMapper.toEntity(walletDto,user);
        log.debug("Saving Wallet entity: {}",wallet);
        Wallet saved=walletRepository.save(wallet);
        log.info("Wallet saved successfully with wallletId={}",saved.getWalletId());
        return walletMapper.toResponseDTO(saved);
    }
    public Wallet getWalletById(Integer walletId) {
        return walletRepository.findById(walletId).orElseThrow(()->new WalletNotFoundException("Wallet not found for walletId: " + walletId));
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

    // Credit to wallet
    public void credit(Wallet wallet,BigDecimal amount) {
        validateWalletStatus(wallet);
        validateAmount(amount);
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }

    //Debit from wallet
    public void debit(Wallet wallet,BigDecimal amount) {
        validateAmount(amount);
        validateWalletStatus(wallet);
        validateBalance(wallet,amount);
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
    }

    @Override
    public void activateWallet(Integer id) {
        Wallet wallet=walletRepository.findById(id).orElseThrow(()->new WalletNotFoundException("Wallet not found for walletId: " +id));

        wallet.setStatus(WalletStatus.ACTIVE);
        walletRepository.save(wallet);
    }

    @Override
    public void inactivateWallet(Integer id) {
     Wallet wallet=walletRepository.findById(id).orElseThrow(()->new WalletNotFoundException("Wallet not found for walletId: " + id));
     wallet.setStatus(WalletStatus.INACTIVE);
     walletRepository.save(wallet);
    }

    private void validateWalletStatus(Wallet wallet) {
        if(wallet.getStatus()==WalletStatus.INACTIVE) {
            throw new WalletInactiveException("Your wallet is inactive please activate it");
        }
    }


    private void validateBalance(Wallet wallet, BigDecimal amount) {
        if(wallet.getBalance().compareTo(amount)<0) {
            throw new InsufficientFundException("Insufficient funds balance : " + wallet.getBalance());
        }
    }

    private void validateAmount(BigDecimal amount) {
        if(amount==null || amount.signum()<=0) {
            throw new InvalidAmountException("Invalid amount amount must be greater that 0 amount: " + amount);
        }
    }


}

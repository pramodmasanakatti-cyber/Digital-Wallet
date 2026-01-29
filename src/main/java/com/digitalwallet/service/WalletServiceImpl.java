package com.digitalwallet.service;

import com.digitalwallet.client.UserClient;
import com.digitalwallet.dto.response.WalletResponseDTO;
import com.digitalwallet.dto.request.WalletRequestDTO;
import com.digitalwallet.entity.Wallet;
import com.digitalwallet.entity.enums.WalletStatus;
import com.digitalwallet.exception.transaction.InsufficientFundException;
import com.digitalwallet.exception.InvalidAmountException;
import com.digitalwallet.exception.wallet.WalletInactiveException;
import com.digitalwallet.exception.wallet.WalletNotFoundException;
import com.digitalwallet.mapper.WalletMapper;
import com.digitalwallet.repository.WalletRepository;
import com.digitalwallet.service.interfaces.WalletServiceForTransaction;
import com.digitalwallet.service.interfaces.WalletServiceForUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class WalletServiceImpl implements WalletServiceForUser, WalletServiceForTransaction {
private final WalletRepository walletRepository;
private final WalletMapper walletMapper;
private final UserClient userClient;

    // Creating wallet
    public WalletResponseDTO createWallet(WalletRequestDTO walletDto) {
        log.debug("Calling user service to verify user");

        userClient.validateUser(walletDto.getUserId());

        log.debug("Mapping WalletRequestDTO to Wallet entity: {}",walletDto);
        Wallet wallet=walletMapper.toEntity(walletDto);


        log.debug("Saving Wallet entity: {}",wallet);
        Wallet saved=walletRepository.save(wallet);


        log.info("Wallet saved successfully with wallletId={}",saved.getWalletId());
        return walletMapper.toResponseDTO(saved);
    }

    // Getting wallet by walletId
    public Wallet getWalletById(Integer walletId) {
        return walletRepository.findById(walletId).orElseThrow(()->new WalletNotFoundException("Wallet not found for walletId: " + walletId));
    }

    // Getting wallet by walletId for End user
    public WalletResponseDTO getWallet(Integer walletId) {
        Wallet wallet=walletRepository.findById(walletId).orElseThrow(()-> new WalletNotFoundException("Wallet not found with id: " + walletId));
        return walletMapper.toResponseDTO(wallet);
    }

    public List<Wallet> getWalletsByUser(Integer userId) {
        return walletRepository.findByUserId(userId);
    }

    // Retriving wallet balance
    public BigDecimal getWalletBalance(Integer walletId) {
        Wallet wallet=walletRepository.findById(walletId).orElseThrow(()->new WalletNotFoundException("Wallet not found with id: " + walletId));
        return wallet.getBalance();
    }

    // Credit to wallet
    public void credit(Integer walletId,BigDecimal amount) {
        // Get wallet from walletId
        Wallet wallet=walletRepository.findById(walletId).orElseThrow(()->new WalletNotFoundException("Wallet not found for walletId: " + walletId));
        validateWalletStatus(wallet);
        validateAmount(amount);
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }


    //Debit from wallet
    public void debit(Integer walletId,BigDecimal amount) {
        // Get wallet from walletId
        Wallet wallet=walletRepository.findById(walletId).orElseThrow(()->new WalletNotFoundException("Wallet not found for walletId: " + walletId));
        validateAmount(amount);
        validateWalletStatus(wallet);
        validateBalance(wallet,amount);
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
    }

    // Check wallet exist or not
    @Override
    public void checkWallet(Integer walletId) {
        walletRepository.findById(walletId).orElseThrow(()->new WalletNotFoundException("Wallet not found for walletId: " + walletId));
    }


    // Activate wallet
    @Override
    public void activateWallet(Integer id) {
        Wallet wallet=walletRepository.findById(id).orElseThrow(()->new WalletNotFoundException("Wallet not found for walletId: " +id));

        wallet.setStatus(WalletStatus.ACTIVE);
        walletRepository.save(wallet);
    }

    // Inactivate wallet
    @Override
    public void inactivateWallet(Integer id) {
     Wallet wallet=walletRepository.findById(id).orElseThrow(()->new WalletNotFoundException("Wallet not found for walletId: " + id));
     wallet.setStatus(WalletStatus.INACTIVE);
     walletRepository.save(wallet);
    }


    // Validate wallet status
    private void validateWalletStatus(Wallet wallet) {
        if(wallet.getStatus()==WalletStatus.INACTIVE) {
            throw new WalletInactiveException("Your wallet is inactive please activate it");
        }
    }

    // Validate wallet balance before debiting money
    private void validateBalance(Wallet wallet, BigDecimal amount) {
        if(wallet.getBalance().compareTo(amount)<0) {
            throw new InsufficientFundException("Insufficient funds balance : " + wallet.getBalance());
        }
    }

    // Validate amount
    private void validateAmount(BigDecimal amount) {
        if(amount==null || amount.signum()<=0) {
            throw new InvalidAmountException("Invalid amount amount must be greater that 0 amount: " + amount);
        }
    }


}

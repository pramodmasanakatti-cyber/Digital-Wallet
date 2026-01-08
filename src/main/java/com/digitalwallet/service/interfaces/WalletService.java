package com.digitalwallet.service.interfaces;

import com.digitalwallet.dto.request.WalletRequestDTO;
import com.digitalwallet.dto.response.WalletResponseDTO;
import com.digitalwallet.entity.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {
    WalletResponseDTO createWallet(WalletRequestDTO walletRequestDTO);
    Wallet getWalletById(Integer walletId);
    WalletResponseDTO getWallet(Integer walletId);
    List<Wallet> getWalletsByUser(Integer userId);
    BigDecimal getWalletBalance(Integer walletId);
    void credit(Wallet wallet,BigDecimal amount);
    void debit(Wallet wallet,BigDecimal amount);
    void activateWallet(Integer id);
    void inactivateWallet(Integer id);


}

package com.digitalwallet.service.interfaces;

import com.digitalwallet.dto.request.WalletRequestDTO;
import com.digitalwallet.dto.response.WalletResponseDTO;
import com.digitalwallet.entity.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface WalletServiceForUser {
    WalletResponseDTO createWallet(WalletRequestDTO walletRequestDTO);
    Wallet getWalletById(Integer walletId);
    WalletResponseDTO getWallet(Integer walletId);
    List<Wallet> getWalletsByUser(Integer userId);
    BigDecimal getWalletBalance(Integer walletId);
    void activateWallet(Integer id);
    void inactivateWallet(Integer id);
}

package com.digitalwallet.service;

import com.digitalwallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletDeleteService {

    @Autowired
    private WalletRepository walletRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteWallets() {
        walletRepository.deleteAll();
    }
}

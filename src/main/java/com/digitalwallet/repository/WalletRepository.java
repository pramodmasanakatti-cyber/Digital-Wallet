package com.digitalwallet.repository;

import com.digitalwallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Integer> {
    List<Wallet> findByUserUserId(Integer userId);
}

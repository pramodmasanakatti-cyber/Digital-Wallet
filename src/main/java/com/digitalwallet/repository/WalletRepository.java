package com.digitalwallet.repository;

import com.digitalwallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Integer> {
    @Query("SELECT w FROM Wallet w WHERE w.user.userId= :userId")
    List<Wallet> findByUserId(@Param("userId") Integer userId);
    List<Wallet> findByBalanceGreaterThan(BigDecimal amount);
}

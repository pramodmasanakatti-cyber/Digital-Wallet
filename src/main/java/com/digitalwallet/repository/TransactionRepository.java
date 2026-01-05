package com.digitalwallet.repository;

import com.digitalwallet.entity.Transaction;
import com.digitalwallet.entity.TransactionStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(value = "SELECT * FROM Transactions WHERE Sender_wallet_id= :walletId OR Receiver_wallet_id= :walletId",nativeQuery = true)
    List<Transaction> findAllByWalletId(@Param("walletId") Integer walletId);

    //find transactions with amount (native query)
    @Query(value = "SELECT * FROM Transactions t WHERE t.Amount= :amount",nativeQuery = true)
    List<Transaction> findTransactionByAmount(@Param("amount")BigDecimal amount);
}

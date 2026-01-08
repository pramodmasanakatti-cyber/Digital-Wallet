package com.digitalwallet.repository;

import com.digitalwallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>, JpaSpecificationExecutor<Transaction> {
    Optional<Transaction> findByExternalTxId(String externalTxId);

    boolean existsByExternalTxId(String txId);

    @Query("SELECT COALESCE(SUM(t.amount),0) FROM Transaction t WHERE t.senderWalletId= :walletId OR t.walletId= :walletId AND t.transactionType!='CREDIT'  AND FUNCTION(DATE,t.transactionDate)= :date")
    BigDecimal sumByWalletAndDate(@Param("walletId") Integer walletId,@Param("date") LocalDate date);
}

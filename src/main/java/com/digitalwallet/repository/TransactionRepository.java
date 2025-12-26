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
    List<Transaction> findBySenderWalletWalletId(Integer senderWalletId);
    List<Transaction> findByReceiverWalletWalletId(Integer receiverWalletId);
    List<Transaction> findByStatus(TransactionStatus status);

    // find all transaction between two wallets
    @Query("SELECT t FROM Transaction t WHERE t.senderWallet.walletId= :senderId AND t.receiverWallet.walletId= :receiverId")
    List<Transaction> findTransactionsBetween(@Param("senderId") Integer senderId,@Param("receiverId") Integer receiverId);

    //find transactions with amount (native query)
    @Query(value = "SELECT * FROM Transactions t WHERE t.Amount>= :amount",nativeQuery = true)
    List<Transaction> findTransactionWithAmountGreaterThan(@Param("amount")BigDecimal amount);
}

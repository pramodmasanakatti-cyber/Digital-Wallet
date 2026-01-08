package com.digitalwallet.specification;

import com.digitalwallet.entity.Transaction;
import com.digitalwallet.entity.enums.TransactionStatus;
import com.digitalwallet.entity.enums.TransactionType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TransactionSpecification {
   public static Specification<Transaction> byWallet(Integer walletId) {
return (root, query, criteriaBuilder) -> criteriaBuilder.or(
        criteriaBuilder.equal(root.get("senderWalletId"),walletId),
        criteriaBuilder.equal(root.get("receiverWalletId"),walletId),
        criteriaBuilder.equal(root.get("walletId"),walletId)
);
   }

    public static Specification<Transaction> byType(TransactionType type) {
       return (root, query, criteriaBuilder) ->
               type==null?null:criteriaBuilder.equal(root.get("transactionType"),type);
    }

    public static Specification<Transaction> byStatus(TransactionStatus status) {
       return (root, query, criteriaBuilder) -> status==null?null:criteriaBuilder.equal(root.get("status"),status);
    }

    public static Specification<Transaction> byDateRange(LocalDateTime from, LocalDateTime to) {
       return (root, query, criteriaBuilder) -> (from==null || to==null)?null:criteriaBuilder.between(root.get("trasactionDate"),from,to);
    }
}

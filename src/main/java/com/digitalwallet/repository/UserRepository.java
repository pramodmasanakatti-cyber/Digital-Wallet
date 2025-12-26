package com.digitalwallet.repository;

import com.digitalwallet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findByFullNameContaining(String name);

    //get total balance of a user
    @Query("SELECT SUM(w.balance) FROM Wallet w WHERE w.user.userId= :userId")
    BigDecimal getTotalBalanceByUser(@Param("userId") Integer userId);
}

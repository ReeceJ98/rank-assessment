package com.rank.assignment.respository;

import com.rank.assignment.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

//    List<Transaction> findByPlayerId(long id);

    @Query(value = "SELECT * FROM TRANSACTION WHERE PLAYER_ID = :id ORDER BY TRANSACTION_ID DESC LIMIT 10;", nativeQuery = true)
    List<Transaction> findLastTenTransactions(int id);

}

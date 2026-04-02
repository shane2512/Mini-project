package com.ftms.repository;

import com.ftms.model.Transaction;
import com.ftms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByStatus(Transaction.TransactionStatus status);
    List<Transaction> findByUserOrderByCreatedAtDesc(User user);
}

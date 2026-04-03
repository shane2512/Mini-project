package com.ftms.repository;

// TransactionRepository handles all queries for the transactions table.
// Again, Spring Data JPA auto-generates SQL from method names.

import com.ftms.model.Transaction;
import com.ftms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // Get all transactions by a specific user
    List<Transaction> findByUser(User user);
    
    // Get transactions by status - used by Central Bank to find pending approvals
    List<Transaction> findByStatus(Transaction.TransactionStatus status);
    
    // Get transactions by user ordered by date descending (latest first)
    List<Transaction> findByUserOrderByCreatedAtDesc(User user);
}

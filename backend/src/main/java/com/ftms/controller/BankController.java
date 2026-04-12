package com.ftms.controller;

import com.ftms.model.Transaction;
import com.ftms.repository.TransactionRepository;
import com.ftms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    // GET transactions approved by central bank — pending bank verification
    @GetMapping("/pending-verification")
    public ResponseEntity<List<Transaction>> getPendingVerification() {
        return ResponseEntity
                .ok(transactionRepository.findByStatus(Transaction.TransactionStatus.APPROVED_BY_CENTRAL_BANK));
    }

    // GET completed transactions
    @GetMapping("/completed")
    public ResponseEntity<List<Transaction>> getCompleted() {
        return ResponseEntity.ok(transactionRepository.findByStatus(Transaction.TransactionStatus.COMPLETED));
    }

    // PUT verify and complete transaction
    @PutMapping("/verify/{txnId}")
    public ResponseEntity<Map<String, Object>> verify(@PathVariable Long txnId, Authentication authentication) {
        Map<String, Object> resp = new HashMap<>();
        Optional<Transaction> txnOpt = transactionRepository.findById(txnId);
        if (txnOpt.isEmpty()) {
            resp.put("success", false);
            resp.put("message", "Transaction not found");
            return ResponseEntity.badRequest().body(resp);
        }
        Transaction txn = txnOpt.get();
        txn.setStatus(Transaction.TransactionStatus.COMPLETED);
        txn.setUpdatedAt(LocalDateTime.now());
        userRepository.findByEmail(authentication.getName()).ifPresent(user -> txn.setBankVerifiedBy(user.getId()));
        transactionRepository.save(txn);
        resp.put("success", true);
        resp.put("message", "Transaction #" + txnId + " verified and completed successfully.");
        return ResponseEntity.ok(resp);
    }
}
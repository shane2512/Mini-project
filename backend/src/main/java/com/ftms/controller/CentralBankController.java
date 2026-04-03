package com.ftms.controller;

import com.ftms.model.Transaction;
import com.ftms.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/central-bank")
public class CentralBankController {

    @Autowired
    private TransactionRepository transactionRepository;

    // GET pending (awaiting central bank approval)
    @GetMapping("/pending")
    public ResponseEntity<List<Transaction>> getPending() {
        return ResponseEntity
                .ok(transactionRepository.findByStatus(Transaction.TransactionStatus.PENDING_CENTRAL_BANK));
    }

    // GET approved by central bank
    @GetMapping("/approved")
    public ResponseEntity<List<Transaction>> getApproved() {
        return ResponseEntity
                .ok(transactionRepository.findByStatus(Transaction.TransactionStatus.APPROVED_BY_CENTRAL_BANK));
    }

    // GET rejected by central bank
    @GetMapping("/rejected")
    public ResponseEntity<List<Transaction>> getRejected() {
        return ResponseEntity
                .ok(transactionRepository.findByStatus(Transaction.TransactionStatus.REJECTED_BY_CENTRAL_BANK));
    }

    // PUT approve transaction
    @PutMapping("/approve/{txnId}")
    public ResponseEntity<Map<String, Object>> approve(@PathVariable Long txnId) {
        Map<String, Object> resp = new HashMap<>();
        Optional<Transaction> txnOpt = transactionRepository.findById(txnId);
        if (txnOpt.isEmpty()) {
            resp.put("success", false);
            resp.put("message", "Transaction not found");
            return ResponseEntity.badRequest().body(resp);
        }
        Transaction txn = txnOpt.get();
        txn.setStatus(Transaction.TransactionStatus.APPROVED_BY_CENTRAL_BANK);
        transactionRepository.save(txn);
        resp.put("success", true);
        resp.put("message", "Transaction #" + txnId + " approved. Forwarded to commercial bank.");
        return ResponseEntity.ok(resp);
    }

    // PUT reject transaction
    @PutMapping("/reject/{txnId}")
    public ResponseEntity<Map<String, Object>> reject(@PathVariable Long txnId, @RequestBody Map<String, String> body) {
        Map<String, Object> resp = new HashMap<>();
        Optional<Transaction> txnOpt = transactionRepository.findById(txnId);
        if (txnOpt.isEmpty()) {
            resp.put("success", false);
            resp.put("message", "Transaction not found");
            return ResponseEntity.badRequest().body(resp);
        }
        Transaction txn = txnOpt.get();
        txn.setStatus(Transaction.TransactionStatus.REJECTED_BY_CENTRAL_BANK);
        txn.setRejectionReason(body.get("reason"));
        transactionRepository.save(txn);
        resp.put("success", true);
        resp.put("message", "Transaction #" + txnId + " rejected.");
        return ResponseEntity.ok(resp);
    }
}
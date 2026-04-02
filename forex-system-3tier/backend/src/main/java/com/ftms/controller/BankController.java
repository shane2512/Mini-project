package com.ftms.controller;

import com.ftms.model.Transaction;
import com.ftms.repository.UserRepository;
import com.ftms.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bank")
@RequiredArgsConstructor
public class BankController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;

    @GetMapping("/pending-verification")
    public ResponseEntity<List<Transaction>> getPendingVerification() {
        return ResponseEntity.ok(transactionService.getPendingBankVerification());
    }

    @PutMapping("/verify/{transactionId}")
    public ResponseEntity<Map<String, Object>> verifyTransaction(
            @PathVariable Long transactionId,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            String officerEmail = authentication.getName();
            Long officerId = userRepository.findByEmail(officerEmail)
                    .orElseThrow(() -> new RuntimeException("Officer not found")).getId();
            
            Transaction transaction = transactionService.verifyByBank(transactionId, officerId);
            response.put("success", true);
            response.put("message", "Transaction " + transactionId + " verified and completed.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

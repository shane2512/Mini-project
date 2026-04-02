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
@RequestMapping("/api/central-bank")
@RequiredArgsConstructor
public class CentralBankController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;

    @GetMapping("/pending")
    public ResponseEntity<List<Transaction>> getPendingTransactions() {
        return ResponseEntity.ok(transactionService.getPendingCentralBankApproval());
    }

    @PutMapping("/approve/{transactionId}")
    public ResponseEntity<Map<String, Object>> approveTransaction(
            @PathVariable Long transactionId,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            String officerEmail = authentication.getName();
            Long officerId = userRepository.findByEmail(officerEmail)
                    .orElseThrow(() -> new RuntimeException("Officer not found")).getId();
            
            Transaction transaction = transactionService.approveByCentralBank(transactionId, officerId);
            response.put("success", true);
            response.put("message", "Transaction " + transactionId + " approved. Forwarded to Commercial Bank.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/reject/{transactionId}")
    public ResponseEntity<Map<String, Object>> rejectTransaction(
            @PathVariable Long transactionId,
            @RequestBody Map<String, String> body,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            String officerEmail = authentication.getName();
            Long officerId = userRepository.findByEmail(officerEmail)
                    .orElseThrow(() -> new RuntimeException("Officer not found")).getId();
            
            String reason = body.getOrDefault("reason", "Transaction rejected by Central Bank");
            Transaction transaction = transactionService.rejectByCentralBank(transactionId, officerId, reason);
            response.put("success", true);
            response.put("message", "Transaction " + transactionId + " rejected.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

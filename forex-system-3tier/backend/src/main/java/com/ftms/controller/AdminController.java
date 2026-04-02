package com.ftms.controller;

import com.ftms.model.User;
import com.ftms.model.Transaction;
import com.ftms.service.UserService;
import com.ftms.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final TransactionService transactionService;

    @GetMapping("/pending-kyc")
    public ResponseEntity<List<User>> getPendingKycUsers() {
        return ResponseEntity.ok(userService.getPendingKycUsers());
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/kyc/{userId}/approve")
    public ResponseEntity<Map<String, Object>> approveKyc(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.approveKyc(userId);
            response.put("success", true);
            response.put("message", "KYC approved for user: " + user.getFullName());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/kyc/{userId}/reject")
    public ResponseEntity<Map<String, Object>> rejectKyc(@PathVariable Long userId, 
                                                          @RequestBody Map<String, String> body) {
        Map<String, Object> response = new HashMap<>();
        try {
            String reason = body.getOrDefault("reason", "KYC documents not satisfactory");
            User user = userService.rejectKyc(userId, reason);
            response.put("success", true);
            response.put("message", "KYC rejected for user: " + user.getFullName());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
}

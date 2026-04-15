package com.ftms.controller;

import com.ftms.model.Transaction;
import com.ftms.model.User;
import com.ftms.repository.TransactionRepository;
import com.ftms.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // GET all users pending account approval
    @GetMapping("/pending-account")
    public ResponseEntity<List<User>> getPendingAccount() {
        List<User> pending = userRepository.findByAccountStatus(User.AccountStatus.PENDING);
        // Don't expose password
        pending.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(pending);
    }

    // GET all users pending KYC
    @GetMapping("/pending-kyc")
    public ResponseEntity<List<User>> getPendingKyc() {
        List<User> pending = userRepository.findByKycStatus(User.KycStatus.PENDING);
        // Don't expose password
        pending.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(pending);
    }

    // GET all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(users);
    }

    // GET single user by ID (for passport viewer)
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", false);
            resp.put("message", "User not found");
            return ResponseEntity.badRequest().body(resp);
        }
        User user = userOpt.get();
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    // GET all transactions
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionRepository.findAll());
    }

    // PUT approve account
    @PutMapping("/approve-account/{userId}")
    @Transactional
    public ResponseEntity<Map<String, Object>> approveAccount(@PathVariable Long userId) {
        Map<String, Object> resp = new HashMap<>();
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            resp.put("success", false);
            resp.put("message", "User not found");
            return ResponseEntity.badRequest().body(resp);
        }
        User user = userOpt.get();
        user.setAccountStatus(User.AccountStatus.APPROVED);
        user.setKycStatus(User.KycStatus.APPROVED);
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();
        resp.put("success", true);
        resp.put("message", "Account and KYC approved for " + user.getFullName());
        return ResponseEntity.ok(resp);
    }

    // PUT reject account
    @PutMapping("/reject-account/{userId}")
    @Transactional
    public ResponseEntity<Map<String, Object>> rejectAccount(@PathVariable Long userId) {
        Map<String, Object> resp = new HashMap<>();
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            resp.put("success", false);
            resp.put("message", "User not found");
            return ResponseEntity.badRequest().body(resp);
        }
        User user = userOpt.get();
        user.setAccountStatus(User.AccountStatus.REJECTED);
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();
        resp.put("success", true);
        resp.put("message", "Account rejected for " + user.getFullName());
        return ResponseEntity.ok(resp);
    }

    // PUT approve KYC
    @PutMapping("/approve-kyc/{userId}")
    @Transactional
    public ResponseEntity<Map<String, Object>> approveKyc(@PathVariable Long userId) {
        Map<String, Object> resp = new HashMap<>();
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            resp.put("success", false);
            resp.put("message", "User not found");
            return ResponseEntity.badRequest().body(resp);
        }
        User user = userOpt.get();
        user.setKycStatus(User.KycStatus.APPROVED);
        user.setAccountStatus(User.AccountStatus.APPROVED);
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();
        resp.put("success", true);
        resp.put("message", "KYC and account approved for " + user.getFullName());
        return ResponseEntity.ok(resp);
    }

    // PUT reject KYC
    @PutMapping("/reject-kyc/{userId}")
    public ResponseEntity<Map<String, Object>> rejectKyc(@PathVariable Long userId) {
        Map<String, Object> resp = new HashMap<>();
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            resp.put("success", false);
            resp.put("message", "User not found");
            return ResponseEntity.badRequest().body(resp);
        }
        User user = userOpt.get();
        user.setKycStatus(User.KycStatus.REJECTED);
        userRepository.save(user);
        resp.put("success", true);
        resp.put("message", "KYC rejected for " + user.getFullName());
        return ResponseEntity.ok(resp);
    }

    // GET dashboard statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();

        // Count total users (excluding system roles)
        List<User> allUsers = userRepository.findAll();
        long totalUsers = allUsers.stream()
                .filter(u -> u.getRole() != User.Role.ADMIN
                        && u.getRole() != User.Role.CENTRAL_BANK
                        && u.getRole() != User.Role.COMMERCIAL_BANK)
                .count();

        // Count pending KYC
        long pendingKyc = userRepository.findByKycStatus(User.KycStatus.PENDING).size();

        // Get all transactions
        List<Transaction> allTransactions = transactionRepository.findAll();
        long totalTransactions = allTransactions.size();
        long completedTransactions = allTransactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.COMPLETED)
                .count();

        // Calculate total transaction value
        BigDecimal totalValue = allTransactions.stream()
                .map(t -> t.getFromAmount() != null ? t.getFromAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        stats.put("success", true);
        stats.put("totalUsers", totalUsers);
        stats.put("pendingKycCount", pendingKyc);
        stats.put("totalTransactions", totalTransactions);
        stats.put("completedTransactions", completedTransactions);
        stats.put("totalTransactionValue", totalValue);
        stats.put("averageTransactionSize",
                totalTransactions > 0
                        ? totalValue.divide(BigDecimal.valueOf(totalTransactions), 2, java.math.RoundingMode.HALF_UP)
                                .doubleValue()
                        : 0);

        return ResponseEntity.ok(stats);
    }

    // PUT update user role
    @PutMapping("/update-user-role/{userId}")
    public ResponseEntity<Map<String, Object>> updateUserRole(
            @PathVariable Long userId,
            @RequestBody Map<String, String> body) {
        Map<String, Object> resp = new HashMap<>();
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            resp.put("success", false);
            resp.put("message", "User not found");
            return ResponseEntity.badRequest().body(resp);
        }

        try {
            String newRole = body.get("role");
            User user = userOpt.get();
            user.setRole(User.Role.valueOf(newRole.toUpperCase()));
            userRepository.save(user);

            resp.put("success", true);
            resp.put("message", "Role updated to " + newRole + " for " + user.getFullName());
            resp.put("user", user);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("message", "Invalid role or error updating user");
            return ResponseEntity.badRequest().body(resp);
        }
    }
}
package com.ftms.controller;

import com.ftms.dto.TransactionRequest;
import com.ftms.model.Transaction;
import com.ftms.service.ForexService;
import com.ftms.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/forex")
@RequiredArgsConstructor
public class ForexController {

    private final ForexService forexService;
    private final TransactionService transactionService;

    @GetMapping("/rates")
    public ResponseEntity<Map<String, Object>> getRates() {
        try {
            Map<String, Object> rates = forexService.getAllRates();
            return ResponseEntity.ok(rates);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch rates: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PostMapping("/transaction")
    public ResponseEntity<Map<String, Object>> createTransaction(
            @RequestBody TransactionRequest request,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            String userEmail = authentication.getName();
            Transaction transaction = transactionService.createTransaction(request, userEmail);
            response.put("success", true);
            response.put("message", "Transaction submitted successfully. Awaiting Central Bank approval.");
            response.put("transactionId", transaction.getId());
            response.put("toAmount", transaction.getToAmount());
            response.put("exchangeRate", transaction.getExchangeRate());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/my-transactions")
    public ResponseEntity<List<Transaction>> getMyTransactions(Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(transactionService.getUserTransactions(userEmail));
    }
}

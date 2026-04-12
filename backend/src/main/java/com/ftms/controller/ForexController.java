package com.ftms.controller;

// ForexController handles forex rate fetching and transaction creation by users.
// /api/forex/rates is public (anyone can see rates)
// /api/forex/transaction requires user to be logged in

import com.ftms.dto.TransactionRequest;
import com.ftms.model.Transaction;
import com.ftms.service.ForexService;
import com.ftms.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/forex")
@RequiredArgsConstructor
public class ForexController {

    private final ForexService forexService;
    private final TransactionService transactionService;

    // GET /api/forex/rates
    // Public endpoint - returns all current exchange rates
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

    // POST /api/forex/transaction
    // Authenticated users place a forex order (import/export/exchange)
    // Authentication object from Spring Security contains the logged-in user's
    // email
    @PostMapping("/transaction")
    public ResponseEntity<Map<String, Object>> createTransaction(
            @RequestBody TransactionRequest request,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            // authentication.getName() returns the email from JWT token
            String userEmail = authentication.getName();
            Transaction transaction = transactionService.createTransaction(request, userEmail);
            response.put("success", true);
            response.put("message", "Transaction submitted successfully. Awaiting Central Bank approval.");
            response.put("transactionId", transaction.getId());
            response.put("fromCurrency", transaction.getFromCurrency());
            response.put("fromAmount", transaction.getFromAmount());
            response.put("bridgeCurrency", transaction.getBridgeCurrency());
            response.put("bridgeAmount", transaction.getBridgeAmount());
            response.put("toCurrency", transaction.getToCurrency());
            response.put("toAmount", transaction.getToAmount());
            response.put("exchangeRate", transaction.getExchangeRate());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // GET /api/forex/my-transactions
    // Returns the logged-in user's transaction history
    @GetMapping("/my-transactions")
    public ResponseEntity<List<Transaction>> getMyTransactions(Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(transactionService.getUserTransactions(userEmail));
    }

    // GET /api/forex/invoice/{transactionId}
    // Returns invoice details for a completed transaction
    // Only the user who placed the transaction or admin can view
    @GetMapping("/invoice/{transactionId}")
    public ResponseEntity<Map<String, Object>> getInvoice(
            @PathVariable Long transactionId,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            String userEmail = authentication.getName();
            Map<String, Object> invoice = transactionService.getInvoice(transactionId, userEmail);
            response.putAll(invoice);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // GET /api/forex/convert-bridge
    // Returns live exchange rate with explicit USD bridge currency display
    // Query params: fromCurrency, toCurrency, amount
    // Example: GET
    // /api/forex/convert-bridge?fromCurrency=INR&toCurrency=EUR&amount=10000
    @GetMapping("/convert-bridge")
    public ResponseEntity<Map<String, Object>> convertWithBridge(
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency,
            @RequestParam BigDecimal amount) {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> conversion = forexService.convertWithBridge(fromCurrency, toCurrency, amount);
            response.put("success", true);
            response.put("data", conversion);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

package com.ftms.service;

// TransactionService handles all business logic for forex transactions.
// It uses ForexService to get rates, calculates converted amounts, and manages transaction status changes.

import com.ftms.dto.TransactionRequest;
import com.ftms.model.Transaction;
import com.ftms.model.User;
import com.ftms.repository.TransactionRepository;
import com.ftms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ForexService forexService;

    // Creates a new forex transaction when user places an order
    public Transaction createTransaction(TransactionRequest request, String userEmail) {
        // Find the user who is creating this transaction
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check that user's KYC is approved before they can transact
        if (user.getKycStatus() != User.KycStatus.APPROVED) {
            throw new RuntimeException("Your KYC is not approved yet. Please wait for admin verification.");
        }

        // Get live exchange rate from the free API
        BigDecimal exchangeRate = forexService.getExchangeRate(
                request.getFromCurrency(),
                request.getToCurrency());

        // Calculate how much the user will receive in the target currency
        BigDecimal toAmount = forexService.convertAmount(
                request.getFromCurrency(),
                request.getToCurrency(),
                request.getFromAmount());

        // Create and save the transaction
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setTransactionType(request.getTransactionType());
        transaction.setFromCurrency(request.getFromCurrency());
        transaction.setToCurrency(request.getToCurrency());
        transaction.setFromAmount(request.getFromAmount());
        transaction.setToAmount(toAmount);
        transaction.setExchangeRate(exchangeRate);
        transaction.setPurpose(request.getPurpose());
        transaction.setBeneficiaryName(request.getBeneficiaryName());
        transaction.setBeneficiaryBank(request.getBeneficiaryBank());
        transaction.setBeneficiarySwift(request.getBeneficiarySwift());
        transaction.setStatus(Transaction.TransactionStatus.PENDING_CENTRAL_BANK);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    // Central Bank approves a transaction - moves it to next step (Bank
    // verification)
    public Transaction approveByCentralBank(Long transactionId, Long centralBankUserId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getStatus() != Transaction.TransactionStatus.PENDING_CENTRAL_BANK) {
            throw new RuntimeException("Transaction is not pending central bank approval");
        }

        transaction.setStatus(Transaction.TransactionStatus.APPROVED_BY_CENTRAL_BANK);
        transaction.setCentralBankApprovedBy(centralBankUserId);
        transaction.setUpdatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    // Central Bank rejects a transaction
    public Transaction rejectByCentralBank(Long transactionId, Long centralBankUserId, String reason) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setStatus(Transaction.TransactionStatus.REJECTED_BY_CENTRAL_BANK);
        transaction.setCentralBankApprovedBy(centralBankUserId);
        transaction.setRejectionReason(reason);
        transaction.setUpdatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    // Commercial Bank verifies and completes the transaction
    public Transaction verifyByBank(Long transactionId, Long bankUserId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getStatus() != Transaction.TransactionStatus.APPROVED_BY_CENTRAL_BANK) {
            throw new RuntimeException("Transaction must be approved by Central Bank first");
        }

        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        transaction.setBankVerifiedBy(bankUserId);
        transaction.setUpdatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    // Get all transactions for a specific user (their history)
    public List<Transaction> getUserTransactions(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return transactionRepository.findByUserOrderByCreatedAtDesc(user);
    }

    // Get all transactions pending Central Bank approval
    public List<Transaction> getPendingCentralBankApproval() {
        return transactionRepository.findByStatus(Transaction.TransactionStatus.PENDING_CENTRAL_BANK);
    }

    // Get all transactions approved by Central Bank, waiting for Bank verification
    public List<Transaction> getPendingBankVerification() {
        return transactionRepository.findByStatus(Transaction.TransactionStatus.APPROVED_BY_CENTRAL_BANK);
    }

    // Get all transactions (for admin)
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Get invoice details for a transaction
    public Map<String, Object> getInvoice(Long transactionId, String userEmail) {
        Map<String, Object> response = new HashMap<>();

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Check authorization - only user who created it or admin can view
        if (!transaction.getUser().getEmail().equals(userEmail)) {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (user.getRole() != User.Role.ADMIN) {
                throw new RuntimeException("You don't have permission to view this invoice");
            }
        }

        // Get approver names
        String approvedByAdminName = "N/A";
        String approvedByBankName = "N/A";

        if (transaction.getCentralBankApprovedBy() != null) {
            Optional<User> approverOpt = userRepository.findById(transaction.getCentralBankApprovedBy());
            if (approverOpt.isPresent()) {
                approvedByAdminName = approverOpt.get().getFullName();
            }
        }

        if (transaction.getBankVerifiedBy() != null) {
            Optional<User> verifierOpt = userRepository.findById(transaction.getBankVerifiedBy());
            if (verifierOpt.isPresent()) {
                approvedByBankName = verifierOpt.get().getFullName();
            }
        }

        response.put("success", true);
        response.put("transactionId", transaction.getId());
        response.put("transactionDate", transaction.getCreatedAt().toString());
        response.put("userName", transaction.getUser().getFullName());
        response.put("userEmail", transaction.getUser().getEmail());
        response.put("userPhone", transaction.getUser().getPhone());
        response.put("userCity", transaction.getUser().getCity());
        response.put("userAddress", transaction.getUser().getAddress());
        response.put("transactionType", transaction.getTransactionType().name());
        response.put("fromCurrency", transaction.getFromCurrency());
        response.put("toCurrency", transaction.getToCurrency());
        response.put("fromAmount", transaction.getFromAmount());
        response.put("toAmount", transaction.getToAmount());
        response.put("exchangeRate", transaction.getExchangeRate());
        response.put("beneficiaryName", transaction.getBeneficiaryName());
        response.put("beneficiaryBank", transaction.getBeneficiaryBank());
        response.put("beneficiarySwift", transaction.getBeneficiarySwift());
        response.put("status", transaction.getStatus().name());
        response.put("purpose", transaction.getPurpose());
        response.put("approvedByAdmin", approvedByAdminName);
        response.put("approvedByBank", approvedByBankName);

        return response;
    }
}

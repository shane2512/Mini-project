package com.ftms.service;

import com.ftms.dto.TransactionRequest;
import com.ftms.model.Transaction;
import com.ftms.model.User;
import com.ftms.repository.TransactionRepository;
import com.ftms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ForexService forexService;

    public Transaction createTransaction(TransactionRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getKycStatus() != User.KycStatus.APPROVED) {
            throw new RuntimeException("Your KYC is not approved yet. Please wait for admin verification.");
        }

        BigDecimal exchangeRate = forexService.getExchangeRate(
            request.getFromCurrency(), 
            request.getToCurrency()
        );

        BigDecimal toAmount = forexService.convertAmount(
            request.getFromCurrency(),
            request.getToCurrency(),
            request.getFromAmount()
        );

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

    public Transaction rejectByCentralBank(Long transactionId, Long centralBankUserId, String reason) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setStatus(Transaction.TransactionStatus.REJECTED_BY_CENTRAL_BANK);
        transaction.setCentralBankApprovedBy(centralBankUserId);
        transaction.setRejectionReason(reason);
        transaction.setUpdatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

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

    public List<Transaction> getUserTransactions(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return transactionRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Transaction> getPendingCentralBankApproval() {
        return transactionRepository.findByStatus(Transaction.TransactionStatus.PENDING_CENTRAL_BANK);
    }

    public List<Transaction> getPendingBankVerification() {
        return transactionRepository.findByStatus(Transaction.TransactionStatus.APPROVED_BY_CENTRAL_BANK);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}

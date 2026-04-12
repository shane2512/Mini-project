package com.ftms.model;

// Transaction.java maps to the 'transactions' table.
// Every forex transaction (import, export, or exchange) creates one row here.
// The status field tracks the lifecycle: PENDING -> APPROVED_BY_CENTRAL_BANK -> VERIFIED_BY_BANK -> COMPLETED

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ftms_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ManyToOne means many transactions can belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "from_currency", nullable = false)
    private String fromCurrency;

    @Column(name = "to_currency", nullable = false)
    private String toCurrency;

    @Column(name = "from_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal fromAmount;

    // USDC Bridge Amount - how much USDC was received after converting from_currency
    @Column(name = "bridge_currency")
    private String bridgeCurrency = "USDC"; // Always USDC as per real-world forex standards

    @Column(name = "bridge_amount", precision = 15, scale = 2)
    private BigDecimal bridgeAmount; // Amount in USDC received from step 1

    @Column(name = "to_amount", precision = 15, scale = 2)
    private BigDecimal toAmount;

    @Column(name = "exchange_rate", precision = 15, scale = 6)
    private BigDecimal exchangeRate;

    private String purpose;

    @Column(name = "beneficiary_name")
    private String beneficiaryName;

    @Column(name = "beneficiary_bank")
    private String beneficiaryBank;

    @Column(name = "beneficiary_swift")
    private String beneficiarySwift;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.PENDING_CENTRAL_BANK;

    @Column(name = "central_bank_approved_by")
    private Long centralBankApprovedBy;

    @Column(name = "bank_verified_by")
    private Long bankVerifiedBy;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum TransactionType {
        IMPORT, EXPORT, EXCHANGE
    }

    public enum TransactionStatus {
        PENDING_CENTRAL_BANK,
        APPROVED_BY_CENTRAL_BANK,
        REJECTED_BY_CENTRAL_BANK,
        VERIFIED_BY_BANK,
        COMPLETED,
        FAILED
    }
}

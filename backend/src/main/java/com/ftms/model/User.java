package com.ftms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "ftms_users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = true)
    private String city;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; // IMPORTER, EXPORTER, EXCHANGER, ADMIN, CENTRAL_BANK, COMMERCIAL_BANK

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    private AccountStatus accountStatus = AccountStatus.PENDING; // PENDING, APPROVED, REJECTED - Admin account

    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_status", nullable = false)
    private KycStatus kycStatus = KycStatus.PENDING; // PENDING, APPROVED, REJECTED - KYC verification

    @Column(name = "role_selected", nullable = false)
    private Boolean roleSelected = false; // Has user chosen their role?

    // Bank details (now mandatory during registration)
    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "account_number", nullable = false)
    private String accountNumber = "UNKNOWN";

    @Column(name = "ifsc_code", nullable = false)
    private String ifscCode;

    @Column(name = "swift_code", nullable = false)
    @Pattern(regexp = "^[A-Z0-9]{6,11}$", message = "Invalid SWIFT code format")
    private String swiftCode;

    // KYC passport document stored as Base64
    @Column(name = "passport_data", columnDefinition = "TEXT")
    private String passportData;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Role {
        IMPORTER,
        EXPORTER,
        EXCHANGER,
        ADMIN,
        CENTRAL_BANK,
        COMMERCIAL_BANK
    }

    public enum KycStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    public enum AccountStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
package com.ftms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "ftms_users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;
    private String city;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; // IMPORTER, EXPORTER, EXCHANGER, ADMIN, CENTRAL_BANK, COMMERCIAL_BANK

    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus = KycStatus.PENDING; // PENDING, APPROVED, REJECTED

    @Column(nullable = false)
    private Boolean roleSelected = false; // Has user chosen their role after KYC approval?

    // Bank details
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String swiftCode;

    // KYC passport document stored as Base64
    @Column(columnDefinition = "TEXT")
    private String passportData;

    @Column(updatable = false)
    private LocalDateTime createdAt;

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
}
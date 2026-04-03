package com.ftms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_rates")
@Data
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String baseCurrency; // Usually USD

    @Column(nullable = false)
    private String targetCurrency; // INR, EUR, etc.

    @Column(nullable = false)
    private BigDecimal rate;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50) DEFAULT 'API'")
    private Source source; // API or CENTRAL_BANK_MANUAL

    @Column(nullable = false)
    private LocalDateTime fetchedAt;

    public enum Source {
        API,
        CENTRAL_BANK_MANUAL
    }
}

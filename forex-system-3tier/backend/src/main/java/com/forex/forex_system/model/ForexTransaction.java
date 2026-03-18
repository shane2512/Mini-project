package com.forex.forex_system.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="forex_transactions") @Data @NoArgsConstructor @AllArgsConstructor
public class ForexTransaction {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="forex_id") private Long forexId;
    @Column(name="customer_id") private Long customerId;
    @Column(name="customer_bank_id") private Long customerBankId;
    @Column(name="from_currency") private String fromCurrency;
    @Column(name="to_currency") private String toCurrency;
    @Column(name="from_country_bank_id") private Long fromCountryBankId;
    @Column(name="to_country_bank_id") private Long toCountryBankId;
    @Column(name="amount") private Double amount;
    @Column(name="converted_amount") private Double convertedAmount;
    @Column(name="rate_used") private Double rateUsed;
    @Column(name="transaction_date") private LocalDateTime transactionDate;
}


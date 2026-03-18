package com.forex.forex_system.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
@Entity @Table(name="invoices") @Data @NoArgsConstructor @AllArgsConstructor
public class Invoice {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="invoice_id") private Long invoiceId;
    @Column(name="customer_id") private Long customerId;
    @Column(name="ref_type") private String refType;
    @Column(name="ref_id") private Long refId;
    @Column(name="invoice_date") private LocalDate invoiceDate;
    @Column(name="amount") private Double amount;
    @Column(name="currency_code") private String currencyCode;
    @Column(name="status") private String status;
}


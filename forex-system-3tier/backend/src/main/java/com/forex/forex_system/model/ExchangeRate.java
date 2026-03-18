package com.forex.forex_system.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
@Entity @Table(name="exchange_rates") @Data @NoArgsConstructor @AllArgsConstructor
public class ExchangeRate {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="rate_id") private Long rateId;
    @Column(name="from_currency") private String fromCurrency;
    @Column(name="to_currency") private String toCurrency;
    @Column(name="rate") private Double rate;
    @Column(name="rate_date") private LocalDate rateDate;
}


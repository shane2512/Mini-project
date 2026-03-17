package com.forex.forex_system.model;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="currencies") @Data @NoArgsConstructor @AllArgsConstructor
public class Currency {
    @Id @Column(name="currency_code") private String currencyCode;
    @Column(name="currency_name",nullable=false) private String currencyName;
    @Column(name="country_id") private Long countryId;
    @Column(name="country_bank_id") private Long countryBankId;
}


package com.forex.forex_system.model;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="country_banks") @Data @NoArgsConstructor @AllArgsConstructor
public class CountryBank {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="country_bank_id") private Long countryBankId;
    @Column(name="bank_name",nullable=false) private String bankName;
    @Column(name="country_id") private Long countryId;
    @Column(name="swift_code") private String swiftCode;
    @Column(name="contact_number") private String contactNumber;
}


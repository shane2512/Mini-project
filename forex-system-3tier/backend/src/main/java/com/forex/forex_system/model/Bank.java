package com.forex.forex_system.model;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="banks") @Data @NoArgsConstructor @AllArgsConstructor
public class Bank {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="bank_id") private Long bankId;
    @Column(name="bank_name",nullable=false) private String bankName;
    @Column(name="branch_name") private String branchName;
    @Column(name="ifsc_code") private String ifscCode;
    @Column(name="contact_number") private String contactNumber;
    @Column(name="country_id") private Long countryId;
}


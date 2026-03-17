package com.forex.forex_system.model;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="customers") @Data @NoArgsConstructor @AllArgsConstructor
public class Customer {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="customer_id") private Long customerId;
    @Column(name="name",nullable=false) private String name;
    @Column(name="phone") private String phone;
    @Column(name="email",nullable=false,unique=true) private String email;
    @Column(name="address") private String address;
    @Column(name="customer_type") private String customerType;
    @Column(name="bank_id") private Long bankId;
}


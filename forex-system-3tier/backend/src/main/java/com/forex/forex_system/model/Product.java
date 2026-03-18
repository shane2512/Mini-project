package com.forex.forex_system.model;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="products") @Data @NoArgsConstructor @AllArgsConstructor
public class Product {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="product_id") private Long productId;
    @Column(name="product_name",nullable=false) private String productName;
    @Column(name="hs_code") private String hsCode;
    @Column(name="unit_price",nullable=false) private Double unitPrice;
}


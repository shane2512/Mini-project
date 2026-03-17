package com.forex.forex_system.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
@Entity @Table(name="orders") @Data @NoArgsConstructor @AllArgsConstructor
public class Order {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="order_id") private Long orderId;
    @Column(name="customer_id") private Long customerId;
    @Column(name="product_id") private Long productId;
    @Column(name="order_type") private String orderType;
    @Column(name="quantity") private Integer quantity;
    @Column(name="total_amount") private Double totalAmount;
    @Column(name="currency_code") private String currencyCode;
    @Column(name="status") private String status;
    @Column(name="order_date") private LocalDate orderDate;
}


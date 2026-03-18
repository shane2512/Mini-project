package com.forex.forex_system.model;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="countries") @Data @NoArgsConstructor @AllArgsConstructor
public class Country {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="country_id") private Long countryId;
    @Column(name="country_name",nullable=false) private String countryName;
    @Column(name="iso_code",nullable=false,unique=true) private String isoCode;
}


package com.forex.forex_system.repository;
import com.forex.forex_system.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {}


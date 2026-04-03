package com.ftms.repository;

import com.ftms.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    // Find a specific exchange rate
    Optional<ExchangeRate> findByBaseCurrencyAndTargetCurrency(String baseCurrency, String targetCurrency);

    // Find rates fetched after a certain time (for caching)
    List<ExchangeRate> findByFetchedAtAfter(LocalDateTime time);

    // Find all rates for a base currency
    List<ExchangeRate> findByBaseCurrency(String baseCurrency);
}

package com.forex.forex_system.pattern.strategy;
import com.forex.forex_system.model.ExchangeRate;
import com.forex.forex_system.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;
@Component
public class DirectConversionStrategy implements ForexStrategy {
    @Autowired private ExchangeRateRepository rateRepo;
    @Override
    public double convert(double amount, String fromCurrency, String toCurrency) {
        Optional<ExchangeRate> rate = rateRepo.findByFromCurrencyAndToCurrency(fromCurrency, toCurrency);
        return rate.map(r -> amount * r.getRate()).orElse(0.0);
    }
}


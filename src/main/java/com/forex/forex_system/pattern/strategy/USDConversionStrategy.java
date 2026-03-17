package com.forex.forex_system.pattern.strategy;
import com.forex.forex_system.model.ExchangeRate;
import com.forex.forex_system.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;
@Component
public class USDConversionStrategy implements ForexStrategy {
    @Autowired private ExchangeRateRepository rateRepo;
    @Override
    public double convert(double amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) return amount;
        if (fromCurrency.equals("USD")) {
            Optional<ExchangeRate> rate = rateRepo.findByFromCurrencyAndToCurrency("USD", toCurrency);
            return rate.map(r -> amount * r.getRate()).orElse(0.0);
        }
        if (toCurrency.equals("USD")) {
            Optional<ExchangeRate> rate = rateRepo.findByFromCurrencyAndToCurrency(fromCurrency, "USD");
            return rate.map(r -> amount * r.getRate()).orElse(0.0);
        }
        Optional<ExchangeRate> toUSD = rateRepo.findByFromCurrencyAndToCurrency(fromCurrency, "USD");
        Optional<ExchangeRate> fromUSD = rateRepo.findByFromCurrencyAndToCurrency("USD", toCurrency);
        if (toUSD.isPresent() && fromUSD.isPresent()) {
            double usdAmount = amount * toUSD.get().getRate();
            return usdAmount * fromUSD.get().getRate();
        }
        return 0.0;
    }
}


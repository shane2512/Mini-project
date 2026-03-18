package com.forex.forex_system.service;
import com.forex.forex_system.model.ExchangeRate;
import com.forex.forex_system.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ExchangeRateService {
    @Autowired private ExchangeRateRepository repo;
    public List<ExchangeRate> getAll() { return repo.findAll(); }
    public ExchangeRate save(ExchangeRate rate) { return repo.save(rate); }
    public ExchangeRate getRate(String from, String to) {
        return repo.findByFromCurrencyAndToCurrency(from, to)
            .orElseThrow(() -> new RuntimeException("Rate not found"));
    }
}


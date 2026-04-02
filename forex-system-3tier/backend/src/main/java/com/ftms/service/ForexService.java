package com.ftms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class ForexService {

    @Value("${forex.api.url}")
    private String forexApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @SuppressWarnings("unchecked")
    public Map<String, Object> getAllRates() {
        try {
            Map<String, Object> response = restTemplate.getForObject(forexApiUrl, Map.class);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch exchange rates: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public BigDecimal convertAmount(String fromCurrency, String toCurrency, BigDecimal amount) {
        Map<String, Object> ratesData = getAllRates();
        Map<String, Object> rates = (Map<String, Object>) ratesData.get("rates");
        
        if (rates == null) {
            throw new RuntimeException("Could not get rates from API");
        }

        double fromRate = getRate(rates, fromCurrency);
        double toRate = getRate(rates, toCurrency);
        
        BigDecimal inUSD = amount.divide(BigDecimal.valueOf(fromRate), 6, RoundingMode.HALF_UP);
        BigDecimal result = inUSD.multiply(BigDecimal.valueOf(toRate));
        
        return result.setScale(2, RoundingMode.HALF_UP);
    }

    private double getRate(Map<String, Object> rates, String currency) {
        if ("USD".equals(currency)) return 1.0;
        Object rate = rates.get(currency.toUpperCase());
        if (rate == null) {
            throw new RuntimeException("Currency not found: " + currency);
        }
        return ((Number) rate).doubleValue();
    }

    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        BigDecimal oneUnit = convertAmount(fromCurrency, toCurrency, BigDecimal.ONE);
        return oneUnit;
    }
}

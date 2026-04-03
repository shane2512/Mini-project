package com.ftms.service;

// ForexService is responsible for fetching live exchange rates from the free API.
// It calls https://api.exchangerate-api.com/v4/latest/USD and parses the response.
// Rates are cached in the database to avoid hitting API rate limits.
// No API key needed for this endpoint.

import com.ftms.model.ExchangeRate;
import com.ftms.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ForexService {

    @Value("${forex.api.url}")
    private String forexApiUrl; // https://api.exchangerate-api.com/v4/latest/USD

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    // Fetches all exchange rates against USD and returns them as a Map
    // Uses caching: if rates are less than 1 hour old, returns cached rates
    // Otherwise fetches fresh from API and caches them
    // Example return: { "INR": 83.45, "EUR": 0.92, "GBP": 0.78, ... }
    @SuppressWarnings("unchecked")
    public Map<String, Object> getAllRates() {
        try {
            // Check if we have cached rates less than 1 hour old
            LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
            List<ExchangeRate> cachedRates = exchangeRateRepository.findByFetchedAtAfter(oneHourAgo);

            if (!cachedRates.isEmpty()) {
                // Return cached rates
                Map<String, Object> ratesMap = new HashMap<>();
                Map<String, Double> ratesData = new HashMap<>();

                for (ExchangeRate er : cachedRates) {
                    ratesData.put(er.getTargetCurrency(), er.getRate().doubleValue());
                }

                ratesMap.put("rates", ratesData);
                ratesMap.put("base", "USD");
                ratesMap.put("source", "cached");
                return ratesMap;
            }

            // Fetch fresh rates from API
            Map<String, Object> response = restTemplate.getForObject(forexApiUrl, Map.class);
            Map<String, Object> rates = (Map<String, Object>) response.get("rates");

            if (rates == null) {
                throw new RuntimeException("Could not get rates from API");
            }

            // Cache the rates in database
            rates.forEach((currency, rate) -> {
                ExchangeRate er = new ExchangeRate();
                er.setBaseCurrency("USD");
                er.setTargetCurrency(currency);
                er.setRate(BigDecimal.valueOf(((Number) rate).doubleValue()));
                er.setSource(ExchangeRate.Source.API);
                er.setFetchedAt(LocalDateTime.now());
                exchangeRateRepository.save(er);
            });

            response.put("source", "fresh");
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch exchange rates: " + e.getMessage());
        }
    }

    // Calculates how much of toCurrency you get for a given amount of fromCurrency
    // Example: convertAmount("INR", "USD", 8345.00) returns 100.00 (if rate is
    // 83.45)
    @SuppressWarnings("unchecked")
    public BigDecimal convertAmount(String fromCurrency, String toCurrency, BigDecimal amount) {
        Map<String, Object> ratesData = getAllRates();
        Map<String, Object> rates = (Map<String, Object>) ratesData.get("rates");

        if (rates == null) {
            throw new RuntimeException("Could not get rates from API");
        }

        // The API gives rates relative to USD
        // To convert INR to USD: divide by INR rate
        // To convert INR to EUR: divide by INR rate, multiply by EUR rate

        double fromRate = getRate(rates, fromCurrency);
        double toRate = getRate(rates, toCurrency);

        // Convert: amount in fromCurrency -> USD -> toCurrency
        BigDecimal inUSD = amount.divide(BigDecimal.valueOf(fromRate), 6, RoundingMode.HALF_UP);
        BigDecimal result = inUSD.multiply(BigDecimal.valueOf(toRate));

        return result.setScale(2, RoundingMode.HALF_UP);
    }

    // Gets the specific rate for a currency, handles USD specially (rate is 1.0)
    private double getRate(Map<String, Object> rates, String currency) {
        if ("USD".equals(currency))
            return 1.0;
        Object rate = rates.get(currency.toUpperCase());
        if (rate == null) {
            throw new RuntimeException("Currency not found: " + currency);
        }
        return ((Number) rate).doubleValue();
    }

    // Gets the exchange rate between two currencies
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        // Rate of 1 unit of fromCurrency in terms of toCurrency
        BigDecimal oneUnit = convertAmount(fromCurrency, toCurrency, BigDecimal.ONE);
        return oneUnit;
    }
}

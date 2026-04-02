package com.pattern.strategy;

public interface ForexStrategy {
    double convert(double amount, String fromCurrency, String toCurrency);
}
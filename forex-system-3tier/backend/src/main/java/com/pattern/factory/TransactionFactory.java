package com.pattern.factory;

import com.forex.forex_system.model.ForexTransaction;
import java.time.LocalDateTime;

public class TransactionFactory {

    public static ForexTransaction createTransaction(
            Long customerId, Long bankId,
            String fromCurrency, String toCurrency,
            Long fromBankId, Long toBankId,
            Double amount, Double convertedAmount,
            Double rate) {

        ForexTransaction tx = new ForexTransaction();
        tx.setCustomerId(customerId);
        tx.setCustomerBankId(bankId);
        tx.setFromCurrency(fromCurrency);
        tx.setToCurrency(toCurrency);
        tx.setFromCountryBankId(fromBankId);
        tx.setToCountryBankId(toBankId);
        tx.setAmount(amount);
        tx.setConvertedAmount(convertedAmount);
        tx.setRateUsed(rate);
        tx.setTransactionDate(LocalDateTime.now());
        return tx;
    }
}
package com.ftms.dto;

// TransactionRequest.java receives the data when a user places an import/export/exchange order.
// The user fills a form and this data comes from that form.

import com.ftms.model.Transaction;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private Transaction.TransactionType transactionType;  // IMPORT, EXPORT, or EXCHANGE
    private String fromCurrency;       // e.g. INR
    private String toCurrency;         // e.g. USD
    private BigDecimal fromAmount;     // how much to convert
    private String purpose;            // why they need this forex
    private String beneficiaryName;    // who receives the money
    private String beneficiaryBank;    // which bank receives
    private String beneficiarySwift;   // SWIFT code of receiving bank
}

package com.ftms.dto;

// TransactionRequest.java receives the data when a user places an import/export/exchange order.
// The user fills a form and this data comes from that form.

import com.ftms.model.Transaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private Transaction.TransactionType transactionType; // IMPORT, EXPORT, or EXCHANGE
    private String fromCurrency; // e.g. INR
    private String toCurrency; // e.g. USD
    private BigDecimal fromAmount; // how much to convert
    private String purpose; // why they need this forex

    @NotBlank(message = "Beneficiary name is required")
    private String beneficiaryName; // who receives the money

    @NotBlank(message = "Beneficiary bank name is required")
    private String beneficiaryBank; // which bank receives

    @NotBlank(message = "Beneficiary SWIFT code is mandatory")
    @Pattern(regexp = "^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$", message = "SWIFT code must be 8 or 11 characters: 4 letters (bank) + 2 letters (country) + 2 alphanumeric (location) + optional 3 alphanumeric (branch)")
    private String beneficiarySwift; // SWIFT code of receiving bank
}

package com.ftms.dto;

import com.ftms.model.Transaction;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private Transaction.TransactionType transactionType;
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal fromAmount;
    private String purpose;
    private String beneficiaryName;
    private String beneficiaryBank;
    private String beneficiarySwift;
}

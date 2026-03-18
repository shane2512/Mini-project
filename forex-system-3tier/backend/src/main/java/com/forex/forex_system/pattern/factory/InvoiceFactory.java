package com.forex.forex_system.pattern.factory;
import com.forex.forex_system.model.Invoice;
import java.time.LocalDate;
public class InvoiceFactory {
    public static Invoice createOrderInvoice(Long customerId, Long orderId, Double amount, String currencyCode) {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(customerId);
        invoice.setRefType("ORDER");
        invoice.setRefId(orderId);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setAmount(amount);
        invoice.setCurrencyCode(currencyCode);
        invoice.setStatus("PENDING");
        return invoice;
    }
    public static Invoice createForexInvoice(Long customerId, Long forexId, Double amount, String currencyCode) {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(customerId);
        invoice.setRefType("FOREX");
        invoice.setRefId(forexId);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setAmount(amount);
        invoice.setCurrencyCode(currencyCode);
        invoice.setStatus("PENDING");
        return invoice;
    }
}


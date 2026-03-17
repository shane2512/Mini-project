package com.forex.forex_system.service;
import com.forex.forex_system.model.ForexTransaction;
import com.forex.forex_system.model.Invoice;
import com.forex.forex_system.pattern.factory.InvoiceFactory;
import com.forex.forex_system.pattern.factory.TransactionFactory;
import com.forex.forex_system.pattern.strategy.USDConversionStrategy;
import com.forex.forex_system.repository.ForexRepository;
import com.forex.forex_system.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ForexService {
    @Autowired private ForexRepository forexRepo;
    @Autowired private InvoiceRepository invoiceRepo;
    @Autowired private USDConversionStrategy conversionStrategy;
    public List<ForexTransaction> getAllTransactions() { return forexRepo.findAll(); }
    public List<ForexTransaction> getByCustomer(Long customerId) { return forexRepo.findByCustomerId(customerId); }
    public ForexTransaction performTransaction(ForexTransaction request) {
        double converted = conversionStrategy.convert(request.getAmount(), request.getFromCurrency(), request.getToCurrency());
        double rate = request.getAmount() > 0 ? converted / request.getAmount() : 0;
        ForexTransaction tx = TransactionFactory.createTransaction(
            request.getCustomerId(), request.getCustomerBankId(),
            request.getFromCurrency(), request.getToCurrency(),
            request.getFromCountryBankId(), request.getToCountryBankId(),
            request.getAmount(), converted, rate);
        ForexTransaction saved = forexRepo.save(tx);
        Invoice invoice = InvoiceFactory.createForexInvoice(saved.getCustomerId(), saved.getForexId(), saved.getConvertedAmount(), saved.getToCurrency());
        invoiceRepo.save(invoice);
        return saved;
    }
}


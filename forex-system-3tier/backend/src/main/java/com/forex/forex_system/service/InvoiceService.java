package com.forex.forex_system.service;
import com.forex.forex_system.model.Invoice;
import com.forex.forex_system.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class InvoiceService {
    @Autowired private InvoiceRepository invoiceRepo;
    public List<Invoice> getAllInvoices() { return invoiceRepo.findAll(); }
    public List<Invoice> getByCustomer(Long customerId) { return invoiceRepo.findByCustomerId(customerId); }
    public Invoice updateStatus(Long id, String status) {
        return invoiceRepo.findById(id).map(inv -> {
            inv.setStatus(status);
            return invoiceRepo.save(inv);
        }).orElseThrow(() -> new RuntimeException("Invoice not found"));
    }
}


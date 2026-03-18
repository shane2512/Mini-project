package com.forex.forex_system.controller;
import com.forex.forex_system.model.Invoice;
import com.forex.forex_system.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
public class InvoiceController {
    @Autowired private InvoiceService invoiceService;
    @GetMapping("/invoices") public String invoicesPage() { return "invoices"; }
    @GetMapping("/api/invoices") @ResponseBody public List<Invoice> getAll() { return invoiceService.getAllInvoices(); }
    @PutMapping("/api/invoices/{id}/status") @ResponseBody public Invoice updateStatus(@PathVariable Long id, @RequestParam String status) { return invoiceService.updateStatus(id, status); }
}


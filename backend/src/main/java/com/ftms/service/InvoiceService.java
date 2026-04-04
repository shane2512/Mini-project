package com.ftms.service;

import com.ftms.model.Transaction;
import com.ftms.model.User;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class InvoiceService {

    // Generates an invoice for a completed transaction
    public Map<String, Object> generateInvoice(Transaction transaction) {
        if (transaction.getStatus() != Transaction.TransactionStatus.COMPLETED) {
            throw new RuntimeException("Invoice can only be generated for COMPLETED transactions");
        }

        User user = transaction.getUser();
        Map<String, Object> invoice = new HashMap<>();

        // Invoice metadata
        invoice.put("invoiceNumber",
                "INV-" + transaction.getId() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.put("invoiceDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        invoice.put("transactionId", transaction.getId());

        // Customer details
        Map<String, Object> customer = new HashMap<>();
        customer.put("name", user.getFullName());
        customer.put("email", user.getEmail());
        customer.put("phone", user.getPhone());
        customer.put("city", user.getCity());
        customer.put("address", user.getAddress());
        customer.put("bankName", user.getBankName());
        customer.put("accountNumber", user.getAccountNumber());
        invoice.put("customer", customer);

        // Transaction details
        Map<String, Object> details = new HashMap<>();
        details.put("transactionType", transaction.getTransactionType().toString()); // IMPORT, EXPORT, EXCHANGE
        details.put("fromCurrency", transaction.getFromCurrency());
        details.put("fromAmount", transaction.getFromAmount());

        // USD Bridge currency (always appears in invoice)
        details.put("bridgeCurrency", "USD");
        details.put("exchangeToUSD", transaction.getToAmount()); // Intermediate USD conversion

        details.put("toCurrency", transaction.getToCurrency());
        details.put("toAmount", transaction.getToAmount());
        details.put("exchangeRate", transaction.getExchangeRate());
        details.put("purpose", transaction.getPurpose());

        // Calculate fees (example: 0.5% transaction fee)
        BigDecimal fees = transaction.getFromAmount().multiply(new BigDecimal("0.005"));
        details.put("transactionFee", fees);
        details.put("totalAmount", transaction.getFromAmount().add(fees));

        invoice.put("transactionDetails", details);

        // Beneficiary details (if applicable for IMPORT/EXPORT)
        if (transaction.getBeneficiaryName() != null) {
            Map<String, Object> beneficiary = new HashMap<>();
            beneficiary.put("name", transaction.getBeneficiaryName());
            beneficiary.put("bank", transaction.getBeneficiaryBank());
            beneficiary.put("swiftCode", transaction.getBeneficiarySwift());
            invoice.put("beneficiary", beneficiary);
        }

        // Approval chain
        Map<String, Object> approvals = new HashMap<>();
        approvals.put("centralBankApprovedBy", transaction.getCentralBankApprovedBy());
        approvals.put("bankVerifiedBy", transaction.getBankVerifiedBy());
        approvals.put("createdAt",
                transaction.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        approvals.put("completedAt",
                transaction.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        invoice.put("approvals", approvals);

        // Invoice status
        invoice.put("status", "COMPLETED");
        invoice.put("paid", true);

        return invoice;
    }

    // Generates HTML version of invoice for browser download
    public String generateInvoiceHTML(Transaction transaction) {
        Map<String, Object> invoice = generateInvoice(transaction);

        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <style>\n" +
                "    body { font-family: Arial, sans-serif; background: #f5f5f5; }\n" +
                "    .invoice { max-width: 900px; margin: 20px auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }\n"
                +
                "    .header { text-align: center; margin-bottom: 30px; border-bottom: 3px solid #2196F3; padding-bottom: 20px; }\n"
                +
                "    .header h1 { color: #2196F3; margin: 0; }\n" +
                "    .invoice-number { color: #666; font-size: 14px; }\n" +
                "    .section { margin: 20px 0; }\n" +
                "    .section-title { background: #f0f0f0; padding: 10px 15px; font-weight: bold; margin-bottom: 10px; border-left: 4px solid #2196F3; }\n"
                +
                "    .row { display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid #eee; }\n"
                +
                "    .label { font-weight: bold; color: #333; }\n" +
                "    .value { color: #666; }\n" +
                "    .amount { font-weight: bold; color: #2196F3; }\n" +
                "    .table { width: 100%; border-collapse: collapse; margin: 15px 0; }\n" +
                "    .table th { background: #f0f0f0; padding: 10px; text-align: left; border-bottom: 2px solid #2196F3; }\n"
                +
                "    .table td { padding: 10px; border-bottom: 1px solid #eee; }\n" +
                "    .total-row { font-weight: bold; background: #f0f0f0; }\n" +
                "    .footer { text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee; color: #666; font-size: 12px; }\n"
                +
                "    .bridge-conversion { background: #e3f2fd; padding: 10px; border-left: 3px solid #2196F3; margin: 10px 0; }\n"
                +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"invoice\">\n" +
                "    <div class=\"header\">\n" +
                "      <h1>FTMS INVOICE</h1>\n" +
                "      <p class=\"invoice-number\">Invoice #" + invoice.get("invoiceNumber") + "</p>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"section\">\n" +
                "      <div class=\"section-title\">Customer Information</div>\n";

        @SuppressWarnings("unchecked")
        Map<String, Object> customer = (Map<String, Object>) invoice.get("customer");
        html += "      <div class=\"row\"><span class=\"label\">Name:</span><span class=\"value\">"
                + customer.get("name") + "</span></div>\n" +
                "      <div class=\"row\"><span class=\"label\">Email:</span><span class=\"value\">"
                + customer.get("email") + "</span></div>\n" +
                "      <div class=\"row\"><span class=\"label\">Phone:</span><span class=\"value\">"
                + customer.get("phone") + "</span></div>\n" +
                "      <div class=\"row\"><span class=\"label\">City:</span><span class=\"value\">"
                + customer.get("city") + "</span></div>\n" +
                "      <div class=\"row\"><span class=\"label\">Bank Name:</span><span class=\"value\">"
                + customer.get("bankName") + "</span></div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"section\">\n" +
                "      <div class=\"section-title\">Transaction Details</div>\n";

        @SuppressWarnings("unchecked")
        Map<String, Object> details = (Map<String, Object>) invoice.get("transactionDetails");
        html += "      <div class=\"row\"><span class=\"label\">Transaction Type:</span><span class=\"value\">"
                + details.get("transactionType") + "</span></div>\n" +
                "      <div class=\"row\"><span class=\"label\">Date:</span><span class=\"value\">"
                + invoice.get("invoiceDate") + "</span></div>\n" +
                "\n" +
                "      <div class=\"bridge-conversion\">\n" +
                "        <strong>Currency Conversion (via USD bridge):</strong><br>\n" +
                "        " + details.get("fromCurrency") + " " + details.get("fromAmount") + " → USD → "
                + details.get("toCurrency") + " " + details.get("toAmount") + "\n" +
                "      </div>\n" +
                "\n" +
                "      <table class=\"table\">\n" +
                "        <tr>\n" +
                "          <th>Description</th><th>Amount</th>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td>" + details.get("fromCurrency") + " Amount</td><td class=\"amount\">"
                + details.get("fromAmount") + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td>Exchange Rate</td><td class=\"amount\">1 USD = " + details.get("exchangeRate")
                + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td>" + details.get("toCurrency") + " Amount</td><td class=\"amount\">"
                + details.get("toAmount") + "</td>\n" +
                "        </tr>\n" +
                "        <tr class=\"total-row\">\n" +
                "          <td>Total Amount</td><td class=\"amount\">" + details.get("totalAmount") + "</td>\n" +
                "        </tr>\n" +
                "      </table>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"footer\">\n" +
                "      <p>This is an electronically generated invoice. No signature is required.</p>\n" +
                "      <p>For queries, contact support@ftms.com</p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>";

        return html;
    }
}

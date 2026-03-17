package com.forex.forex_system.service;
import com.forex.forex_system.model.Order;
import com.forex.forex_system.model.Product;
import com.forex.forex_system.model.Invoice;
import com.forex.forex_system.pattern.factory.InvoiceFactory;
import com.forex.forex_system.repository.OrderRepository;
import com.forex.forex_system.repository.ProductRepository;
import com.forex.forex_system.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
@Service
public class OrderService {
    @Autowired private OrderRepository orderRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private InvoiceRepository invoiceRepo;
    public List<Order> getAllOrders() { return orderRepo.findAll(); }
    public List<Order> getOrdersByCustomer(Long customerId) { return orderRepo.findByCustomerId(customerId); }
    public Order createOrder(Order order) {
        Product product = productRepo.findById(order.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found"));
        order.setTotalAmount(product.getUnitPrice() * order.getQuantity());
        order.setStatus("PENDING");
        order.setOrderDate(LocalDate.now());
        Order saved = orderRepo.save(order);
        Invoice invoice = InvoiceFactory.createOrderInvoice(saved.getCustomerId(), saved.getOrderId(), saved.getTotalAmount(), saved.getCurrencyCode());
        invoiceRepo.save(invoice);
        return saved;
    }
    public Order updateStatus(Long id, String status) {
        return orderRepo.findById(id).map(o -> {
            o.setStatus(status);
            return orderRepo.save(o);
        }).orElseThrow(() -> new RuntimeException("Order not found"));
    }
}


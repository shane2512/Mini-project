package com.forex.forex_system.controller;
import com.forex.forex_system.model.Order;
import com.forex.forex_system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
public class OrderController {
    @Autowired private OrderService orderService;
    @GetMapping("/orders") public String ordersPage() { return "orders"; }
    @GetMapping("/api/orders") @ResponseBody public List<Order> getAll() { return orderService.getAllOrders(); }
    @PostMapping("/api/orders") @ResponseBody public Order create(@RequestBody Order order) { return orderService.createOrder(order); }
    @PutMapping("/api/orders/{id}/status") @ResponseBody public Order updateStatus(@PathVariable Long id, @RequestParam String status) { return orderService.updateStatus(id, status); }
}


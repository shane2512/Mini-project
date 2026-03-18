package com.forex.forex_system.controller;
import com.forex.forex_system.model.Customer;
import com.forex.forex_system.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
public class CustomerController {
    @Autowired private CustomerService customerService;
    @GetMapping("/customers") public String customersPage() { return "customers"; }
    @GetMapping("/api/customers") @ResponseBody public List<Customer> getAll() { return customerService.getAllCustomers(); }
    @PostMapping("/api/customers") @ResponseBody public Customer create(@RequestBody Customer customer) { return customerService.saveCustomer(customer); }
    @PutMapping("/api/customers/{id}") @ResponseBody public Customer update(@PathVariable Long id, @RequestBody Customer customer) { return customerService.updateCustomer(id, customer); }
    @DeleteMapping("/api/customers/{id}") @ResponseBody public ResponseEntity<?> delete(@PathVariable Long id) { customerService.deleteCustomer(id); return ResponseEntity.ok().build(); }
}


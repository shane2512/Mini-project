package com.forex.forex_system.service;
import com.forex.forex_system.model.Customer;
import com.forex.forex_system.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class CustomerService {
    @Autowired private CustomerRepository customerRepo;
    public List<Customer> getAllCustomers() { return customerRepo.findAll(); }
    public Optional<Customer> getCustomerById(Long id) { return customerRepo.findById(id); }
    public Customer saveCustomer(Customer customer) { return customerRepo.save(customer); }
    public void deleteCustomer(Long id) { customerRepo.deleteById(id); }
    public Optional<Customer> findByEmail(String email) { return customerRepo.findByEmail(email); }
    public Customer updateCustomer(Long id, Customer updated) {
        return customerRepo.findById(id).map(c -> {
            c.setName(updated.getName()); c.setPhone(updated.getPhone());
            c.setEmail(updated.getEmail()); c.setAddress(updated.getAddress());
            c.setCustomerType(updated.getCustomerType()); c.setBankId(updated.getBankId());
            return customerRepo.save(c);
        }).orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}


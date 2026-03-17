package com.forex.forex_system.service;
import com.forex.forex_system.model.Product;
import com.forex.forex_system.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ProductService {
    @Autowired private ProductRepository repo;
    public List<Product> getAll() { return repo.findAll(); }
    public Product save(Product p) { return repo.save(p); }
    public void delete(Long id) { repo.deleteById(id); }
}


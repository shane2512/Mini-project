package com.forex.forex_system.controller;
import com.forex.forex_system.model.Product;
import com.forex.forex_system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
public class ProductController {
    @Autowired private ProductService productService;
    @GetMapping("/products") public String productsPage() { return "products"; }
    @GetMapping("/api/products") @ResponseBody public List<Product> getAll() { return productService.getAll(); }
    @PostMapping("/api/products") @ResponseBody public Product create(@RequestBody Product product) { return productService.save(product); }
    @DeleteMapping("/api/products/{id}") @ResponseBody public ResponseEntity<?> delete(@PathVariable Long id) { productService.delete(id); return ResponseEntity.ok().build(); }
}


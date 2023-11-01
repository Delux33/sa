package com.sa.controller;

import com.sa.entity.Product;
import com.sa.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductRest {

    @Autowired
    private ProductRepository productRepo;

    @GetMapping
    public List<Product> getAllProducts() {
        return (List<Product>) productRepo.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepo.findById(id).orElse(null);
    }

    @PostMapping
    public void createProduct(@RequestBody Product product) {
        productRepo.save(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id,
                                 @RequestBody Product product) {
        if (productRepo.existsById(id)) {
            product.setId(id);
            return productRepo.save(product);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
    }

    @DeleteMapping()
    public void deleteProduct() {
        productRepo.deleteAll();
    }
}

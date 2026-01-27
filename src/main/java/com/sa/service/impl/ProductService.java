package com.sa.service.impl;

import com.sa.entity.Product;
import com.sa.repos.ProductRepository;
import com.sa.service.IDefaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IDefaultService<Product> {

    private final ProductRepository productRepo;

    @Override
    public ResponseEntity<List<Product>> getAll() {
        List<Product> products = (List<Product>) productRepo.findAll();
        return ResponseEntity.ok(products);
    }

    @Override
    public ResponseEntity<Product> getById(Long id) {
        return productRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<Product>> getByName(String productName) {
        List<Product> products = productRepo.findByName(productName);
        return ResponseEntity.ok(products);
    }

    @Override
    public void create(Product product) {
        productRepo.save(product);
    }

    @Override
    public ResponseEntity<Product> update(Long id, Product product) {
        if (productRepo.existsById(id)) {
            product.setId(id);
            Product saved = productRepo.save(product);
            return ResponseEntity.ok(saved);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public void deleteById(Long id) {
        productRepo.deleteById(id);
    }

    @Override
    public void deleteAll() {
        productRepo.deleteAll();
    }
}

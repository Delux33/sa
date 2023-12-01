package com.sa.service.impl;

import com.sa.entity.Product;
import com.sa.repos.ProductRepository;
import com.sa.service.IDefaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IDefaultService<Product> {

    @Autowired
    private ProductRepository productRepo;

    @Override
    public List<Product> getAll() {
        return (List<Product>) productRepo.findAll();
    }

    @Override
    public Product getById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    @Override
    public List<Product> getByName(String productName) {
        return productRepo.findByName(productName);
    }

    @Override
    public void create(Product product) {
        productRepo.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        if (productRepo.existsById(id)) {
            product.setId(id);
            return productRepo.save(product);
        }
        return null;
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

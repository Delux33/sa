package com.sa.service.impl;

import com.sa.entity.Product;
import com.sa.repos.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepo;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product(1L, "Laptop", 999.99);
        product2 = new Product(2L, "Phone", 499.99);
    }

    @Test
    @DisplayName("Should get all products")
    void shouldGetAllProducts() {
        when(productRepo.findAll()).thenReturn(Arrays.asList(product1, product2));

        ResponseEntity<List<Product>> response = productService.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(productRepo).findAll();
    }

    @Test
    @DisplayName("Should get product by ID when exists")
    void shouldGetProductByIdWhenExists() {
        when(productRepo.findById(1L)).thenReturn(Optional.of(product1));

        ResponseEntity<Product> response = productService.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Laptop", response.getBody().getName());
        verify(productRepo).findById(1L);
    }

    @Test
    @DisplayName("Should return not found when product does not exist")
    void shouldReturnNotFoundWhenProductDoesNotExist() {
        when(productRepo.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productService.getById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productRepo).findById(99L);
    }

    @Test
    @DisplayName("Should get products by name")
    void shouldGetProductsByName() {
        when(productRepo.findByName("Laptop")).thenReturn(Collections.singletonList(product1));

        ResponseEntity<List<Product>> response = productService.getByName("Laptop");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Laptop", response.getBody().get(0).getName());
        verify(productRepo).findByName("Laptop");
    }

    @Test
    @DisplayName("Should create product")
    void shouldCreateProduct() {
        productService.create(product1);

        verify(productRepo).save(product1);
    }

    @Test
    @DisplayName("Should update product when exists")
    void shouldUpdateProductWhenExists() {
        when(productRepo.existsById(1L)).thenReturn(true);
        when(productRepo.save(any(Product.class))).thenReturn(product1);

        ResponseEntity<Product> response = productService.update(1L, product1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(productRepo).existsById(1L);
        verify(productRepo).save(product1);
    }

    @Test
    @DisplayName("Should return not found when updating non-existent product")
    void shouldReturnNotFoundWhenUpdatingNonExistentProduct() {
        when(productRepo.existsById(99L)).thenReturn(false);

        ResponseEntity<Product> response = productService.update(99L, product1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productRepo).existsById(99L);
        verify(productRepo, never()).save(any());
    }

    @Test
    @DisplayName("Should delete product by ID")
    void shouldDeleteProductById() {
        productService.deleteById(1L);

        verify(productRepo).deleteById(1L);
    }

    @Test
    @DisplayName("Should delete all products")
    void shouldDeleteAllProducts() {
        productService.deleteAll();

        verify(productRepo).deleteAll();
    }
}

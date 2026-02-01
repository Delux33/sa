package com.sa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.entity.Product;
import com.sa.service.impl.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@DisplayName("ProductController Tests")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    void shouldGetAllProducts() throws Exception {
        List<Product> products = Arrays.asList(product1, product2);
        when(productService.getAll()).thenReturn(ResponseEntity.ok(products));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptop"));

        verify(productService).getAll();
    }

    @Test
    @DisplayName("Should get product by ID")
    void shouldGetProductById() throws Exception {
        when(productService.getById(1L)).thenReturn(ResponseEntity.ok(product1));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(999.99));

        verify(productService).getById(1L);
    }

    @Test
    @DisplayName("Should return not found for non-existent product")
    void shouldReturnNotFoundForNonExistentProduct() throws Exception {
        when(productService.getById(99L)).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());

        verify(productService).getById(99L);
    }

    @Test
    @DisplayName("Should get products by name")
    void shouldGetProductsByName() throws Exception {
        when(productService.getByName("Laptop")).thenReturn(ResponseEntity.ok(Collections.singletonList(product1)));

        mockMvc.perform(get("/api/products/name/Laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Laptop"));

        verify(productService).getByName("Laptop");
    }

    @Test
    @DisplayName("Should create product with JSON")
    void shouldCreateProductWithJson() throws Exception {
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isCreated());

        verify(productService).create(any(Product.class));
    }

    @Test
    @DisplayName("Should create product with XML")
    void shouldCreateProductWithXml() throws Exception {
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_XML)
                .content("<product><name>Laptop</name><price>999.99</price></product>"))
                .andExpect(status().isCreated());

        verify(productService).create(any(Product.class));
    }

    @Test
    @DisplayName("Should update product")
    void shouldUpdateProduct() throws Exception {
        when(productService.update(eq(1L), any(Product.class))).thenReturn(ResponseEntity.ok(product1));

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));

        verify(productService).update(eq(1L), any(Product.class));
    }

    @Test
    @DisplayName("Should update product with XML")
    void shouldUpdateProductWithXml() throws Exception {
        when(productService.update(eq(1L), any(Product.class))).thenReturn(ResponseEntity.ok(product1));

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_XML)
                .content("<product><name>Laptop</name><price>999.99</price></product>"))
                .andExpect(status().isOk());

        verify(productService).update(eq(1L), any(Product.class));
    }

    @Test
    @DisplayName("Should return not found when updating non-existent product")
    void shouldReturnNotFoundWhenUpdatingNonExistentProduct() throws Exception {
        when(productService.update(eq(99L), any(Product.class))).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(put("/api/products/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isNotFound());

        verify(productService).update(eq(99L), any(Product.class));
    }

    @Test
    @DisplayName("Should delete product by ID")
    void shouldDeleteProductById() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk());

        verify(productService).deleteById(1L);
    }

    @Test
    @DisplayName("Should delete all products")
    void shouldDeleteAllProducts() throws Exception {
        mockMvc.perform(delete("/api/products"))
                .andExpect(status().isOk());

        verify(productService).deleteAll();
    }
}

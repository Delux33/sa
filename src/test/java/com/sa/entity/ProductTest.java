package com.sa.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product Entity Tests")
class ProductTest {

    @Test
    @DisplayName("Should create Product with default constructor")
    void shouldCreateProductWithDefaultConstructor() {
        Product product = new Product();
        assertNull(product.getId());
        assertNull(product.getName());
        assertNull(product.getPrice());
    }

    @Test
    @DisplayName("Should create Product with name and price constructor")
    void shouldCreateProductWithNameAndPrice() {
        Product product = new Product("Laptop", 999.99);

        assertEquals("Laptop", product.getName());
        assertEquals(999.99, product.getPrice());
    }

    @Test
    @DisplayName("Should create Product with all args constructor")
    void shouldCreateProductWithAllArgsConstructor() {
        Product product = new Product(1L, "Laptop", 999.99);

        assertEquals(1L, product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals(999.99, product.getPrice());
    }

    @Test
    @DisplayName("Should use Builder pattern")
    void shouldUseBuilder() {
        Product product = Product.builder()
                .id(1L)
                .name("Laptop")
                .price(999.99)
                .build();

        assertEquals(1L, product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals(999.99, product.getPrice());
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void shouldTestEqualsAndHashCode() {
        Product product1 = new Product(1L, "Laptop", 999.99);
        Product product2 = new Product(1L, "Laptop", 999.99);
        Product product3 = new Product(2L, "Phone", 499.99);

        assertEquals(product1, product2);
        assertNotEquals(product1, product3);
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    @DisplayName("Should test toString")
    void shouldTestToString() {
        Product product = new Product(1L, "Laptop", 999.99);

        String toString = product.toString();
        assertTrue(toString.contains("Laptop"));
        assertTrue(toString.contains("999.99"));
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        Product product = new Product();

        product.setId(10L);
        product.setName("Phone");
        product.setPrice(599.99);

        assertEquals(10L, product.getId());
        assertEquals("Phone", product.getName());
        assertEquals(599.99, product.getPrice());
    }

    @Test
    @DisplayName("Should test canEqual method")
    void shouldTestCanEqual() {
        Product product1 = new Product(1L, "Laptop", 999.99);
        Product product2 = new Product(1L, "Laptop", 999.99);

        assertTrue(product1.canEqual(product2));
    }
}

package com.sa.repos;

import com.sa.entity.Product;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Hidden
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findByName(String productName);
}

package com.sa.controller;

import com.sa.entity.Product;
import com.sa.entity.swagger.ProductForSwagger;
import com.sa.service.impl.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "api/products")
@Tag(name = "Product API", description = "API по управлению продуктами")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Получить все продукты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукты найдены",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Product.class))))
    })
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAll();
    }

    @Operation(summary = "Получить продукт по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class)))
    })
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @Operation(summary = "Получить продукт(ы) по названию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт найден",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Product.class))))
    })
    @GetMapping("/name/{name}")
    public List<Product> getProductsByName(@PathVariable @RequestParam String productName) {
        return productService.getByName(productName);
    }

    @Operation(summary = "Создание нового продукта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно создан", content = @Content),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = @Content)
    })
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void createProduct(@io.swagger.v3.oas.annotations.parameters.RequestBody @RequestBody ProductForSwagger productForSwagger) {
        productService.create(new Product(productForSwagger.getName(), productForSwagger.getPrice()));
    }

    @Operation(summary = "Обновление продукта по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно обновлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                    content = @Content)
    })
    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Product updateProduct(@PathVariable Long id,
                                 @io.swagger.v3.oas.annotations.parameters.RequestBody @RequestBody ProductForSwagger productForSwagger) {
//      ResponseEntity.ok(update);
        return productService.update(id, new Product(productForSwagger.getName(), productForSwagger.getPrice()));
    }

    @Operation(summary = "Удаление продукта по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно удален")
    })
    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteById(id);
    }

    @Operation(summary = "Удаление всех продуктов")
    @DeleteMapping()
    public void deleteAllProducts() {
        productService.deleteAll();
    }
}

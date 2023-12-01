package com.sa.entity.swagger;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Hidden
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductForSwagger {

    @Hidden
    private Long id;

    private String name;

    private Double price;
}

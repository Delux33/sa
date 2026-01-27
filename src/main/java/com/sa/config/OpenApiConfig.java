package com.sa.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.ComponentScan;

@OpenAPIDefinition(
        info = @Info(
                title = "Документация API для того чтобы потыкать swagger и postman",
                version = "1.0.0"
        )
)
@ComponentScan(basePackages = "com.sa.controller")
public class OpenApiConfig {

}

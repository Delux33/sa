package com.sa.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.ComponentScan;

@OpenAPIDefinition(
        info = @Info(
                title = "API documentation for SA application",
                description = "API for Products and Users", version = "1.0.0"
        )
)
@ComponentScan(basePackages = "com.sa.controller")
public class OpenApiConfig {

}

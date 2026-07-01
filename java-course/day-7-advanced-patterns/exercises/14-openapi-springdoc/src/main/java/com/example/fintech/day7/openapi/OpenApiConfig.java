package com.example.fintech.day7.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Top-level API metadata — rendered on the Swagger UI landing page and in the
 * {@code info} section of {@code /v3/api-docs}.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI fintechOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Fintech Payment API")
                .version("1.0")
                .description("REST API for creating and querying payments in the fintech course project."));
    }
}

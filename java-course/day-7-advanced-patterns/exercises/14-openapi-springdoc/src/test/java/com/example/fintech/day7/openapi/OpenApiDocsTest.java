package com.example.fintech.day7.openapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpenApiDocsTest {

    @Autowired
    TestRestTemplate rest;

    @Test
    void apiDocs_returnsGeneratedOpenApiSpec() {
        ResponseEntity<String> response = rest.getForEntity("/v3/api-docs", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Fintech Payment API");
    }

    @Test
    void swaggerUi_isAvailable() {
        ResponseEntity<String> response = rest.getForEntity("/swagger-ui.html", String.class);

        // /swagger-ui.html redirects to /swagger-ui/index.html — accept either the
        // redirect itself or the followed-through 200.
        assertThat(response.getStatusCode().is2xxSuccessful()
                || response.getStatusCode().is3xxRedirection()).isTrue();
    }
}

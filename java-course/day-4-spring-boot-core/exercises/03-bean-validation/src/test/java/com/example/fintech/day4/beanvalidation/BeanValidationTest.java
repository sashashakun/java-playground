package com.example.fintech.day4.beanvalidation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BeanValidationTest {

    @Autowired MockMvc mockMvc;

    private static final String VALID_JSON =
        """
        {
          "amount": 100.00,
          "currency": "USD",
          "description": "Subscription fee",
          "idempotencyKey": "idem-key-001"
        }
        """;

    @Test
    void validRequestReturns201() throws Exception {
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.amount").value(100.00));
    }

    @Test
    void missingAmountReturns400() throws Exception {
        String json = """{"currency":"USD","description":"Test","idempotencyKey":"k1"}""";
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[*].field", hasItem("amount")));
    }

    @Test
    void zeroAmountReturns400() throws Exception {
        String json = """{"amount":0,"currency":"USD","description":"Test","idempotencyKey":"k1"}""";
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    void invalidCurrencyPatternReturns400() throws Exception {
        String json = """{"amount":100,"currency":"us","description":"Test","idempotencyKey":"k1"}""";
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[*].field", hasItem("currency")));
    }

    @Test
    void blankDescriptionReturns400() throws Exception {
        String json = """{"amount":100,"currency":"USD","description":"","idempotencyKey":"k1"}""";
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    void missingIdempotencyKeyReturns400() throws Exception {
        String json = """{"amount":100,"currency":"USD","description":"Test"}""";
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    void validationResponseContainsMessageField() throws Exception {
        String json = """{}""";
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").isNotEmpty())
            .andExpect(jsonPath("$.errors").isArray());
    }
}

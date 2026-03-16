package com.example.fintech.day7.validation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class CustomValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void validRequest_returns200() throws Exception {
        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"userId":"user-1","amount":100.00,"currency":"USD"}
                        """))
            .andExpect(status().isOk());
    }

    @Test
    void invalidCurrencyCode_returns400() throws Exception {
        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"userId":"user-1","amount":100.00,"currency":"XYZ"}
                        """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void numericCurrencyCode_returns400() throws Exception {
        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"userId":"user-1","amount":100.00,"currency":"123"}
                        """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void amountExceedsUsdLimit_returns400() throws Exception {
        // USD max is 10,000 — 15,000 should fail
        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"userId":"user-1","amount":15000.00,"currency":"USD"}
                        """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void amountExceedsEurLimit_returns400() throws Exception {
        // EUR max is 8,000 — 9,000 should fail
        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"userId":"user-1","amount":9000.00,"currency":"EUR"}
                        """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void negativeAmount_returns400() throws Exception {
        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"userId":"user-1","amount":-50.00,"currency":"USD"}
                        """))
            .andExpect(status().isBadRequest());
    }

    @Test
    void jpyWithinLimit_returns200() throws Exception {
        // JPY max is 1,000,000 — 500,000 is fine
        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"userId":"user-1","amount":500000,"currency":"JPY"}
                        """))
            .andExpect(status().isOk());
    }

    @Test
    void missingUserId_returns400() throws Exception {
        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"amount":100.00,"currency":"USD"}
                        """))
            .andExpect(status().isBadRequest());
    }
}

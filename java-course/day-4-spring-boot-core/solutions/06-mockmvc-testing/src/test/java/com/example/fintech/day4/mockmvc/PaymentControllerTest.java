package com.example.fintech.day4.mockmvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private PaymentService service;

    private Payment samplePayment;

    @BeforeEach
    void setUp() {
        samplePayment = new Payment("p1", new BigDecimal("100.00"), "USD",
                                    "Test payment", "PENDING", Instant.now());
    }

    @Test
    void getAllReturns200WithList() throws Exception {
        when(service.findAll()).thenReturn(List.of(samplePayment));
        mockMvc.perform(get("/api/payments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value("p1"));
    }

    @Test
    void getAllReturns200WhenEmpty() throws Exception {
        when(service.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/payments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getByIdFoundReturns200() throws Exception {
        when(service.findById("p1")).thenReturn(Optional.of(samplePayment));
        mockMvc.perform(get("/api/payments/p1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("p1"))
            .andExpect(jsonPath("$.amount").value(100.0));
    }

    @Test
    void getByIdNotFoundReturns404() throws Exception {
        when(service.findById("ghost")).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/payments/ghost"))
            .andExpect(status().isNotFound());
    }

    @Test
    void createReturns201WithLocationHeader() throws Exception {
        when(service.create(any())).thenReturn(samplePayment);
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"amount":100,"currency":"USD","description":"Test"}"""))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", containsString("/api/payments/")))
            .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void createWithMissingAmountReturns400() throws Exception {
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"currency":"USD","description":"Test"}"""))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteFoundReturns204() throws Exception {
        when(service.delete("p1")).thenReturn(true);
        mockMvc.perform(delete("/api/payments/p1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteNotFoundReturns404() throws Exception {
        when(service.delete("ghost")).thenReturn(false);
        mockMvc.perform(delete("/api/payments/ghost"))
            .andExpect(status().isNotFound());
    }
}

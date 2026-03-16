package com.example.fintech.day4.dependencyinjection;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerDITest {

    @Autowired MockMvc mockMvc;
    @MockitoBean PaymentService service;

    private final Payment p1 = new Payment("p1", new BigDecimal("100"), "USD",
                                           "Test", "PENDING", Instant.now());

    @Test
    void getAllDelegatesToService() throws Exception {
        when(service.findAll()).thenReturn(List.of(p1));
        mockMvc.perform(get("/api/payments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("p1"));
    }

    @Test
    void getByIdFoundReturns200() throws Exception {
        when(service.findById("p1")).thenReturn(Optional.of(p1));
        mockMvc.perform(get("/api/payments/p1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    void getByIdMissingReturns404() throws Exception {
        when(service.findById("ghost")).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/payments/ghost"))
            .andExpect(status().isNotFound());
    }

    @Test
    void createReturns201() throws Exception {
        when(service.create(any())).thenReturn(p1);
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"amount":100,"currency":"USD","description":"Test"}"""))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));
    }

    @Test
    void deleteFoundReturns204() throws Exception {
        when(service.delete("p1")).thenReturn(true);
        mockMvc.perform(delete("/api/payments/p1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteMissingReturns404() throws Exception {
        when(service.delete("ghost")).thenReturn(false);
        mockMvc.perform(delete("/api/payments/ghost"))
            .andExpect(status().isNotFound());
    }
}

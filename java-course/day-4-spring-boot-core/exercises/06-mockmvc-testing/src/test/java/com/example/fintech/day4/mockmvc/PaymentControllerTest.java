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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Exercise 06 — Write the MockMvc Tests!
 *
 * This test class is the exercise. The implementation (PaymentController) is provided.
 * Your job: implement all the TODO test methods below.
 *
 * @WebMvcTest loads ONLY the web layer — not the full Spring context.
 * @MockitoBean replaces the real PaymentService with a Mockito mock.
 *
 * Key imports you'll need:
 *   import static org.mockito.Mockito.when;
 *   import static org.mockito.ArgumentMatchers.any;
 *   import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
 *   import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
 *   import static org.hamcrest.Matchers.*;
 *
 * MockMvc patterns:
 *   mockMvc.perform(get("/path"))
 *     .andExpect(status().isOk())
 *     .andExpect(jsonPath("$.field").value("expected"))
 *     .andExpect(jsonPath("$", hasSize(2)))
 *     .andExpect(header().string("Location", containsString("/api/payments/")));
 */
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService service;

    private Payment samplePayment;

    @BeforeEach
    void setUp() {
        samplePayment = new Payment("p1", new BigDecimal("100.00"), "USD",
                                    "Test payment", "PENDING", Instant.now());
    }

    /**
     * TODO 1 — Test GET /api/payments returns 200 and a list
     *
     * Setup:  when(service.findAll()).thenReturn(List.of(samplePayment));
     * Verify: status 200, jsonPath("$", hasSize(1)), jsonPath("$[0].id").value("p1")
     */
    @Test
    void getAllReturns200WithList() throws Exception {
        throw new UnsupportedOperationException("TODO: implement this test");
    }

    /**
     * TODO 2 — Test GET /api/payments returns empty list (200, not 204)
     *
     * Setup:  when(service.findAll()).thenReturn(List.of());
     * Verify: status 200, jsonPath("$", hasSize(0))
     */
    @Test
    void getAllReturns200WhenEmpty() throws Exception {
        throw new UnsupportedOperationException("TODO: implement this test");
    }

    /**
     * TODO 3 — Test GET /api/payments/{id} returns 200 when found
     *
     * Setup:  when(service.findById("p1")).thenReturn(Optional.of(samplePayment));
     * Verify: status 200, jsonPath("$.id").value("p1"), jsonPath("$.amount").value(100.0)
     */
    @Test
    void getByIdFoundReturns200() throws Exception {
        throw new UnsupportedOperationException("TODO: implement this test");
    }

    /**
     * TODO 4 — Test GET /api/payments/{id} returns 404 when not found
     *
     * Setup:  when(service.findById("ghost")).thenReturn(Optional.empty());
     * Verify: status 404
     */
    @Test
    void getByIdNotFoundReturns404() throws Exception {
        throw new UnsupportedOperationException("TODO: implement this test");
    }

    /**
     * TODO 5 — Test POST /api/payments returns 201 with Location header
     *
     * Setup:  when(service.create(any())).thenReturn(samplePayment);
     * Send:   JSON body with valid payment fields
     * Verify: status 201, Location header exists and contains "/api/payments/"
     *         jsonPath("$.status").value("PENDING")
     */
    @Test
    void createReturns201WithLocationHeader() throws Exception {
        throw new UnsupportedOperationException("TODO: implement this test");
    }

    /**
     * TODO 6 — Test POST /api/payments returns 400 for missing amount
     *
     * Send:   JSON body with null/missing amount field
     * Verify: status 400
     * Note:   service.create() should NOT be called (validation fires first)
     */
    @Test
    void createWithMissingAmountReturns400() throws Exception {
        throw new UnsupportedOperationException("TODO: implement this test");
    }

    /**
     * TODO 7 — Test DELETE /api/payments/{id} returns 204 when found
     *
     * Setup:  when(service.delete("p1")).thenReturn(true);
     * Verify: status 204, no response body
     */
    @Test
    void deleteFoundReturns204() throws Exception {
        throw new UnsupportedOperationException("TODO: implement this test");
    }

    /**
     * TODO 8 — Test DELETE /api/payments/{id} returns 404 when not found
     *
     * Setup:  when(service.delete("ghost")).thenReturn(false);
     * Verify: status 404
     */
    @Test
    void deleteNotFoundReturns404() throws Exception {
        throw new UnsupportedOperationException("TODO: implement this test");
    }
}

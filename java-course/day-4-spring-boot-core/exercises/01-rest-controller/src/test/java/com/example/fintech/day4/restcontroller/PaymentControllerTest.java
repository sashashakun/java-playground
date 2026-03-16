package com.example.fintech.day4.restcontroller;

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

/**
 * Exercise 01 Tests — uses @SpringBootTest (full context) + MockMvc
 * because there's no service layer to mock yet.
 *
 * @DirtiesContext ensures each test gets a fresh in-memory store.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String CREATE_JSON =
        """
        {"amount": 100.00, "currency": "USD", "description": "Test payment"}
        """;

    @Test
    void getAllReturnsEmptyListInitially() throws Exception {
        mockMvc.perform(get("/api/payments"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getByIdReturns404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/payments/nonexistent"))
            .andExpect(status().isNotFound());
    }

    @Test
    void createReturns201WithLocationHeader() throws Exception {
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", containsString("/api/payments/")))
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.amount").value(100.00))
            .andExpect(jsonPath("$.currency").value("USD"))
            .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void getByIdReturnsCreatedPayment() throws Exception {
        // First create
        String location = mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_JSON))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getHeader("Location");

        // Extract id from location
        String id = location.substring(location.lastIndexOf('/') + 1);

        // Then fetch
        mockMvc.perform(get("/api/payments/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    void deleteReturns204WhenFound() throws Exception {
        String location = mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_JSON))
            .andReturn().getResponse().getHeader("Location");

        String id = location.substring(location.lastIndexOf('/') + 1);

        mockMvc.perform(delete("/api/payments/{id}", id))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteReturns404WhenNotFound() throws Exception {
        mockMvc.perform(delete("/api/payments/ghost-id"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getAllReturnsCreatedPayments() throws Exception {
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_JSON)).andExpect(status().isCreated());
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_JSON)).andExpect(status().isCreated());

        mockMvc.perform(get("/api/payments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }
}

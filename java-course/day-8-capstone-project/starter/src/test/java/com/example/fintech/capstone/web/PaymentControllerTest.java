package com.example.fintech.capstone.web;

import com.example.fintech.capstone.domain.Account;
import com.example.fintech.capstone.domain.Payment;
import com.example.fintech.capstone.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Capstone Exercise Q — Slice Tests: PaymentControllerTest
 *
 * TODO Q1: Add @WebMvcTest(PaymentController.class) to this class.
 *          Add @MockitoBean on paymentService field.
 *
 * TODO Q2: Complete processPayment_returnsCreated:
 *          - Set up a stub Account and Payment
 *          - when(paymentService.processPayment(any(), any(), any(), any())).thenReturn(payment)
 *          - POST /payments with valid JSON body
 *          - andExpect(status().isCreated())
 *          - andExpect(header().exists("Location"))
 *          - andExpect(jsonPath("$.status").value("PENDING"))
 *
 * TODO Q3: Complete getByAccount_returnsPaginatedList:
 *          - when(paymentService.findByAccount(eq("acc-1"), any(Pageable.class)))
 *              .thenReturn(new PageImpl<>(List.of(payment)))
 *          - GET /payments?accountId=acc-1
 *          - andExpect(status().isOk())
 *          - andExpect(jsonPath("$.content[0].status").value("PENDING"))
 */
// TODO Q1: @WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // TODO Q1: @MockitoBean
    private PaymentService paymentService;

    @Test
    // TODO Q2: implement
    void processPayment_returnsCreated() throws Exception {
        // TODO Q2: implement
    }

    @Test
    // TODO Q3: implement
    void getByAccount_returnsPaginatedList() throws Exception {
        // TODO Q3: implement
    }

    @Test
    void processPayment_missingBody_returns400() throws Exception {
        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
}

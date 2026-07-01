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

// SOLUTION Q — Slice Tests: PaymentControllerTest

@WebMvcTest(PaymentController.class)                    // TODO Q1 ✓
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean                                        // TODO Q1 ✓
    private PaymentService paymentService;

    @Test
    void processPayment_returnsCreated() throws Exception {  // TODO Q2 ✓
        Account account = new Account("owner-1", "USD", new BigDecimal("1000"));
        Payment payment = new Payment(account, new BigDecimal("100.00"), "USD", "Test payment");

        when(paymentService.processPayment(any(), any(), any(), any())).thenReturn(payment);

        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"accountId":"acc-1","amount":100.00,"currency":"USD","description":"Test"}
                    """))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.status").value("PENDING"))
            .andExpect(jsonPath("$.amount").value(100.00));
    }

    @Test
    void getByAccount_returnsPaginatedList() throws Exception {  // TODO Q3 ✓
        Account account = new Account("owner-1", "USD", new BigDecimal("1000"));
        Payment payment = new Payment(account, new BigDecimal("50.00"), "USD", "Coffee");

        when(paymentService.findByAccount(eq("acc-1"), any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(payment)));

        mockMvc.perform(get("/payments").param("accountId", "acc-1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].status").value("PENDING"))
            .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void processPayment_missingBody_returns400() throws Exception {
        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
}

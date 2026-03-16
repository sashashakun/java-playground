package com.example.fintech.day6.webmvctest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransferController.class)
class TransferControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean TransferService service;

    private TransferResponse sample(String id) {
        return new TransferResponse(id, "alice", "bob",
            new BigDecimal("500.00"), "USD", "COMPLETED", Instant.now());
    }

    @Test
    void postValidTransfer_returns201WithLocation() throws Exception {
        when(service.create(any(), any(), any(), any(), any())).thenReturn(sample("txn-001"));

        TransferRequest req = new TransferRequest("alice", "bob",
            new BigDecimal("500.00"), "USD", "ref-1");

        mockMvc.perform(post("/transfers")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/transfers/txn-001")))
            .andExpect(jsonPath("$.transferId").value("txn-001"))
            .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void postMissingFromUserId_returns400() throws Exception {
        String body = """
            {"fromUserId":null,"toUserId":"bob","amount":100,"currency":"USD"}
            """;

        mockMvc.perform(post("/transfers")
                .contentType(APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.title").value("Validation failed"));

        verify(service, never()).create(any(), any(), any(), any(), any());
    }

    @Test
    void postNegativeAmount_returns400() throws Exception {
        String body = """
            {"fromUserId":"alice","toUserId":"bob","amount":-10,"currency":"USD"}
            """;

        mockMvc.perform(post("/transfers")
                .contentType(APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getById_returns200() throws Exception {
        when(service.findById("txn-002")).thenReturn(sample("txn-002"));

        mockMvc.perform(get("/transfers/txn-002"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fromUserId").value("alice"))
            .andExpect(jsonPath("$.toUserId").value("bob"));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        when(service.findById("ghost")).thenThrow(new TransferNotFoundException("ghost"));

        mockMvc.perform(get("/transfers/ghost"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.title").value("Transfer not found"));
    }

    @Test
    void getByUserId_returnsList() throws Exception {
        when(service.findByUserId("alice"))
            .thenReturn(List.of(sample("t1"), sample("t2")));

        mockMvc.perform(get("/transfers").param("userId", "alice"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deleteTransfer_returns204() throws Exception {
        doNothing().when(service).cancel("txn-del");

        mockMvc.perform(delete("/transfers/txn-del"))
            .andExpect(status().isNoContent());

        verify(service).cancel("txn-del");
    }

    @Test
    void postInvalidCurrency_returns400() throws Exception {
        String body = """
            {"fromUserId":"alice","toUserId":"bob","amount":100,"currency":"usd"}
            """;

        mockMvc.perform(post("/transfers")
                .contentType(APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }
}

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

/**
 * Exercise 03 — @WebMvcTest: Write MockMvc Tests
 *
 * @WebMvcTest loads ONLY the web layer: controllers, filters, @ControllerAdvice.
 * It does NOT load @Service or @Repository beans — use @MockitoBean for those.
 *
 * TypeScript analogy: supertest(app).post('/transfers') — but with richer matchers
 * and zero boilerplate server setup.
 *
 * The TransferController and all its supporting classes are provided.
 * Your task: implement all 8 TODO test methods.
 */
@WebMvcTest(TransferController.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransferService service;

    private TransferResponse sample(String id) {
        return new TransferResponse(id, "alice", "bob",
            new BigDecimal("500.00"), "USD", "COMPLETED", Instant.now());
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 1 — POST /transfers returns 201 with Location header
    //
    // Stub service.create(...) to return sample("txn-001").
    // POST a valid TransferRequest body.
    // Assert:
    //   - status 201
    //   - Location header contains "/transfers/txn-001"
    //   - response JSON: $.transferId == "txn-001"
    //   - response JSON: $.status == "COMPLETED"
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO1_postValidTransfer_returns201WithLocation() throws Exception {
        throw new UnsupportedOperationException("TODO 1");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 2 — POST /transfers with missing fromUserId returns 400
    //
    // POST a body with fromUserId = null.
    // Assert: status 400, $.title == "Validation failed"
    // Verify: service.create is never called.
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO2_postMissingFromUserId_returns400() throws Exception {
        throw new UnsupportedOperationException("TODO 2");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 3 — POST /transfers with negative amount returns 400
    //
    // POST a body with amount = -10.
    // Assert: status 400.
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO3_postNegativeAmount_returns400() throws Exception {
        throw new UnsupportedOperationException("TODO 3");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 4 — GET /transfers/{id} returns 200 with body
    //
    // Stub service.findById("txn-002") to return sample("txn-002").
    // GET /transfers/txn-002.
    // Assert: status 200, $.fromUserId == "alice", $.toUserId == "bob".
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO4_getById_returns200() throws Exception {
        throw new UnsupportedOperationException("TODO 4");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 5 — GET /transfers/{id} with unknown id returns 404
    //
    // Stub service.findById("ghost") to throw new TransferNotFoundException("ghost").
    // GET /transfers/ghost.
    // Assert: status 404, $.title == "Transfer not found".
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO5_getById_notFound_returns404() throws Exception {
        throw new UnsupportedOperationException("TODO 5");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 6 — GET /transfers?userId=alice returns list
    //
    // Stub service.findByUserId("alice") to return List.of(sample("t1"), sample("t2")).
    // GET /transfers?userId=alice.
    // Assert: status 200, $.length() == 2.
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO6_getByUserId_returnsList() throws Exception {
        throw new UnsupportedOperationException("TODO 6");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 7 — DELETE /transfers/{id} returns 204
    //
    // Stub service.cancel("txn-del") to do nothing (it's void, so default mock is fine).
    // DELETE /transfers/txn-del.
    // Assert: status 204, no body.
    // Verify: service.cancel("txn-del") was called once.
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO7_deleteTransfer_returns204() throws Exception {
        throw new UnsupportedOperationException("TODO 7");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 8 — POST /transfers with invalid currency (lowercase) returns 400
    //
    // POST a body where currency = "usd" (lowercase — fails @Pattern([A-Z]{3})).
    // Assert: status 400.
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO8_postInvalidCurrency_returns400() throws Exception {
        throw new UnsupportedOperationException("TODO 8");
    }
}

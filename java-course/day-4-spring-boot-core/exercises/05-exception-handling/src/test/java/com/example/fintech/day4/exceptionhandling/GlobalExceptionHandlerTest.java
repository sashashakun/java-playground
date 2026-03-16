package com.example.fintech.day4.exceptionhandling;

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
class GlobalExceptionHandlerTest {

    @Autowired MockMvc mockMvc;

    private static final String VALID_JSON =
        """{"amount":100,"currency":"USD","description":"Test","idempotencyKey":"key-001"}""";

    @Test
    void getByIdMissingReturns404ProblemDetail() throws Exception {
        mockMvc.perform(get("/api/payments/nonexistent"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.title").value("Payment Not Found"))
            .andExpect(jsonPath("$.detail").value(containsString("nonexistent")));
    }

    @Test
    void deleteByIdMissingReturns404ProblemDetail() throws Exception {
        mockMvc.perform(delete("/api/payments/ghost"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void duplicateIdempotencyKeyReturns409() throws Exception {
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_JSON))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_JSON))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.title").value("Duplicate Payment"));
    }

    @Test
    void exceedingLimitReturns422() throws Exception {
        String big = """{"amount":99999,"currency":"USD","description":"Big","idempotencyKey":"big-key"}""";
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(big))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.status").value(422))
            .andExpect(jsonPath("$.title").value("Payment Limit Exceeded"));
    }

    @Test
    void validationErrorReturns400ProblemDetail() throws Exception {
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.title").value("Validation Failed"));
    }

    @Test
    void problemDetailIncludesInstance() throws Exception {
        mockMvc.perform(get("/api/payments/gone"))
            .andExpect(jsonPath("$.instance").value(containsString("/api/payments/gone")));
    }
}

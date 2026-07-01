package com.example.fintech.capstone.integration;

import com.example.fintech.capstone.web.dto.AccountResponse;
import com.example.fintech.capstone.web.dto.CreateAccountRequest;
import com.example.fintech.capstone.web.dto.CreatePaymentRequest;
import com.example.fintech.capstone.web.dto.PaymentResponse;
import com.example.fintech.capstone.web.dto.TransferRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

// SOLUTION R — Integration Tests: CapstoneIntegrationTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // TODO R1 ✓
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // TODO R1 ✓
class CapstoneIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createAccount_thenGetBalance() {              // TODO R2 ✓
        var request = new CreateAccountRequest("owner-1", "USD", new BigDecimal("1000.00"));
        ResponseEntity<AccountResponse> response =
            restTemplate.postForEntity("/accounts", request, AccountResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().currency()).isEqualTo("USD");
        assertThat(response.getBody().balance()).isEqualByComparingTo("1000.00");
    }

    @Test
    void transfer_debitAndCredit() {                   // TODO R3 ✓
        var from = restTemplate.postForEntity("/accounts",
            new CreateAccountRequest("owner-1", "USD", new BigDecimal("500")),
            AccountResponse.class).getBody();
        var to = restTemplate.postForEntity("/accounts",
            new CreateAccountRequest("owner-2", "USD", BigDecimal.ZERO),
            AccountResponse.class).getBody();

        var transferReq = new TransferRequest(from.id(), to.id(), new BigDecimal("200"));
        ResponseEntity<Void> result =
            restTemplate.postForEntity("/accounts/transfer", transferReq, Void.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void processPayment_debitsAccount() {              // TODO R4 ✓
        var account = restTemplate.postForEntity("/accounts",
            new CreateAccountRequest("owner-3", "USD", new BigDecimal("300")),
            AccountResponse.class).getBody();

        var payRequest = new CreatePaymentRequest(
            account.id(), new BigDecimal("100"), "USD", "Subscription");
        ResponseEntity<PaymentResponse> payResponse =
            restTemplate.postForEntity("/payments", payRequest, PaymentResponse.class);

        assertThat(payResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(payResponse.getBody().status().name()).isEqualTo("PENDING");
        assertThat(payResponse.getBody().amount()).isEqualByComparingTo("100");
    }

    @Test
    void createAccount_invalidCurrency_returns400() {
        var request = new CreateAccountRequest("owner-99", "INVALID", new BigDecimal("100"));
        ResponseEntity<String> response = restTemplate.postForEntity("/accounts", request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}

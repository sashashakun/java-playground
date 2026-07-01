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

/**
 * Capstone Exercise R — Integration Tests: CapstoneIntegrationTest
 *
 * TODO R1: Add @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
 *          Add @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
 *          so each test gets a clean database.
 *
 * TODO R2: Complete createAccount_thenGetBalance:
 *          - POST /accounts with CreateAccountRequest("owner-1", "USD", 1000.00)
 *          - assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED)
 *          - assertThat(response.getBody().currency()).isEqualTo("USD")
 *          - assertThat(response.getBody().balance()).isEqualByComparingTo("1000.00")
 *
 * TODO R3: Complete transfer_debitAndCredit:
 *          - Create two accounts (owner-1 USD 500, owner-2 USD 0)
 *          - POST /accounts/transfer with fromAccountId, toAccountId, amount=200
 *          - Verify the response is 204
 *          (Balance verification via GET /accounts?ownerId=owner-1 is a bonus)
 *
 * TODO R4: Complete processPayment_debitsAccount:
 *          - Create account with 300.00 USD
 *          - POST /payments with accountId, amount=100, currency="USD", description="test"
 *          - assertThat(payment status).isEqualTo("PENDING")
 *          - assertThat response status is 201 Created
 */
// TODO R1: @SpringBootTest(webEnvironment = ...) @DirtiesContext(...)
class CapstoneIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    // TODO R2: implement
    void createAccount_thenGetBalance() {
        // TODO R2: implement
    }

    @Test
    // TODO R3: implement
    void transfer_debitAndCredit() {
        // TODO R3: implement
    }

    @Test
    // TODO R4: implement
    void processPayment_debitsAccount() {
        // TODO R4: implement
    }

    @Test
    void createAccount_invalidCurrency_returns400() {
        // This test works without any TODOs — it verifies @ValidCurrency works
        var request = new CreateAccountRequest("owner-99", "INVALID", new BigDecimal("100"));
        ResponseEntity<String> response = restTemplate.postForEntity("/accounts", request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}

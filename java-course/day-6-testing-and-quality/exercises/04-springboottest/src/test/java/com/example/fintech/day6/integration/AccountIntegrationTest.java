package com.example.fintech.day6.integration;

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
 * Exercise 04 — @SpringBootTest Integration Tests
 *
 * @SpringBootTest(webEnvironment = RANDOM_PORT) starts a REAL embedded Tomcat server
 * on a random port. TestRestTemplate makes actual HTTP calls to it.
 *
 * This is the closest you can get to production without Docker/real DB.
 *
 * TypeScript analogy: supertest(app) after calling app.listen() — a real HTTP server,
 * not a mocked one. All layers (controller → service → repository → H2) run together.
 *
 * @DirtiesContext resets the Spring context (and H2 db) between tests to prevent
 * state leakage when using a shared in-memory database.
 *
 * Your task: implement all 7 TODO test methods.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountIntegrationTest {

    @Autowired
    private TestRestTemplate rest;

    private AccountController.OpenRequest openReq(String ownerId) {
        return new AccountController.OpenRequest(ownerId, "USD", new BigDecimal("1000.00"));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 1 — POST /accounts creates an account and returns 201
    //
    // POST an OpenRequest for ownerId="alice".
    // Assert: status 201, response body ownerId == "alice".
    // Also assert: Location header is present and contains the new account ID.
    //
    // Use: rest.postForEntity("/accounts", openReq("alice"), Account.class)
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO1_openAccount_returns201() {
        throw new UnsupportedOperationException("TODO 1");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 2 — POST /accounts twice for same ownerId returns 409
    //
    // Open alice's account once (assert 201).
    // Open alice's account again (assert 409).
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO2_duplicateAccount_returns409() {
        throw new UnsupportedOperationException("TODO 2");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 3 — GET /accounts/{ownerId} returns the account
    //
    // Open alice's account, then GET /accounts/alice.
    // Assert: status 200, balance == 1000.
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO3_getAccount_returnsCorrectBalance() {
        throw new UnsupportedOperationException("TODO 3");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 4 — GET /accounts/{ownerId} for unknown owner returns 404
    //
    // GET /accounts/ghost (no account created).
    // Assert: status 404.
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO4_getUnknownAccount_returns404() {
        throw new UnsupportedOperationException("TODO 4");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 5 — POST /accounts/transfer moves money between accounts
    //
    // Open alice (1000 USD) and bob (500 USD).
    // POST /accounts/transfer with fromOwnerId=alice, toOwnerId=bob, amount=200.
    // Assert: response status 200.
    // GET /accounts/alice → balance == 800.
    // GET /accounts/bob   → balance == 700.
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO5_transfer_updatesBalances() {
        throw new UnsupportedOperationException("TODO 5");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 6 — POST /accounts/transfer with insufficient funds returns 422
    //
    // Open alice (100 USD) and bob (0 USD).
    // POST transfer amount=500 from alice to bob.
    // Assert: status 422.
    // Assert: alice's balance is STILL 100 (transaction rolled back).
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO6_transferInsufficientFunds_returns422AndRollsBack() {
        throw new UnsupportedOperationException("TODO 6");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 7 — POST /accounts/transfer to non-existent account returns 404
    //
    // Open alice (1000 USD). Transfer to "ghost" (doesn't exist).
    // Assert: status 404.
    // Assert: alice's balance is STILL 1000 (rolled back).
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO7_transferToUnknownAccount_returns404AndRollsBack() {
        throw new UnsupportedOperationException("TODO 7");
    }
}

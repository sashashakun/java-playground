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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountIntegrationTest {

    @Autowired TestRestTemplate rest;

    private AccountController.OpenRequest openReq(String ownerId, BigDecimal balance) {
        return new AccountController.OpenRequest(ownerId, "USD", balance);
    }

    private AccountController.OpenRequest openReq(String ownerId) {
        return openReq(ownerId, new BigDecimal("1000.00"));
    }

    @Test
    void openAccount_returns201() {
        ResponseEntity<Account> response = rest.postForEntity(
            "/accounts", openReq("alice"), Account.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getOwnerId()).isEqualTo("alice");
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getHeaders().getLocation().toString())
            .contains("/accounts/");
    }

    @Test
    void duplicateAccount_returns409() {
        rest.postForEntity("/accounts", openReq("alice"), Account.class);

        ResponseEntity<Object> second = rest.postForEntity(
            "/accounts", openReq("alice"), Object.class);

        assertThat(second.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void getAccount_returnsCorrectBalance() {
        rest.postForEntity("/accounts", openReq("bob"), Account.class);

        ResponseEntity<Account> response = rest.getForEntity(
            "/accounts/bob", Account.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getBalance())
            .isEqualByComparingTo("1000.00");
    }

    @Test
    void getUnknownAccount_returns404() {
        ResponseEntity<Object> response = rest.getForEntity("/accounts/ghost", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void transfer_updatesBalances() {
        rest.postForEntity("/accounts", openReq("alice"), Account.class);
        rest.postForEntity("/accounts", openReq("bob", new BigDecimal("500.00")), Account.class);

        var transferReq = new AccountController.TransferRequest(
            "alice", "bob", new BigDecimal("200.00"));
        ResponseEntity<Void> transferResp = rest.postForEntity(
            "/accounts/transfer", transferReq, Void.class);

        assertThat(transferResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(rest.getForEntity("/accounts/alice", Account.class).getBody().getBalance())
            .isEqualByComparingTo("800.00");
        assertThat(rest.getForEntity("/accounts/bob", Account.class).getBody().getBalance())
            .isEqualByComparingTo("700.00");
    }

    @Test
    void transferInsufficientFunds_returns422AndRollsBack() {
        rest.postForEntity("/accounts", openReq("alice", new BigDecimal("100.00")), Account.class);
        rest.postForEntity("/accounts", openReq("bob", BigDecimal.ZERO), Account.class);

        var transferReq = new AccountController.TransferRequest(
            "alice", "bob", new BigDecimal("500.00"));
        ResponseEntity<Object> resp = rest.postForEntity("/accounts/transfer", transferReq, Object.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(rest.getForEntity("/accounts/alice", Account.class).getBody().getBalance())
            .isEqualByComparingTo("100.00"); // rolled back
    }

    @Test
    void transferToUnknownAccount_returns404AndRollsBack() {
        rest.postForEntity("/accounts", openReq("alice"), Account.class);

        var transferReq = new AccountController.TransferRequest(
            "alice", "ghost", new BigDecimal("100.00"));
        ResponseEntity<Object> resp = rest.postForEntity("/accounts/transfer", transferReq, Object.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(rest.getForEntity("/accounts/alice", Account.class).getBody().getBalance())
            .isEqualByComparingTo("1000.00"); // rolled back
    }
}

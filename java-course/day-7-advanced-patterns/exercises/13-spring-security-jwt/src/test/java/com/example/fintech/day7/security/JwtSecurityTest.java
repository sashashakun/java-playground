package com.example.fintech.day7.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtSecurityTest {

    @Autowired
    TestRestTemplate rest;

    @Test
    void requestWithoutToken_isRejected() {
        ResponseEntity<String> response = rest.getForEntity("/api/payments", String.class);

        assertThat(response.getStatusCode().value()).isIn(401, 403);
    }

    @Test
    void requestWithValidToken_returnsPayments() {
        String token = obtainToken("alice");

        ResponseEntity<String> response = rest.exchange(
                "/api/payments", HttpMethod.GET, withBearer(token), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("pay-1");
    }

    @Test
    void userRoleToken_cannotDeletePayments() {
        String token = obtainToken("alice"); // plain user — no ROLE_ADMIN

        ResponseEntity<Void> response = rest.exchange(
                "/api/payments/pay-1", HttpMethod.DELETE, withBearer(token), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private String obtainToken(String username) {
        ResponseEntity<AuthController.TokenResponse> response = rest.postForEntity(
                "/api/auth/token",
                Map.of("username", username, "password", "password"),
                AuthController.TokenResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response.getBody().token();
    }

    private HttpEntity<Void> withBearer(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}

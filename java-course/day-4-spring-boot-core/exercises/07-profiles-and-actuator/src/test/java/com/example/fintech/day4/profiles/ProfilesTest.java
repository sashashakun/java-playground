package com.example.fintech.day4.profiles;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests run with the default profile (no @ActiveProfiles) → MockPaymentGateway
 */
@SpringBootTest
@AutoConfigureMockMvc
class ProfilesTest {

    @Autowired MockMvc mockMvc;
    @Autowired PaymentGateway gateway;

    @Test
    void defaultProfileLoadsMockGateway() {
        assertThat(gateway).isInstanceOf(MockPaymentGateway.class);
        assertThat(gateway.getGatewayName()).isEqualTo("MockGateway");
    }

    @Test
    void mockGatewayChargeReturnsSuccess() {
        var result = gateway.charge("p1", new java.math.BigDecimal("100"), "USD");
        assertThat(result.success()).isTrue();
        assertThat(result.transactionId()).startsWith("mock-");
    }

    @Test
    void gatewayInfoEndpointReturnsMockGatewayName() throws Exception {
        mockMvc.perform(get("/api/gateway/info"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.activeGateway").value("MockGateway"));
    }

    @Test
    void actuatorHealthEndpointReturnsUp() throws Exception {
        mockMvc.perform(get("/actuator/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void actuatorInfoEndpointIsAccessible() throws Exception {
        mockMvc.perform(get("/actuator/info"))
            .andExpect(status().isOk());
    }
}

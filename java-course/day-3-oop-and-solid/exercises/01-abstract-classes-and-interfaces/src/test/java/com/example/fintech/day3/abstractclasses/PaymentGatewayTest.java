package com.example.fintech.day3.abstractclasses;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class PaymentGatewayTest {

    private static final BigDecimal AMOUNT = new BigDecimal("99.99");

    // ─── StripeGateway ───────────────────────────────────────────────────────

    @Test
    void stripeGatewayNameIsStripe() {
        StripeGateway gw = new StripeGateway("sk_test_key");
        assertThat(gw.getGatewayName()).isEqualTo("Stripe");
    }

    @Test
    void stripeProcessSuccessfulCharge() {
        StripeGateway gw = new StripeGateway("sk_test_key");
        var result = gw.process(PaymentGateway.ChargeRequest.of(AMOUNT, "USD", "cust-1"));
        assertThat(result.success()).isTrue();
        assertThat(result.transactionId()).isNotBlank();
    }

    @Test
    void stripeRejectsUnsupportedCurrency() {
        StripeGateway gw = new StripeGateway("sk_test_key");
        var result = gw.process(PaymentGateway.ChargeRequest.of(AMOUNT, "XYZ", "cust-1"));
        assertThat(result.success()).isFalse();
    }

    @Test
    void stripeFailsAuthWithBlankKey() {
        StripeGateway gw = new StripeGateway("");
        assertThatThrownBy(() -> gw.process(
            PaymentGateway.ChargeRequest.of(AMOUNT, "USD", "cust-1")))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("API key");
    }

    @Test
    void stripeAuditLogRecordsCharge() {
        StripeGateway gw = new StripeGateway("sk_test_key");
        gw.process(PaymentGateway.ChargeRequest.of(AMOUNT, "USD", "cust-1"));
        assertThat(gw.getAuditLog()).hasSize(1).first().asString().contains("Stripe");
    }

    @Test
    void stripeRefundSuccess() {
        StripeGateway gw = new StripeGateway("sk_test_key");
        var result = gw.refund("tx-1", new BigDecimal("50.00"));
        assertThat(result.success()).isTrue();
    }

    @Test
    void stripeRefundRejectsZeroAmount() {
        StripeGateway gw = new StripeGateway("sk_test_key");
        assertThatThrownBy(() -> gw.refund("tx-1", BigDecimal.ZERO))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void stripeVerifyReturnsTrueAfterSuccessfulCharge() {
        StripeGateway gw = new StripeGateway("sk_test_key");
        var result = gw.process(PaymentGateway.ChargeRequest.of(AMOUNT, "USD", "cust-1"));
        assertThat(gw.verify(result.transactionId())).isTrue();
    }

    @Test
    void stripeVerifyReturnsFalseForUnknownTransaction() {
        StripeGateway gw = new StripeGateway("sk_test_key");
        assertThat(gw.verify("unknown-tx")).isFalse();
    }

    // ─── PayPalGateway ───────────────────────────────────────────────────────

    @Test
    void paypalGatewayNameIsPayPal() {
        PayPalGateway gw = new PayPalGateway("client-id", "client-secret");
        assertThat(gw.getGatewayName()).isEqualTo("PayPal");
    }

    @Test
    void paypalProcessSuccessfulCharge() {
        PayPalGateway gw = new PayPalGateway("client-id", "client-secret");
        var result = gw.process(PaymentGateway.ChargeRequest.of(AMOUNT, "USD", "cust-1"));
        assertThat(result.success()).isTrue();
    }

    @Test
    void paypalRejectsAmountOverLimit() {
        PayPalGateway gw = new PayPalGateway("client-id", "client-secret");
        var result = gw.process(
            PaymentGateway.ChargeRequest.of(new BigDecimal("15000"), "USD", "cust-1"));
        assertThat(result.success()).isFalse();
    }

    @Test
    void paypalFailsAuthWithBlankCredentials() {
        PayPalGateway gw = new PayPalGateway("", "secret");
        assertThatThrownBy(() -> gw.process(
            PaymentGateway.ChargeRequest.of(AMOUNT, "USD", "cust-1")))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void paypalVerifyAfterSuccessfulCharge() {
        PayPalGateway gw = new PayPalGateway("client-id", "secret");
        var result = gw.process(PaymentGateway.ChargeRequest.of(AMOUNT, "USD", "cust-1"));
        assertThat(gw.verify(result.transactionId())).isTrue();
    }

    @Test
    void paypalDoesNotImplementRefundable() {
        PayPalGateway gw = new PayPalGateway("client-id", "secret");
        assertThat(gw).isNotInstanceOf(Refundable.class);
    }

    // ─── Polymorphism ────────────────────────────────────────────────────────

    @Test
    void polymorphicProcessingViaBaseClass() {
        java.util.List<PaymentGateway> gateways = java.util.List.of(
            new StripeGateway("sk_test"),
            new PayPalGateway("id", "secret")
        );
        for (PaymentGateway gw : gateways) {
            var result = gw.process(PaymentGateway.ChargeRequest.of(AMOUNT, "USD", "cust-1"));
            assertThat(result.success()).isTrue();
        }
    }
}

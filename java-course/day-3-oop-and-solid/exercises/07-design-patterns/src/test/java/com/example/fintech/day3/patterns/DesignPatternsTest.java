package com.example.fintech.day3.patterns;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class DesignPatternsTest {

    // ─── Strategy Pattern ─────────────────────────────────────────────────────

    @Test
    void standardPricingIs2Percent() {
        var strategy = new PricingStrategy.StandardPricingStrategy();
        assertThat(strategy.calculateFee(new BigDecimal("1000"))).isEqualByComparingTo("20.00");
    }

    @Test
    void volumePricingTier1Under10k() {
        // 2%
        var strategy = new PricingStrategy.VolumePricingStrategy();
        assertThat(strategy.calculateFee(new BigDecimal("5000"))).isEqualByComparingTo("100.00");
    }

    @Test
    void volumePricingTier2Under50k() {
        // 1.5%
        var strategy = new PricingStrategy.VolumePricingStrategy();
        assertThat(strategy.calculateFee(new BigDecimal("20000"))).isEqualByComparingTo("300.00");
    }

    @Test
    void volumePricingTier3Above50k() {
        // 1%
        var strategy = new PricingStrategy.VolumePricingStrategy();
        assertThat(strategy.calculateFee(new BigDecimal("100000"))).isEqualByComparingTo("1000.00");
    }

    @Test
    void promoPricingUsesConfiguredRate() {
        var strategy = new PricingStrategy.PromoPricingStrategy(new BigDecimal("0.005"));
        assertThat(strategy.calculateFee(new BigDecimal("1000"))).isEqualByComparingTo("5.00");
    }

    // ─── Builder Pattern ──────────────────────────────────────────────────────

    @Test
    void builderCreatesFullyPopulatedRequest() {
        PaymentRequest req = PaymentRequest.builder()
            .amount(new BigDecimal("250.00"))
            .currency("EUR")
            .merchantId("merch-1")
            .idempotencyKey("idem-key-42")
            .description("Invoice #42")
            .customerId("cust-7")
            .build();

        assertThat(req.getAmount()).isEqualByComparingTo("250.00");
        assertThat(req.getCurrency()).isEqualTo("EUR");
        assertThat(req.getMerchantId()).isEqualTo("merch-1");
        assertThat(req.getIdempotencyKey()).isEqualTo("idem-key-42");
        assertThat(req.getDescription()).isEqualTo("Invoice #42");
        assertThat(req.getCustomerId()).isEqualTo("cust-7");
    }

    @Test
    void builderDefaultsDescriptionToEmpty() {
        PaymentRequest req = PaymentRequest.builder()
            .amount(new BigDecimal("100"))
            .currency("USD")
            .merchantId("m1")
            .idempotencyKey("k1")
            .build();
        assertThat(req.getDescription()).isEmpty();
        assertThat(req.getCustomerId()).isNull();
    }

    @Test
    void builderThrowsWhenAmountMissing() {
        assertThatThrownBy(() ->
            PaymentRequest.builder().currency("USD").merchantId("m1").idempotencyKey("k1").build())
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("amount");
    }

    @Test
    void builderThrowsWhenCurrencyMissing() {
        assertThatThrownBy(() ->
            PaymentRequest.builder().amount(new BigDecimal("100")).merchantId("m1").idempotencyKey("k1").build())
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("currency");
    }

    @Test
    void builderThrowsWhenIdempotencyKeyMissing() {
        assertThatThrownBy(() ->
            PaymentRequest.builder().amount(new BigDecimal("100")).currency("USD").merchantId("m1").build())
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("idempotencyKey");
    }

    // ─── Factory Pattern ──────────────────────────────────────────────────────

    @Test
    void factoryCreatesStripeGateway() {
        GatewayFactory factory = GatewayFactory.defaultFactory();
        GatewayFactory.Gateway gw = factory.create("stripe");
        assertThat(gw.getName()).isEqualTo("Stripe");
    }

    @Test
    void factoryCreatesPayPalGateway() {
        GatewayFactory.Gateway gw = GatewayFactory.defaultFactory().create("paypal");
        assertThat(gw.getName()).isEqualTo("PayPal");
    }

    @Test
    void factoryCreatesCryptoGateway() {
        GatewayFactory.Gateway gw = GatewayFactory.defaultFactory().create("crypto");
        assertThat(gw.getName()).isEqualTo("Crypto");
    }

    @Test
    void factoryIsCaseInsensitive() {
        GatewayFactory.Gateway gw = GatewayFactory.defaultFactory().create("STRIPE");
        assertThat(gw.getName()).isEqualTo("Stripe");
    }

    @Test
    void factoryThrowsForUnknownGateway() {
        assertThatThrownBy(() -> GatewayFactory.defaultFactory().create("monero"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("monero");
    }

    @Test
    void gatewayChargeReturnsTransactionId() {
        GatewayFactory.Gateway gw = GatewayFactory.defaultFactory().create("stripe");
        String txId = gw.charge(new BigDecimal("100"), "USD");
        assertThat(txId).startsWith("stripe-tx-");
    }

    // ─── Observer Pattern ─────────────────────────────────────────────────────

    @Test
    void busNotifiesAllSubscribers() {
        List<String> log = new ArrayList<>();
        PaymentEventBus bus = new PaymentEventBus();
        bus.subscribe(e -> log.add("listener1: " + e));
        bus.subscribe(e -> log.add("listener2: " + e));
        bus.publish(new PaymentEventBus.PaymentCompleted("tx-1", new BigDecimal("100")));
        assertThat(log).hasSize(2).anyMatch(s -> s.startsWith("listener1"))
                                  .anyMatch(s -> s.startsWith("listener2"));
    }

    @Test
    void busDoesNotNotifyUnsubscribedListener() {
        List<String> log = new ArrayList<>();
        PaymentEventBus bus = new PaymentEventBus();
        PaymentEventBus.PaymentListener listener = e -> log.add("called");
        bus.subscribe(listener);
        bus.unsubscribe(listener);
        bus.publish(new PaymentEventBus.PaymentInitiated("tx-1", new BigDecimal("50"), "USD"));
        assertThat(log).isEmpty();
    }

    @Test
    void busCountsListeners() {
        PaymentEventBus bus = new PaymentEventBus();
        assertThat(bus.listenerCount()).isEqualTo(0);
        bus.subscribe(e -> {});
        bus.subscribe(e -> {});
        assertThat(bus.listenerCount()).isEqualTo(2);
    }

    @Test
    void busContinuesNotifyingAfterListenerException() {
        List<String> log = new ArrayList<>();
        PaymentEventBus bus = new PaymentEventBus();
        bus.subscribe(e -> { throw new RuntimeException("boom"); });
        bus.subscribe(e -> log.add("second-called"));
        // Should not throw; second listener should still be called
        bus.publish(new PaymentEventBus.PaymentFailed("tx-1", "Timeout"));
        assertThat(log).containsExactly("second-called");
    }

    @Test
    void busReceivesCorrectEventType() {
        List<PaymentEventBus.PaymentEvent> received = new ArrayList<>();
        PaymentEventBus bus = new PaymentEventBus();
        bus.subscribe(received::add);
        bus.publish(new PaymentEventBus.PaymentInitiated("tx-1", new BigDecimal("100"), "USD"));
        assertThat(received).hasSize(1)
            .first().isInstanceOf(PaymentEventBus.PaymentInitiated.class);
    }
}

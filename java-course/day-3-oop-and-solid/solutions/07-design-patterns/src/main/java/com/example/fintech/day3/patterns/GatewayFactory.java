package com.example.fintech.day3.patterns;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class GatewayFactory {

    public interface Gateway {
        String getName();
        String charge(BigDecimal amount, String currency);
    }

    private final Map<String, Supplier<Gateway>> registry = new HashMap<>();

    public void register(String name, Supplier<Gateway> supplier) {
        registry.put(name.toLowerCase(), supplier);
    }

    public Gateway create(String name) {
        Supplier<Gateway> supplier = registry.get(name.toLowerCase());
        if (supplier == null) throw new IllegalArgumentException("Unknown gateway: " + name);
        return supplier.get();
    }

    public static GatewayFactory defaultFactory() {
        GatewayFactory factory = new GatewayFactory();
        factory.register("stripe", MockStripeGateway::new);
        factory.register("paypal", MockPayPalGateway::new);
        factory.register("crypto", MockCryptoGateway::new);
        return factory;
    }

    public static class MockStripeGateway implements Gateway {
        @Override public String getName() { return "Stripe"; }
        @Override public String charge(BigDecimal amount, String currency) {
            return "stripe-tx-" + UUID.randomUUID();
        }
    }

    public static class MockPayPalGateway implements Gateway {
        @Override public String getName() { return "PayPal"; }
        @Override public String charge(BigDecimal amount, String currency) {
            return "paypal-tx-" + UUID.randomUUID();
        }
    }

    public static class MockCryptoGateway implements Gateway {
        @Override public String getName() { return "Crypto"; }
        @Override public String charge(BigDecimal amount, String currency) {
            return "crypto-tx-" + UUID.randomUUID();
        }
    }
}

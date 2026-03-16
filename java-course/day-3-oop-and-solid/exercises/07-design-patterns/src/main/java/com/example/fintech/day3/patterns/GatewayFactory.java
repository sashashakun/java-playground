package com.example.fintech.day3.patterns;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Exercise 07 — Factory Method Pattern: GatewayFactory
 *
 * Decouples gateway creation from the rest of the codebase.
 * New gateways can be registered without changing existing code (OCP too).
 *
 * Provided interface — no changes needed.
 *
 * TODO 1 — register(String name, Supplier<Gateway> supplier):
 *   Store the factory function in an internal map (case-insensitive key).
 *
 * TODO 2 — create(String name) → Gateway:
 *   Look up the supplier (case-insensitive); call it to get the Gateway.
 *   Throw IllegalArgumentException("Unknown gateway: " + name) if not found.
 *
 * TODO 3 — Implement the three concrete gateways as inner classes:
 *
 *   MockStripeGateway  — getName() → "Stripe";  charge() → "stripe-tx-" + UUID
 *   MockPayPalGateway  — getName() → "PayPal";  charge() → "paypal-tx-" + UUID
 *   MockCryptoGateway  — getName() → "Crypto";  charge() → "crypto-tx-" + UUID
 *
 * TODO 4 — static defaultFactory():
 *   Return a GatewayFactory pre-registered with "stripe", "paypal", "crypto".
 */
public class GatewayFactory {

    /** Minimal gateway interface for this exercise. */
    public interface Gateway {
        String getName();
        String charge(java.math.BigDecimal amount, String currency);
    }

    private final Map<String, Supplier<Gateway>> registry = new HashMap<>();

    public void register(String name, Supplier<Gateway> supplier) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Gateway create(String name) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static GatewayFactory defaultFactory() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // TODO: Add MockStripeGateway, MockPayPalGateway, MockCryptoGateway as static inner classes
}

package com.example.fintech.day3.soliddip;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Exercise 06 — In-Memory implementation of PaymentRepository
 *
 * Perfect for unit tests — no database required.
 *
 * TODO 1 — save(Payment payment):
 *   - Store in an internal Map<String, Payment> keyed by payment.id()
 *   - Upsert semantics (overwrite if exists)
 *
 * TODO 2 — findById(String id):
 *   - Return Optional.ofNullable(map.get(id))
 *
 * TODO 3 — findAll():
 *   - Return a new List containing all payments (insertion order)
 *
 * TODO 4 — delete(String id):
 *   - Remove from map; return true if the key existed, false otherwise
 */
public class InMemoryPaymentRepository implements PaymentRepository {

    private final Map<String, Payment> store = new LinkedHashMap<>();

    @Override
    public void save(Payment payment) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Optional<Payment> findById(String id) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public List<Payment> findAll() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean delete(String id) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

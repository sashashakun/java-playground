package com.example.fintech.day4.dependencyinjection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Exercise 02 — In-Memory Payment Service
 *
 * Annotate this class with @Service so Spring registers it as a bean.
 *
 * TODO 1 — Add @Service annotation to this class
 *
 * TODO 2 — create(CreatePaymentRequest request):
 *   - Generate UUID id
 *   - Build Payment(id, request.amount(), request.currency(), request.description(),
 *                    "PENDING", Instant.now())
 *   - Store in the map and return
 *
 * TODO 3 — findById(String id):
 *   - Return Optional.ofNullable(store.get(id))
 *
 * TODO 4 — findAll():
 *   - Return new ArrayList<>(store.values())
 *
 * TODO 5 — delete(String id):
 *   - Return store.remove(id) != null
 */
// TODO 1: Add @Service here
public class InMemoryPaymentService implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(InMemoryPaymentService.class);

    private final Map<String, Payment> store = new ConcurrentHashMap<>();

    @Override
    public Payment create(CreatePaymentRequest request) {
        log.info("Processing payment: amount={}, currency={}", request.amount(), request.currency());
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Optional<Payment> findById(String id) {
        log.debug("Payment not found: id={}", id);
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

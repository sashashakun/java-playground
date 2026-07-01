package com.example.fintech.day4.restcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Exercise 01 — REST Controller Basics
 *
 * Implement a CRUD payment controller backed by an in-memory ConcurrentHashMap.
 * No service layer yet — just get comfortable with Spring Web annotations.
 *
 * Endpoints:
 *   GET    /api/payments          → 200, list of all payments
 *   GET    /api/payments/{id}     → 200 with payment, or 404
 *   POST   /api/payments          → 201 Created with Location header, body = new payment
 *   DELETE /api/payments/{id}     → 204 No Content, or 404 if not found
 *
 * TODO 1 — getAll():
 *   Return ResponseEntity.ok(store.values())
 *   — returns all values in the map as a Collection
 *
 * TODO 2 — getById(String id):
 *   Look up in store. If present → 200; if absent → 404.
 *   Use: store.containsKey(id) ? ResponseEntity.ok(store.get(id)) : ResponseEntity.notFound().build()
 *
 * TODO 3 — create(CreatePaymentRequest request):
 *   1. Generate a UUID id
 *   2. Build a Payment record with status "PENDING" and Instant.now()
 *   3. Store it in the map
 *   4. Build a Location URI using:
 *        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
 *            .path("/{id}").buildAndExpand(payment.id()).toUri();
 *   5. Return ResponseEntity.created(location).body(payment)
 *
 * TODO 4 — delete(String id):
 *   If store.remove(id) != null → ResponseEntity.noContent().build()
 *   Else → ResponseEntity.notFound().build()
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final ConcurrentHashMap<String, Payment> store = new ConcurrentHashMap<>();

    @GetMapping
    public ResponseEntity<Collection<Payment>> getAll() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable String id) {
        log.debug("Fetching payment: id={}", id);
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody CreatePaymentRequest request) {
        log.info("Creating payment: amount={}, currency={}", request.amount(), request.currency());
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

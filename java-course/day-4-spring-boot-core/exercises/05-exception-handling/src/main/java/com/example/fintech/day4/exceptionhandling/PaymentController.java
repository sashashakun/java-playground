package com.example.fintech.day4.exceptionhandling;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Exercise 05 — Controller that throws custom exceptions
 *
 * Provided — no changes needed.
 * The GlobalExceptionHandler converts these exceptions to ProblemDetail responses.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final BigDecimal MAX_AMOUNT = new BigDecimal("50000");
    private final ConcurrentHashMap<String, Payment> store = new ConcurrentHashMap<>();
    private final Set<String> usedIdempotencyKeys = new HashSet<>();

    @GetMapping
    public ResponseEntity<Collection<Payment>> getAll() {
        return ResponseEntity.ok(store.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable String id) {
        if (!store.containsKey(id)) throw new PaymentExceptions.PaymentNotFoundException(id);
        return ResponseEntity.ok(store.get(id));
    }

    @PostMapping
    public ResponseEntity<Payment> create(@Valid @RequestBody CreatePaymentRequest request) {
        if (usedIdempotencyKeys.contains(request.idempotencyKey())) {
            throw new PaymentExceptions.DuplicatePaymentException(request.idempotencyKey());
        }
        if (request.amount().compareTo(MAX_AMOUNT) > 0) {
            throw new PaymentExceptions.PaymentLimitExceededException(MAX_AMOUNT, request.amount());
        }
        Payment payment = new Payment(
            UUID.randomUUID().toString(),
            request.amount(), request.currency(), request.description(), "PENDING", Instant.now()
        );
        store.put(payment.id(), payment);
        usedIdempotencyKeys.add(request.idempotencyKey());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").buildAndExpand(payment.id()).toUri();
        return ResponseEntity.created(location).body(payment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (store.remove(id) == null) throw new PaymentExceptions.PaymentNotFoundException(id);
        return ResponseEntity.noContent().build();
    }
}

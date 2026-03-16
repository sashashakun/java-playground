package com.example.fintech.day4.beanvalidation;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Exercise 03 — Bean Validation in a Controller
 *
 * The POST endpoint uses @Valid to trigger Jakarta validation on the request body.
 * When validation fails, Spring throws MethodArgumentNotValidException,
 * which the ValidationAdvice handles.
 *
 * TODO: Add @Valid before @RequestBody in the create() method.
 * The annotation @Valid is already imported — just add it.
 *
 * Everything else is provided — focus on create() and the ValidationAdvice.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final ConcurrentHashMap<String, Payment> store = new ConcurrentHashMap<>();

    @GetMapping
    public ResponseEntity<Collection<Payment>> getAll() {
        return ResponseEntity.ok(store.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable String id) {
        return store.containsKey(id)
            ? ResponseEntity.ok(store.get(id))
            : ResponseEntity.notFound().build();
    }

    /**
     * TODO: Add @Valid before @RequestBody to enable validation.
     * Without it, the constraints on CreatePaymentRequest are ignored.
     */
    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody CreatePaymentRequest request) {
        Payment payment = new Payment(
            UUID.randomUUID().toString(),
            request.amount(),
            request.currency(),
            request.description(),
            "PENDING",
            Instant.now()
        );
        store.put(payment.id(), payment);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").buildAndExpand(payment.id()).toUri();
        return ResponseEntity.created(location).body(payment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return store.remove(id) != null
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}

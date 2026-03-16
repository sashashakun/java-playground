package com.example.fintech.day4.dependencyinjection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Exercise 02 — Dependency Injection
 *
 * This controller delegates to PaymentService.
 * Note: NO @Autowired needed — Spring injects via the single constructor automatically.
 *
 * TODO 1 — Add @RestController and @RequestMapping("/api/payments") annotations
 *
 * TODO 2 — Implement the constructor that accepts PaymentService
 *   and stores it in a final field (constructor injection pattern)
 *
 * TODO 3 — Implement getAll():   @GetMapping → ResponseEntity.ok(service.findAll())
 *
 * TODO 4 — Implement getById():  @GetMapping("/{id}") → 200 or 404
 *
 * TODO 5 — Implement create():   @PostMapping → 201 with Location header
 *   Build the location URI:
 *     URI location = ServletUriComponentsBuilder.fromCurrentRequest()
 *         .path("/{id}").buildAndExpand(payment.id()).toUri();
 *
 * TODO 6 — Implement delete():   @DeleteMapping("/{id}") → 204 or 404
 */
// TODO 1: Add Spring Web annotations here
public class PaymentController {

    // TODO 2: Add private final field + constructor

    @GetMapping
    public ResponseEntity<List<Payment>> getAll() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable String id) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody CreatePaymentRequest request) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

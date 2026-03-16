package com.example.fintech.day4.dependencyinjection;

import java.util.List;
import java.util.Optional;

/**
 * Exercise 02 — Service Interface (Provided — no changes needed)
 *
 * The controller depends on THIS interface, not on any concrete implementation.
 * This enables:
 *   1. Easy testing (inject a mock)
 *   2. Easy swapping (InMemory → database without changing the controller)
 */
public interface PaymentService {

    Payment create(CreatePaymentRequest request);

    Optional<Payment> findById(String id);

    List<Payment> findAll();

    boolean delete(String id);
}

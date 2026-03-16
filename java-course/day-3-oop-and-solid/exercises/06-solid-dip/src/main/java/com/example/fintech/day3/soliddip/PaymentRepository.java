package com.example.fintech.day3.soliddip;

import java.util.List;
import java.util.Optional;

/**
 * Exercise 06 — DIP: Repository abstraction
 *
 * High-level code depends on THIS interface, not on any concrete storage.
 *
 * Provided — no changes needed.
 */
public interface PaymentRepository {

    void save(Payment payment);

    Optional<Payment> findById(String id);

    List<Payment> findAll();

    boolean delete(String id);
}

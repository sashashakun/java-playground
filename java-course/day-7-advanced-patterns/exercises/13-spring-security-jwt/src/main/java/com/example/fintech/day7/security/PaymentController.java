package com.example.fintech.day7.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    public record Payment(String id, BigDecimal amount, String currency, String status) {}

    private static final List<Payment> PAYMENTS = List.of(
            new Payment("pay-1", new BigDecimal("120.00"), "USD", "COMPLETED"),
            new Payment("pay-2", new BigDecimal("54.30"), "EUR", "PENDING"),
            new Payment("pay-3", new BigDecimal("990.99"), "GBP", "COMPLETED"));

    /** Any authenticated user with ROLE_USER may list payments. */
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<Payment> list() {
        return PAYMENTS;
    }

    /** Destructive operations require ROLE_ADMIN — a plain USER token gets 403. */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return ResponseEntity.noContent().build();
    }
}

package com.example.fintech.day5.transactions;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository repo;

    public PaymentService(PaymentRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Payment createPayment(BigDecimal amount, String currency,
                                 String merchantId, String description,
                                 String idempotencyKey) {
        if (repo.existsByIdempotencyKey(idempotencyKey)) {
            throw new DuplicateKeyException("Idempotency key already used: " + idempotencyKey);
        }
        Money money = new Money(amount, currency);
        return repo.save(new Payment(money, merchantId, description, idempotencyKey));
    }

    @Transactional(readOnly = true)
    public List<Payment> findAll() {
        return repo.findAll();
    }

    @Transactional
    public Payment completePayment(String paymentId) {
        Payment payment = repo.findById(paymentId)
            .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        payment.setStatus(PaymentStatus.COMPLETED);
        return repo.save(payment);
    }

    @Transactional
    public List<Payment> batchApprove(List<String> paymentIds) {
        return paymentIds.stream()
            .map(id -> repo.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id)))
            .peek(p -> p.setStatus(PaymentStatus.PROCESSING))
            .map(repo::save)
            .toList();
    }

    @Transactional(readOnly = true)
    public Optional<Payment> findById(String id) {
        return repo.findById(id);
    }

    public static class PaymentNotFoundException extends RuntimeException {
        public PaymentNotFoundException(String id) {
            super("Payment not found: " + id);
        }
    }

    public static class DuplicateKeyException extends RuntimeException {
        public DuplicateKeyException(String message) {
            super(message);
        }
    }
}

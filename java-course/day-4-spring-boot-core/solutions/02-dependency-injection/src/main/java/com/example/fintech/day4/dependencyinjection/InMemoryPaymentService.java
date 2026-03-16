package com.example.fintech.day4.dependencyinjection;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryPaymentService implements PaymentService {

    private final Map<String, Payment> store = new ConcurrentHashMap<>();

    @Override
    public Payment create(CreatePaymentRequest request) {
        Payment payment = new Payment(
            UUID.randomUUID().toString(),
            request.amount(), request.currency(), request.description(), "PENDING", Instant.now()
        );
        store.put(payment.id(), payment);
        return payment;
    }

    @Override
    public Optional<Payment> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Payment> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean delete(String id) {
        return store.remove(id) != null;
    }
}

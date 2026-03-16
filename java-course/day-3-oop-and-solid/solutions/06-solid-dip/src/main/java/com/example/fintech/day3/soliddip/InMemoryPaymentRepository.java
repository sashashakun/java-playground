package com.example.fintech.day3.soliddip;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryPaymentRepository implements PaymentRepository {

    private final Map<String, Payment> store = new LinkedHashMap<>();

    @Override
    public void save(Payment payment) { store.put(payment.id(), payment); }

    @Override
    public Optional<Payment> findById(String id) { return Optional.ofNullable(store.get(id)); }

    @Override
    public List<Payment> findAll() { return new ArrayList<>(store.values()); }

    @Override
    public boolean delete(String id) { return store.remove(id) != null; }
}

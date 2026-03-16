package com.example.fintech.day4.mockmvc;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Payment create(CreatePaymentRequest request);
    Optional<Payment> findById(String id);
    List<Payment> findAll();
    boolean delete(String id);
}

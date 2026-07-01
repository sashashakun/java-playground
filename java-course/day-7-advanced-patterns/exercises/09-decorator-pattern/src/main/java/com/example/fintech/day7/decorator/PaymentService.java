package com.example.fintech.day7.decorator;

public interface PaymentService {
    String processPayment(String merchantId, double amount, String currency);
}

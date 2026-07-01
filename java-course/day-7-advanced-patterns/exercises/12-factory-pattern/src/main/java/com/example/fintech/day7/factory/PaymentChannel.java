package com.example.fintech.day7.factory;

public interface PaymentChannel {
    PaymentValidator validator();
    PaymentProcessor processor();
}

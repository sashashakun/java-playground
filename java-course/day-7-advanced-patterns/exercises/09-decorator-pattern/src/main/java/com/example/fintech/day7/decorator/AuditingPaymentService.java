package com.example.fintech.day7.decorator;

public class AuditingPaymentService implements PaymentService {
    private final PaymentService delegate;

    public AuditingPaymentService(PaymentService delegate) {
        this.delegate = delegate;
    }

    @Override
    public String processPayment(String merchantId, double amount, String currency) {
        System.out.println("[AUDIT] Processing payment: merchant=" + merchantId
                + " amount=" + amount + " " + currency);
        String result = delegate.processPayment(merchantId, amount, currency);
        System.out.println("[AUDIT] Payment result: " + result);
        return result;
    }
}

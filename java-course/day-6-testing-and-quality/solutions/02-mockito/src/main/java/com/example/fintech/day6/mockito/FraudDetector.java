package com.example.fintech.day6.mockito;

import java.math.BigDecimal;

/** Checks whether a transaction looks suspicious. */
public interface FraudDetector {
    boolean isSuspicious(String userId, BigDecimal amount, String currency);
}

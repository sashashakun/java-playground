package com.example.fintech.capstone.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String accountId, BigDecimal balance, BigDecimal requested) {
        super("Insufficient funds in account %s: balance=%s, requested=%s"
            .formatted(accountId, balance, requested));
    }
}

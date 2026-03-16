package com.example.fintech.day4.beanvalidation;

import java.math.BigDecimal;
import java.time.Instant;

public record Payment(String id, BigDecimal amount, String currency,
                      String description, String status, Instant createdAt) {}

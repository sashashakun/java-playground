package com.example.fintech.capstone.web.dto;

import com.example.fintech.capstone.domain.Account;
import com.example.fintech.capstone.domain.AccountStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record AccountResponse(
    String id,
    String ownerId,
    String currency,
    BigDecimal balance,
    AccountStatus status,
    Instant createdAt
) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(
            account.getId(),
            account.getOwnerId(),
            account.getCurrency(),
            account.getBalance(),
            account.getStatus(),
            account.getCreatedAt()
        );
    }
}

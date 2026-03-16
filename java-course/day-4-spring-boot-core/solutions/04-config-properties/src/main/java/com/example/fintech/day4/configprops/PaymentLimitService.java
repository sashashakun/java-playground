package com.example.fintech.day4.configprops;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PaymentLimitService {

    private final PaymentConfig config;

    public PaymentLimitService(PaymentConfig config) {
        this.config = config;
    }

    public boolean isWithinLimit(BigDecimal amount) {
        return amount.compareTo(config.getMaxAmount()) <= 0;
    }

    public boolean isSupportedCurrency(String currency) {
        return config.getSupportedCurrencies().contains(currency);
    }

    public BigDecimal calculateFee(BigDecimal amount) {
        return amount.multiply(config.getDefaultFeeRate()).setScale(2, RoundingMode.HALF_EVEN);
    }
}

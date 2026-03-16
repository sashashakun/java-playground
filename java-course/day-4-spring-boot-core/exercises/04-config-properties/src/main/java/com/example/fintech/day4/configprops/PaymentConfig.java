package com.example.fintech.day4.configprops;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;

/**
 * Exercise 04 — Configuration Properties
 *
 * Bind values from application.yml (prefix "payment") to this class.
 *
 * TODO 1 — Add @ConfigurationProperties(prefix = "payment")
 *
 * TODO 2 — Add @Validated so Spring validates this bean on startup.
 *   If a required field is missing from application.yml, the app fails fast.
 *
 * TODO 3 — The fields are already declared. Add validation annotations:
 *   - maxAmount:              @NotNull @DecimalMin("0.01")
 *   - supportedCurrencies:   @NotEmpty
 *   - defaultFeeRate:        @NotNull @DecimalMin("0")
 *   - gatewayUrl:            @NotBlank
 *
 * TODO 4 — For @ConfigurationProperties with mutable fields, you need
 *   either a regular class with getters+setters, or use a Java record.
 *   This class uses setters — add the missing setters at the bottom.
 *
 * Application.yml structure (already provided in resources/):
 *   payment:
 *     max-amount: 50000.00
 *     supported-currencies: [USD, EUR, GBP, CHF]
 *     default-fee-rate: 0.015
 *     gateway-url: "https://api.example-gateway.com/v1"
 */
// TODO 1: @ConfigurationProperties(prefix = "payment")
// TODO 2: @Validated
public class PaymentConfig {

    // TODO 3: add validation annotations
    private BigDecimal maxAmount;
    private List<String> supportedCurrencies;
    private BigDecimal defaultFeeRate;
    private String gatewayUrl;

    // Getters (provided)
    public BigDecimal getMaxAmount()              { return maxAmount; }
    public List<String> getSupportedCurrencies()  { return supportedCurrencies; }
    public BigDecimal getDefaultFeeRate()         { return defaultFeeRate; }
    public String getGatewayUrl()                 { return gatewayUrl; }

    // TODO 4: Add setters (Spring needs them for binding)
    // setMaxAmount, setSupportedCurrencies, setDefaultFeeRate, setGatewayUrl
}

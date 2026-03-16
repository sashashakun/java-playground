package com.example.fintech.day4.configprops;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;

@ConfigurationProperties(prefix = "payment")
@Validated
public class PaymentConfig {

    @NotNull @DecimalMin("0.01") private BigDecimal maxAmount;
    @NotEmpty private List<String> supportedCurrencies;
    @NotNull @DecimalMin("0") private BigDecimal defaultFeeRate;
    @NotBlank private String gatewayUrl;

    public BigDecimal getMaxAmount()             { return maxAmount; }
    public List<String> getSupportedCurrencies() { return supportedCurrencies; }
    public BigDecimal getDefaultFeeRate()        { return defaultFeeRate; }
    public String getGatewayUrl()                { return gatewayUrl; }

    public void setMaxAmount(BigDecimal maxAmount)                       { this.maxAmount = maxAmount; }
    public void setSupportedCurrencies(List<String> supportedCurrencies) { this.supportedCurrencies = supportedCurrencies; }
    public void setDefaultFeeRate(BigDecimal defaultFeeRate)             { this.defaultFeeRate = defaultFeeRate; }
    public void setGatewayUrl(String gatewayUrl)                         { this.gatewayUrl = gatewayUrl; }
}

package com.example.fintech.day5.customqueries;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Money {

    @Column(name = "amount", precision = 19, scale = 4, nullable = false)
    private BigDecimal value;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    protected Money() {}

    public Money(BigDecimal value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public BigDecimal getValue()  { return value; }
    public String getCurrency()   { return currency; }
}

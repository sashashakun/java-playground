package com.example.fintech.day7.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// SOLUTION 05 — Custom Validation: @ValidCurrency annotation

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyValidator.class)      // TODO 1 ✓
public @interface ValidCurrency {

    String message() default "Invalid ISO-4217 currency code";  // TODO 2 ✓

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package com.example.fintech.capstone.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Capstone Exercise M — Validation: @ValidCurrency annotation
 *
 * TODO M1: Add @Constraint(validatedBy = CurrencyValidator.class) to this annotation.
 * TODO M2: Set the default message to "Invalid ISO-4217 currency code".
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
// TODO M1: @Constraint(validatedBy = CurrencyValidator.class)
public @interface ValidCurrency {

    // TODO M2: String message() default "Invalid ISO-4217 currency code";
    String message() default "must be a valid currency";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

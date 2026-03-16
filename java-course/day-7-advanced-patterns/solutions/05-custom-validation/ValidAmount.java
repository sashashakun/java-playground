package com.example.fintech.day7.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// SOLUTION 05 — Custom Validation: @ValidAmount annotation

@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AmountValidator.class)        // TODO 3 ✓
public @interface ValidAmount {

    String message() default "Amount must be positive and within allowed range for currency";  // TODO 4 ✓

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

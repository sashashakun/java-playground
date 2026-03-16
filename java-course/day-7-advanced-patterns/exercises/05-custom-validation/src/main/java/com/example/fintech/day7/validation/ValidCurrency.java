package com.example.fintech.day7.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Exercise 05 — Custom Validation: Annotation
 *
 * Jakarta Bean Validation lets you define custom constraints.
 * Three parts:
 *   1. The annotation (this file)
 *   2. The ConstraintValidator implementation (CurrencyValidator.java)
 *   3. Usage on a bean field or parameter
 *
 * TypeScript analogy: Joi / Zod / class-validator @IsISO4217Currency()
 *
 * The @Constraint(validatedBy = ...) links this annotation to its validator.
 * message(), groups(), payload() are required by the spec.
 *
 * TODO 1: Fill in @Constraint(validatedBy = ...) pointing to CurrencyValidator.class
 * TODO 2: Set message = "Invalid ISO-4217 currency code"
 *         (groups and payload are already provided below — don't remove them)
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
// TODO 1: @Constraint(validatedBy = CurrencyValidator.class)
public @interface ValidCurrency {

    // TODO 2: String message() default "Invalid ISO-4217 currency code";
    String message() default "must be a valid currency";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

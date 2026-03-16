package com.example.fintech.day7.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Exercise 05 — Custom Validation: Class-level annotation
 *
 * Class-level constraints validate the whole object (cross-field validation).
 * @Target must include ElementType.TYPE.
 * The validator receives the entire PaymentRequest and can check combinations.
 *
 * TypeScript analogy: Joi .when() rules, Zod .superRefine()
 *
 * TODO 3: Fill in @Constraint(validatedBy = ...) pointing to AmountValidator.class
 * TODO 4: Set message = "Amount must be positive and within allowed range for currency"
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
// TODO 3: @Constraint(validatedBy = AmountValidator.class)
public @interface ValidAmount {

    // TODO 4: String message() default "Amount must be positive and within allowed range for currency";
    String message() default "must be a valid amount";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

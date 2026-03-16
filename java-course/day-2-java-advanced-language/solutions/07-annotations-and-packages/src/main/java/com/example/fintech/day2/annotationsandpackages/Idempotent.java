package com.example.fintech.day2.annotationsandpackages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Idempotent {
    String keyHeader() default "Idempotency-Key";
    int ttlSeconds() default 86400;
}

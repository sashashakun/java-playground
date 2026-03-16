package com.example.fintech.day5.repositories;

import java.math.BigDecimal;
import java.util.List;

/**
 * Exercise 02 — Spring Data Repositories
 *
 * Spring Data generates the implementation automatically at startup — no SQL needed.
 * You only declare the interface. Spring reads the method name and builds a query.
 *
 * TypeScript analogy: like Prisma generating type-safe finder methods from your schema,
 * but instead of generated code you just declare a method signature and Spring infers
 * the SQL from the name.
 *
 * TODO 1: Extend JpaRepository<Payment, String>
 *         This gives you free CRUD: save(), findById(), findAll(), delete(), count()…
 *
 * TODO 2: Add a derived query method that finds all payments with a given status.
 *         Method name must follow the Spring Data naming convention.
 *         Hint: findBy + field name. Return type: List<Payment>.
 *
 * TODO 3: Add a derived query method that finds payments for a specific merchant.
 *         Field name in Payment is `merchantId`.
 *
 * TODO 4: Add a derived query method that finds payments whose amount value
 *         is greater than a given threshold.
 *         The field path is: amount (embedded) → value.
 *         Hint: Spring Data traverses nested properties with underscore or camelCase:
 *               findByAmount_ValueGreaterThan(BigDecimal threshold)
 *
 * TODO 5: Add a derived query method that returns true/false if an
 *         idempotency key already exists.
 *         Return type: boolean. Hint: existsBy + fieldName.
 *
 * TODO 6: Add a derived query that finds all payments for a merchant, ordered
 *         by createdAt descending.
 *         Hint: …OrderByCreatedAtDesc
 */
public interface PaymentRepository {

    // TODO: method stubs go here

}

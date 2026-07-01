# Day 5 — Data Layer: Spring Data JPA

## Overview

Today you connect Spring Boot applications to a relational database using **JPA** (Jakarta Persistence API)
and **Spring Data**. You will model domain entities, write repository interfaces, build custom queries,
manage transactions, and paginate large result sets — all without boilerplate SQL.

**Target audience**: Senior TypeScript/Node.js engineers. Comparisons to Prisma, TypeORM, Drizzle, and
Knex are included where helpful.

---

## Prerequisites

- Completed Day 4 (Spring Boot Core)
- Gradle 8.x, Java 21, IntelliJ IDEA

---

## Learning Objectives

By the end of Day 5 you will be able to:

1. Map Java classes to database tables with JPA annotations
2. Use `@Embeddable` / `@Embedded` to model value objects
3. Write a `JpaRepository` interface with zero boilerplate CRUD
4. Define derived query methods using Spring Data naming conventions
5. Write JPQL and native SQL with `@Query`
6. Model `@OneToMany` / `@ManyToOne` bidirectional relationships
7. Control transactions with `@Transactional`, isolation levels, and read-only hints
8. Return paginated results with `Pageable` and `Page<T>`
9. Test the data layer in isolation with `@DataJpaTest`

---

## TypeScript ↔ Java Mapping

| TypeScript / Node.js              | Java / Spring Data JPA                        |
|-----------------------------------|-----------------------------------------------|
| Prisma schema `model Payment {}`  | `@Entity class Payment {}`                    |
| Drizzle `pgTable` definition      | `@Entity` + `@Table` + `@Column`              |
| `prisma.payment.findMany()`       | `repository.findAll()`                        |
| Prisma `where:` clause            | Derived query method or `@Query`              |
| Prisma `include:` (eager load)    | `FetchType.EAGER` / `JOIN FETCH`              |
| TypeORM `@OneToMany`              | `@OneToMany(mappedBy=…, cascade=…)`           |
| Knex `.transacting(trx)`         | `@Transactional` (declarative AOP)            |
| Prisma `findMany({ skip, take })` | `repository.findAll(PageRequest.of(…))`       |
| Jest + TypeORM in-memory DB       | `@DataJpaTest` (auto-configures H2)           |

---

## Exercises

| # | Topic | Concept |
|---|-------|---------|
| 01 | JPA Entities | `@Entity`, `@Embeddable`, `@Column`, `@Enumerated` |
| 02 | Repositories | `JpaRepository`, derived query methods |
| 03 | Custom Queries | `@Query` (JPQL + native), projections, `@Modifying` |
| 04 | Relationships | `@OneToMany`, `@ManyToOne`, cascades, fetch types |
| 05 | Transactions | `@Transactional`, isolation, rollback, read-only |
| 06 | Pagination & Sorting | `Pageable`, `Page<T>`, `Slice<T>`, `Sort` |
| 07 | DataJpaTest | `@DataJpaTest`, test slices, assertion patterns |

---

## Project Structure

Each exercise is an independent Gradle project:

```
exercises/01-jpa-entities/
├── build.gradle.kts
├── settings.gradle.kts
└── src/
    ├── main/java/com/example/fintech/day5/entities/
    │   ├── Payment.java        ← annotate this
    │   ├── Money.java          ← annotate this
    │   ├── PaymentStatus.java
    │   └── Day5Application.java
    ├── main/resources/application.properties
    └── test/java/com/example/fintech/day5/entities/
        └── PaymentEntityTest.java
```

Open any single exercise folder in IntelliJ as a Gradle project.
Run tests: `./gradlew test` from within the exercise directory.

---

## Running an Exercise

```bash
cd exercises/01-jpa-entities
./gradlew test          # run tests
./gradlew bootRun       # start app (H2 in-memory)
```

H2 console (when running): `http://localhost:8080/h2-console`
JDBC URL: `jdbc:h2:mem:fintechdb`

---

## Exercise 01 — Flyway: Schema Migration vs DDL Auto

### Flyway: Schema Migration vs DDL Auto

| Approach | How schema is created | Good for |
|---|---|---|
| `ddl-auto=create-drop` | Hibernate creates/drops on startup | Local dev + testing only |
| `ddl-auto=validate` + Flyway | Flyway runs `V1__*.sql` migrations | Staging + Production |
| `ddl-auto=none` + Flyway | Same, but no validation | When schema is managed externally |

**Rule**: Use Flyway in production. Use `create-drop` for throw-away test databases.

---

## Exercise 05 — Transactions

### Transaction Isolation Levels in Fintech

| Level | Prevents | Risk | When to use |
|---|---|---|---|
| `READ_UNCOMMITTED` | Nothing | Dirty reads, phantom reads | Never in fintech |
| `READ_COMMITTED` | Dirty reads | Non-repeatable reads | Default for most reads |
| `REPEATABLE_READ` | Dirty + non-repeatable reads | Phantom reads | Balance queries under load |
| `SERIALIZABLE` | Everything | Performance (full locking) | Concurrent balance updates, idempotency checks |

**Fintech rule**: For concurrent balance deductions (two transfers at the same millisecond),
use `SERIALIZABLE` or optimistic locking (`@Version`) to prevent double-spend. Never rely
on `READ_COMMITTED` for balance-altering operations.

---

## Key JPA Annotations Quick Reference

```java
@Entity                         // marks class as JPA entity
@Table(name = "payments")       // customise table name / indexes
@Id                             // primary key
@GeneratedValue(strategy = GenerationType.UUID)   // auto-assign UUID
@Column(nullable = false, unique = true, length = 3)
@Enumerated(EnumType.STRING)    // store enum name, not ordinal
@Embeddable / @Embedded         // inline value object (like Prisma type)
@ManyToOne(fetch = FetchType.LAZY)
@OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
@JoinColumn(name = "merchant_id")
@Transient                      // field NOT persisted
```

# Day 7 — Advanced Patterns

## Overview

Production Spring Boot applications go well beyond CRUD. Today covers cross-cutting patterns
used in real fintech systems: event-driven decoupling, caching, retry/resilience, async processing,
custom validation, dynamic JPA queries, and transactional event delivery.

---

## Learning Objectives

1. Decouple business logic with Spring Application Events and `ApplicationEventPublisher`
2. Add caching with `@Cacheable`, `@CacheEvict`, `@CachePut`
3. Make HTTP calls resilient with `@Retryable` and `@Recover` (Spring Retry)
4. Run work off the main thread with `@Async` and a custom `ThreadPoolTaskExecutor`
5. Write reusable domain-specific constraints with custom `@Constraint` annotations
6. Build dynamic database queries with JPA `Specification<T>`
7. Publish events reliably (only after commit) using `@TransactionalEventListener`

---

## TypeScript ↔ Java Mapping

| TypeScript / Node.js                   | Java / Spring                                   |
|----------------------------------------|-------------------------------------------------|
| EventEmitter / custom bus              | `ApplicationEventPublisher` + `@EventListener`  |
| Redis/node-cache                       | `@Cacheable` / `@CacheEvict`                    |
| `retry` npm package                    | `@Retryable` (Spring Retry)                     |
| Worker threads / BullMQ               | `@Async` + `ThreadPoolTaskExecutor`             |
| `zod` custom `.refine()` validator    | `@Constraint` + `ConstraintValidator<T>`        |
| Prisma `where: { AND: [...] }`         | `Specification<T>.and(spec)`                    |
| DB-backed outbox pattern               | `@TransactionalEventListener(AFTER_COMMIT)`     |

---

## Exercises

| # | Topic | Key API |
|---|-------|---------|
| 01 | Spring Events | `ApplicationEventPublisher`, `@EventListener`, `@TransactionalEventListener` |
| 02 | Caching | `@Cacheable`, `@CacheEvict`, `@CachePut`, `@EnableCaching` |
| 03 | Spring Retry | `@Retryable`, `@Recover`, `@EnableRetry`, `@Backoff` |
| 04 | Async Processing | `@Async`, `@EnableAsync`, `ThreadPoolTaskExecutor`, `CompletableFuture` |
| 05 | Custom Validation | `@Constraint`, `ConstraintValidator<A, T>`, `@ValidCurrency`, `@ValidAmount` |
| 06 | JPA Specifications | `Specification<T>`, `JpaSpecificationExecutor`, `CriteriaBuilder` |
| 07 | Transactional Events | `@TransactionalEventListener`, outbox pattern, rollback safety |

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
| 08 | Builder Pattern | Fluent builder, validation in `build()`, immutable value objects |
| 09 | Decorator Pattern | Wrapping interfaces, audit/logging cross-cuts |
| 10 | Strategy Pattern | `@FunctionalInterface` strategy, runtime-swappable algorithms |
| 11 | Observer Pattern | Typed event bus, `sealed` interfaces, exhaustive dispatch |
| 12 | Abstract Factory | Factory returning paired validator + processor per channel |

---

## 🔀 Paradigm Comparison — GOF Patterns

### Exercise 08 — Builder

| Java OOP | TypeScript Functional |
|---|---|
| `PaymentRequestBuilder` class with fluent setters | `pipe(withAmount(100), withCurrency("USD"), validate)({})` |
| `build()` validates and throws `IllegalStateException` | `validate` is the last function in the pipe — same semantics |
| Mutable builder accumulates state | Each step returns a new immutable object (spread) |
| Type-safe via Java's type system at compile time | Type-safe via TypeScript's structural typing |

**Verdict**: Java's builder is idiomatic for complex object construction. The TS pipe approach achieves the same result with less ceremony — but Java's IDE tooling (autocompletion on the builder) is a genuine advantage.

---

### Exercise 09 — Decorator

| Java OOP | TypeScript Functional |
|---|---|
| `AuditingPaymentService implements PaymentService` | `const withAudit = (fn: PaymentFn): PaymentFn => ...` |
| Decorators form a class hierarchy | Decorators are just HOFs — stack with composition |
| DI container wires the decorator chain | Manual wrapping: `withAudit(withMetrics(simplePaymentFn))` |
| `instanceof` checks possible on each layer | No class — decoration is transparent |

**Verdict**: The functional decorator is simpler and more composable. Java's class-based decorator is better when you need DI-managed lifecycle or multiple interface methods to decorate.

---

### Exercise 10 — Strategy

| Java OOP | TypeScript Functional |
|---|---|
| `@FunctionalInterface RiskScoringStrategy` | `type RiskStrategy = (amount: number, currency: string) => RiskLevel` |
| `RiskEngine` holds a `RiskScoringStrategy` field | `riskEngine(strategy)` — strategy is just a closure argument |
| Strategies as named lambdas or anonymous classes | Strategies as named arrow functions |
| Runtime injection (Spring `@Qualifier`) | Pass the function directly |

**Verdict**: Strategy is the pattern where Java and TypeScript converge most — both boil down to "pass a function." Java's added ceremony (`@FunctionalInterface`, `@Qualifier`) exists to satisfy the type system and DI container; TS needs neither.

---

### Exercise 11 — Observer

| Java OOP | TypeScript Functional |
|---|---|
| `ComplianceListener` functional interface | `type Handler = (event: ComplianceEvent) => void` |
| `sealed interface ComplianceEvent` + records | Discriminated union: `type ComplianceEvent = TransactionCreated \| TransactionFlagged` |
| `ComplianceEventBus` class with `List<Listener>` | `createEventBus()` factory returns `{subscribe, publish}` closure |
| `instanceof` dispatch in listeners | Exhaustive `switch (event.type)` — TypeScript narrows the type |

**Verdict**: TypeScript's discriminated unions give exhaustiveness checking "for free" via the type system. Java's `sealed` interfaces + `instanceof` pattern matching (Java 21+) achieve the same, but with more syntax. The functional TS approach has less boilerplate.

---

### Exercise 12 — Abstract Factory

| Java OOP | TypeScript Functional |
|---|---|
| `PaymentChannel` interface + two implementing classes | `type PaymentChannel = { validate: ..., process: ... }` object literal |
| `PaymentChannelFactory.create(ChannelType)` static method | `createChannel(type: ChannelType): PaymentChannel` factory function |
| Open/Closed: add channel = add new class | Add channel = add `case` in switch + type to union |
| Java enforces interface contract at compile time | TS structural typing enforces it at compile time |

**Verdict**: The functional approach has less ceremony and the discriminated union ensures every channel is handled. Java's abstract factory shines when channels need full Spring lifecycle management (stateful beans, injected dependencies).

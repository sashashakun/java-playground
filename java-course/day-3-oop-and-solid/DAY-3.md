# Day 3 — OOP and SOLID

**Theme: Payment Processing Engine**

You're building the core of a fintech payment processing engine. Today is about
object-oriented design done _right_ — not just syntax, but the _thinking_
behind abstractions, hierarchies, and the SOLID principles that keep large
codebases maintainable.

---

## What you're building

A pluggable **Payment Processing Engine** composed of:

| Component | Concept |
|-----------|---------|
| `PaymentGateway` hierarchy | Abstract classes + template method |
| `Account` hierarchy | Inheritance + polymorphism |
| `CompositeValidator<T>` | Composition over inheritance |
| `FeeCalculator` with strategies | SRP + OCP |
| Segregated `Payment*` interfaces | LSP + ISP |
| `PaymentOrchestrator` with ports | Dependency Inversion |
| Strategy + Builder + Factory + Observer | Design patterns |

---

## Mental model: Java OOP vs TypeScript

| TypeScript | Java |
|------------|------|
| `abstract class Foo { abstract bar(): void }` | `abstract class Foo { abstract void bar(); }` |
| `interface Foo { bar(): void }` | `interface Foo { void bar(); }` |
| `class X extends Y implements Z` | Same! |
| No abstract fields | Abstract classes CAN have fields |
| Duck typing for interfaces | Explicit `implements` required |
| Constructor overloading via `?` params | Multiple constructors OR builder pattern |

**Key Java-specific rules:**
- A class can `extend` only ONE class (abstract or concrete)
- A class can `implement` multiple interfaces
- Abstract classes CAN have constructors, fields, and implemented methods
- Interfaces (Java 8+) can have `default` and `static` methods
- `final` class = cannot be subclassed; `final` method = cannot be overridden

---

## Setup

```bash
# Open one exercise at a time in IntelliJ
cd exercises/01-abstract-classes-and-interfaces
./gradlew test   # all fail (red) → your job: make them green
```

---

## Exercises

### Exercise 01 — Abstract Classes & Interfaces

**File:** `src/main/java/com/example/fintech/day3/abstractclasses/`

You're defining the contract for payment gateways.

**Theory:**
- `abstract class` = shared behaviour (fields, constructors) + enforced contract
- `interface` = pure contract (what, not how) — can implement many
- Template Method pattern: abstract class defines the _algorithm skeleton_,
  subclasses fill in the _steps_

```
PaymentGateway (abstract)
├── authenticate()          ← abstract — each gateway does it differently
├── submitCharge()          ← abstract
├── process(request)        ← template method — calls authenticate + submitCharge + log
└── getGatewayName()        ← abstract

StripeGateway extends PaymentGateway
PayPalGateway extends PaymentGateway
```

**Interfaces to implement:**
```java
interface Refundable { RefundResult refund(String transactionId, BigDecimal amount); }
interface Verifiable { boolean verify(String transactionId); }
```

**Break it:** Make `StripeGateway` throw `UnsupportedOperationException` from
`refund()`. What design smell does that reveal? (Hint: ISP / Liskov)

---

## 🔀 Paradigm Comparison — Exercise 01: Template Method

| Java OOP | TypeScript Functional |
|---|---|
| Abstract class defines algorithm skeleton | HOF `processPayment(impl)` defines the fixed steps |
| `protected abstract` methods enforced by compiler | `GatewayImpl` interface — TypeScript checks at call site |
| Subclass override = one decision point per class | Passing a different `impl` object = same flexibility |
| Audit log stored on instance | Audit log is a side effect logged in the HOF |

**Compile-time difference**: Java's `abstract` keyword means you literally cannot instantiate `PaymentGateway` — the compiler prevents it. TypeScript has no equivalent; you just don't call `processPayment()` without passing an impl.

**Verdict**: For fintech where the charge algorithm must never be modified (PCI compliance), Java's `final` Template Method is stronger — it's enforced by the type system, not convention. In TypeScript, you'd need a code review or linter rule. OOP wins here.

---

### Exercise 02 — Inheritance & Polymorphism

**File:** `src/main/java/com/example/fintech/day3/inheritance/`

Build a bank account hierarchy that handles polymorphic collections correctly.

```
Account (abstract)
├── balance: BigDecimal
├── deposit(amount): void        ← concrete, shared
├── withdraw(amount): void       ← abstract — different overdraft rules
├── getAccountType(): String     ← abstract
└── getStatement(): String       ← concrete template

CheckingAccount  — allows overdraft up to limit
SavingsAccount   — no overdraft; throws InsufficientFundsException
CryptoAccount    — no overdraft; fees deducted on withdraw
```

**The polymorphism exercise:**
```java
List<Account> accounts = List.of(checking, savings, crypto);
accounts.forEach(a -> a.deposit(new BigDecimal("100")));
// Should work identically for all three — Liskov!
```

**Break it:** Try adding `MoneyMarketAccount` that overrides `deposit()` to
reject amounts < 1000. Does that violate Liskov? Why?

---

## 🔀 Paradigm Comparison — Exercise 02: Polymorphism

| Java OOP | TypeScript Functional |
|---|---|
| `instanceof` checks are a code smell | `switch (account.kind)` is idiomatic and exhaustive |
| Subclass adds `overdraftLimit` field | `CheckingAccount` type includes `overdraftLimit` |
| `account.withdraw(amount)` — behavior on the object | `withdraw(account, amount)` — function over data |
| Adding a new account type = new subclass | Adding a new account type = new union member |

**Compile-time difference**: TypeScript's exhaustive `switch` on a discriminated union gives compile-time errors if you add a new `Account` type and forget to handle it. Java achieves this via `sealed` classes (Java 17+) — but only if you remember to use them.

**Verdict**: Near-equal. For a fixed account taxonomy (no plugins), TS discriminated unions are more concise. For extensible hierarchies (third-party account types), Java's class system is cleaner. In fintech where account types are stable, functional wins on brevity.

---

### Exercise 03 — Composition Over Inheritance

**File:** `src/main/java/com/example/fintech/day3/composition/`

Instead of inheriting from a base `Validator`, _compose_ validators.

**The wrong way (inheritance):**
```
BaseValidator → AmountValidator → AmountAndCurrencyValidator → FullValidator  // deep chain!
```

**The right way (composition):**
```java
Validator<PaymentRequest> chain = Validator.<PaymentRequest>of()
    .and(new AmountValidator())
    .and(new CurrencyValidator(SUPPORTED))
    .and(new DuplicateValidator(seenIds));
```

**Build:**
- `Validator<T>` functional interface: `ValidationResult validate(T input)`
- `ValidationResult` sealed interface: `record Valid()` and `record Invalid(String reason)`
- `AmountValidator` — rejects zero/negative amounts
- `CurrencyValidator` — rejects unsupported currencies (configurable set)
- `DuplicateValidator` — rejects IDs already seen (keeps a `Set<String>`)
- `CompositeValidator<T>` — chains validators, returns first failure

---

## 🔀 Paradigm Comparison — Exercise 03: Composition

| Java OOP | TypeScript Functional |
|---|---|
| `Validator<T>` functional interface | `type Validator<T> = (input: T) => ValidationResult` |
| `and(other)` instance method combinator | `const and = (a, b) => (input) => ...` free function |
| Java `@FunctionalInterface` documents intent | No annotation needed — TS functions are first-class |
| Lambda syntax: `req -> req.amount() > 0 ? ... : ...` | Arrow function: `(req) => req.amount > 0 ? ... : ...` |

**Compile-time difference**: Almost none here. Java and TypeScript are essentially equivalent — this is because the Java exercise is already functional (using a functional interface, not classes). This is the lesson: Java can be functional too.

**Verdict**: This is the exercise where the paradigms converge most. The TS version is slightly more concise (no annotation, no `implements`). Functionally identical — showing students that "functional vs OOP" is a spectrum, not a binary.

---

### Exercise 04 — SOLID: SRP & OCP

**File:** `src/main/java/com/example/fintech/day3/solidsrpocp/`

**SRP — Single Responsibility:**
Start from a bloated `TransactionProcessor` god-class that does everything.
Split it into focused components.

**OCP — Open/Closed:**
Add new fee types _without_ touching existing code via the Strategy pattern.

```java
// Before OCP violation:
if (type == PURCHASE) fee = 0.015;
else if (type == TRANSFER) fee = 0.005;
else if (type == CRYPTO) ...  // touching this file every time!

// After:
FeeStrategy strategy = registry.get(transactionType);
BigDecimal fee = strategy.calculate(amount);
```

**Build:**
- `FeeStrategy` interface: `BigDecimal calculate(BigDecimal amount)`
- `PurchaseFeeStrategy` (1.5%)
- `TransferFeeStrategy` (0.5%)
- `CryptoFeeStrategy` (0.5% min 50¢)
- `ZeroFeeStrategy` (free)
- `FeeStrategyRegistry` — maps `TransactionType` → `FeeStrategy`

---

## 🔀 Paradigm Comparison — Exercise 04: SRP & OCP

| Java OOP | TypeScript Functional |
|---|---|
| `@FunctionalInterface FeeStrategy` | `type FeeStrategy = (amount: number) => number` |
| `FeeStrategyRegistry` class wraps the Map | Plain `Map<TransactionType, FeeStrategy>` |
| `registry.register(type, strategy)` method | `registry.set(type, strategy)` — Map API directly |
| OCP: new strategy = new class + register call | OCP: new strategy = new map entry (one line) |

**Compile-time difference**: Java's `@FunctionalInterface` ensures the interface has exactly one abstract method — the compiler enforces this contract. TypeScript has no equivalent; the type alias just describes a function shape.

**Verdict**: TypeScript wins on brevity here. The registry pattern in Java requires a class wrapper; in TypeScript, a Map literal is sufficient. Both achieve OCP — but the Java ceremony adds ~20 lines for the same result. This is a case where Java's verbosity doesn't pay off.

---

### Exercise 05 — SOLID: LSP & ISP

**File:** `src/main/java/com/example/fintech/day3/solidlspisp/`

**LSP — Liskov Substitution:**
Every subtype must be usable wherever the base type is expected — no surprises.

**ISP — Interface Segregation:**
Don't force clients to depend on methods they don't use. Split fat interfaces.

**Build:**
```java
// BAD — monolithic:
interface PaymentService {
    charge(); refund(); verifyFraud(); generateReport(); archiveOldPayments();
}

// GOOD — segregated:
interface Chargeable  { PaymentResult charge(PaymentRequest r); }
interface Refundable  { RefundResult refund(String txId, BigDecimal amount); }
interface Auditable   { List<PaymentRecord> getAuditLog(LocalDate from, LocalDate to); }

class FullPaymentService implements Chargeable, Refundable, Auditable { ... }
class ReadOnlyAuditService implements Auditable { ... }  // doesn't need charge/refund!
```

**LSP exercise:** `PaymentRequest` → `InternationalPaymentRequest` extends it.
Show that code using `PaymentRequest` still works with `InternationalPaymentRequest`.

---

## 🔀 Paradigm Comparison — Exercise 05: LSP & ISP

| Java OOP | TypeScript Functional |
|---|---|
| `interface Chargeable { ChargeResult charge(...) }` | `type Chargeable = { charge: (amount: number, ...) => string }` |
| `FullPaymentService implements Chargeable, Refundable, Auditable` | `type FullPaymentService = Chargeable & Refundable & Auditable` |
| ISP: explicit `implements` list | ISP: implicit — any object with the right shape qualifies |
| LSP: contract tests verify substitutability | LSP: exhaustive type checks verify structural compatibility |

**Compile-time difference**: Java's nominal typing means you must explicitly declare `implements Chargeable`. TypeScript's structural typing means any object with a `charge` method satisfies `Chargeable` — duck typing at compile time. This is weaker (a bug could accidentally satisfy an interface) but more flexible (third-party types can satisfy your interface without modification).

**Verdict**: For ISP, both approaches work equally well. For LSP in large teams, Java's explicit `implements` makes the intent clearer in code review. For rapid prototyping or third-party integration, TypeScript's structural typing is more pragmatic.

---

### Exercise 06 — SOLID: DIP (Dependency Inversion)

**File:** `src/main/java/com/example/fintech/day3/soliddip/`

High-level policy should NOT depend on low-level details.
Both should depend on **abstractions**.

```
// BAD:
class PaymentOrchestrator {
    private MySqlPaymentRepository db = new MySqlPaymentRepository();  // concrete!
    private SmtpEmailService email = new SmtpEmailService();           // concrete!
}

// GOOD:
class PaymentOrchestrator {
    private final PaymentRepository repository;   // interface
    private final NotificationPort notifier;      // interface
    // injected via constructor
}
```

**Build:**
- `PaymentRepository` interface: `save`, `findById`, `findAll`, `delete`
- `InMemoryPaymentRepository` implements it (for tests)
- `NotificationPort` interface: `notify(PaymentEvent event)`
- `LoggingNotification` (logs to console)
- `PaymentOrchestrator` — only touches interfaces; fully testable

---

## 🔀 Paradigm Comparison — Exercise 06: DIP

| Java OOP | TypeScript Functional |
|---|---|
| Constructor injection: `new PaymentOrchestrator(repo, notify)` | Partial application: `makeOrchestrator(repo, notify)` |
| Spring `@Autowired` handles injection at runtime | Manual wiring (or a FP DI container) |
| Interface `PaymentRepository` separates contract from impl | `type PaymentRepository = { save: ...; findById: ... }` |
| DI container resolves dependencies automatically | Manual composition root required |

**Compile-time difference**: In Java, Spring resolves dependencies at runtime (startup fails if a bean is missing). TypeScript with manual wiring catches missing dependencies at compile time — if you forget to pass `repo`, TypeScript errors immediately.

**Verdict**: For application-scale DI (50+ beans), Spring's IoC container is indispensable — TypeScript's manual wiring doesn't scale. For library or utility code, functional partial application is lighter-weight and more testable (no mocking framework needed). DIP as a principle applies in both paradigms; the implementation mechanism differs.

---

### Exercise 07 — Design Patterns Toolkit

**File:** `src/main/java/com/example/fintech/day3/patterns/`

Four patterns essential for production Java:

**Strategy** — swap algorithms at runtime
```java
PaymentPricer pricer = new PaymentPricer(new VolumePricingStrategy());
BigDecimal fee = pricer.calculateFee(amount, volume);
```

**Builder** — construct complex objects safely
```java
PaymentRequest request = PaymentRequest.builder()
    .amount(new BigDecimal("100.00"))
    .currency("USD")
    .merchantId("merchant-123")
    .idempotencyKey(UUID.randomUUID().toString())
    .build();  // validates required fields
```

**Factory Method** — decouple creation from use
```java
PaymentGatewayFactory factory = new PaymentGatewayFactory();
Gateway gw = factory.create("stripe");  // returns StripeGateway
Gateway gw = factory.create("paypal"); // returns PayPalGateway
```

**Observer** — event-driven processing
```java
PaymentEventBus bus = new PaymentEventBus();
bus.subscribe(new AuditLogger());
bus.subscribe(new FraudAlertService());
bus.publish(new PaymentCompleted("tx-001", new BigDecimal("500")));
// Both listeners are invoked
```

---

## 🔀 Paradigm Comparison — Exercise 07: Design Patterns

| Pattern | Java OOP | TypeScript Functional |
|---|---|---|
| Factory | `GatewayFactory` class with `create(type)` | `createGateway(type)` free function |
| Observer | `PaymentEventBus` class with `List<Listener>` | `makeEventBus()` closure over `listeners[]` |
| Strategy | `PricingStrategy` interface + implementations | `type PricingStrategy = (base, qty) => number` |
| Builder | `PaymentRequest.builder()` with nested class | Chained builder functions returning same builder |

**Compile-time difference**: All four patterns are trivially implementable in both paradigms. Java's class-based approach is more verbose but provides nominal typing (you can `instanceof PaymentEventBus`). TypeScript closures are more concise but opaque — you can't introspect the bus's listener list without exposing it.

**Verdict**: Factory and Strategy patterns are simpler in TypeScript — free functions beat factory classes. Observer is roughly equivalent. The Java patterns add value at scale: a class-based EventBus can be annotated (`@Component`), proxied, monitored with Spring Boot Actuator. For microservices at fintech scale, Java's OOP patterns integrate better with the Spring ecosystem.

---

## Checkpoints

After completing all exercises:

- [ ] Can you explain why Java's single inheritance limit exists?
- [ ] When would you choose an abstract class over an interface?
- [ ] What is the "diamond problem" and how does Java solve it?
- [ ] Which SOLID principle does the Strategy pattern most directly support?
- [ ] Why is constructor injection preferred over field injection for DIP?

---

## TypeScript → Java Translation Corner

```typescript
// TypeScript: abstract class
abstract class PaymentGateway {
    abstract authenticate(): Promise<void>;
    async process(req: PaymentRequest): Promise<void> {
        await this.authenticate();
        // ...
    }
}

// Java equivalent
abstract class PaymentGateway {
    abstract void authenticate();
    final void process(PaymentRequest req) {
        authenticate();
        // ...
    }
}
```

```typescript
// TypeScript: interface (structural / duck typing)
interface Refundable { refund(id: string): void; }
function processRefund(r: Refundable) { r.refund("tx-1"); }
// Any object with .refund() works!

// Java: interface (nominal typing)
interface Refundable { void refund(String id); }
void processRefund(Refundable r) { r.refund("tx-1"); }
// Must explicitly implement Refundable!
```

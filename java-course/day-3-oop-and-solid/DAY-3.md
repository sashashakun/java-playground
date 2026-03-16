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

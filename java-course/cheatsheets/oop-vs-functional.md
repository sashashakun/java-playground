# OOP vs Functional — Decision Framework for Fintech Engineers

> Not a religious war. A practical guide to choosing the right tool for each problem.
> This course is Java, but modern Java borrows heavily from functional programming.
> The best code uses both — intentionally.

---

## The Honest Meta-Answer

Neither paradigm wins universally. The question isn't "OOP or functional?" — it's:

- **Does this thing have identity and mutable state over time?** → OOP class
- **Is this a data transformation with no side effects?** → pure function
- **Does this thing need to be swapped for testing or alternative implementations?** → interface (OOP) OR function parameter (FP)
- **Is this a domain concept that closes over its own logic?** → class
- **Is this a pipeline of transforms on data?** → stream/function composition

---

## 10 Common Fintech Scenarios

### Scenario 1: Fee Calculation

| | OOP | Functional |
|--|-----|------------|
| **Approach** | `FeeStrategy` interface with `PercentageFeeStrategy`, `FlatFeeStrategy`, `TieredFeeStrategy` classes | Map of `feeType → (transaction) => fee` functions, or a plain function with a strategy parameter |
| **Verdict** | ✅ **Use OOP when strategies need Spring DI, configuration from YAML, or separate deployment** | ✅ **Use functional when strategies are data-driven and defined in a config file/DB** |
| **Why** | Strategy + DI = injectable, replaceable, testable with mocks | A function map is 5 lines vs 5 classes; if the strategy is just "multiply by X" it doesn't need a class |

```java
// Java — OOP Strategy (worth it when strategies are complex or Spring beans)
interface FeeStrategy { BigDecimal calculate(Transaction tx); }

@Component("percentage")
class PercentageFeeStrategy implements FeeStrategy {
    @Value("${fees.rate}") private BigDecimal rate;
    public BigDecimal calculate(Transaction tx) { return tx.getAmount().multiply(rate); }
}
```

```typescript
// TypeScript — functional (better when strategies are simple data transforms)
const feeStrategies: Record<FeeType, (tx: Transaction) => BigDecimal> = {
  PERCENTAGE: (tx) => tx.amount.mul(config.feeRate),
  FLAT: (_tx) => new BigDecimal("0.50"),
  TIERED: (tx) => tx.amount.lt("1000") ? new BigDecimal("0.25") : new BigDecimal("1.00"),
};
const calculateFee = (type: FeeType, tx: Transaction) => feeStrategies[type](tx);
```

---

### Scenario 2: Account State Machine

| | OOP | Functional |
|--|-----|------------|
| **Approach** | `Account` class with state field; methods enforce valid transitions | Sealed type + pure transition functions + exhaustive pattern matching |
| **Verdict** | ✅ **OOP wins here** | Functional is fine too, but less natural in Java |
| **Why** | An account HAS identity (a UUID), mutable state over time (balance, status), and behavior tied to state. This is what classes are FOR. | Discriminated unions + immutable updates work great in TypeScript but produce a lot of data copying in Java. |

```java
// Java — natural OOP (account is an entity with lifecycle)
@Entity
public class Account {
    private AccountStatus status;
    private BigDecimal balance;

    public void freeze(String reason) {
        if (status == CLOSED) throw new InvalidStateException("Cannot freeze a closed account");
        this.status = FROZEN;
        this.freezeReason = reason;
    }

    public void withdraw(BigDecimal amount) {
        if (status != ACTIVE) throw new InvalidStateException("Account not active");
        if (amount.compareTo(balance) > 0) throw new InsufficientFundsException();
        this.balance = balance.subtract(amount);
    }
}
```

---

### Scenario 3: Payment Routing

| | OOP | Functional |
|--|-----|------------|
| **Approach** | `Router` class with `List<RoutingRule>` where each rule is a `Predicate<Payment>` + `PaymentGateway` | Chain of pure functions: `payment => Optional<gateway>` |
| **Verdict** | ✅ **Functional wins** | OOP adds ceremony without value here |
| **Why** | Routing is a pure decision: given this payment, which gateway? No state mutation, no identity. It's a lookup function. |

```java
// Java — functional approach (even in Java, this is cleaner than routing classes)
@Component
public class PaymentRouter {
    record RoutingRule(Predicate<Payment> condition, PaymentGateway gateway) {}

    private final List<RoutingRule> rules;

    public PaymentRouter(StripeGateway stripe, CryptoGateway crypto, SepaBankGateway sepa) {
        this.rules = List.of(
            new RoutingRule(p -> p.getCurrency().equals("BTC"), crypto),
            new RoutingRule(p -> p.getRegion().equals("EU"), sepa),
            new RoutingRule(p -> true, stripe)  // default
        );
    }

    public PaymentGateway route(Payment payment) {
        return rules.stream()
            .filter(r -> r.condition().test(payment))
            .map(RoutingRule::gateway)
            .findFirst()
            .orElseThrow(() -> new NoRouteException(payment));
    }
}
```

---

### Scenario 4: Audit Logging

| | OOP | Functional |
|--|-----|------------|
| **Approach** | `@Aspect` with `@Around` advice | Decorator / wrapper function |
| **Verdict** | ✅ **OOP (AOP) wins** in Spring Boot | Functional decorator is fine for non-Spring code |
| **Why** | Spring AOP integrates with `@Transactional`, security context, MDC logging. Hard to replicate with a manual function wrapper across a whole codebase. |

```java
// Java AOP — attaches to ANY method annotated @Audited without touching business code
@Aspect @Component
public class AuditAspect {
    @Around("@annotation(Audited)")
    public Object audit(ProceedingJoinPoint jp) throws Throwable {
        log.info("AUDIT: {} called", jp.getSignature().getName());
        Object result = jp.proceed();
        log.info("AUDIT: {} completed", jp.getSignature().getName());
        return result;
    }
}
```

---

### Scenario 5: Data Transformation Pipelines

| | OOP | Functional |
|--|-----|------------|
| **Approach** | Visitor pattern, or chain of `Transformer<T>` objects | Stream pipeline, function composition |
| **Verdict** | ✅ **Functional wins, and Java's Stream API IS the answer** | |
| **Why** | `filter → map → groupBy → reduce` is exactly what streams are for. Creating classes for "filter by currency" is absurd ceremony. |

```java
// Java — just use streams (functional IS the right answer here)
Map<String, BigDecimal> totalByCounterparty = transactions.stream()
    .filter(t -> t.getStatus() == SETTLED)
    .filter(t -> t.getAmount().compareTo(THRESHOLD) > 0)
    .collect(Collectors.groupingBy(
        Transaction::getCounterpartyId,
        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
    ));
```

---

### Scenario 6: Validation Rules

| | OOP | Functional |
|--|-----|------------|
| **Approach** | `ValidationRule<T>` interface with `List<ValidationRule<Payment>>` | List of `Predicate<Payment>` functions or `Payment → ValidationResult` functions |
| **Verdict** | **Tie** — both are clean. Jakarta Bean Validation annotations are often best. |
| **Why** | `@NotNull @Positive @Size` on record fields is 3 annotations vs 3 classes. Use annotations for simple validation, validator classes for complex cross-field rules. |

```java
// Java — best of both: annotations for simple rules, classes for complex ones
public record PaymentRequest(
    @NotNull UUID senderId,
    @NotNull @Positive BigDecimal amount,
    @NotBlank @Size(min=3,max=3) String currency
) {}

// Complex cross-field validation: separate validator class
@Component
public class PaymentLimitValidator {
    public void validate(PaymentRequest req, Account account) {
        if (req.amount().compareTo(account.getDailyLimit()) > 0)
            throw new ValidationException("Exceeds daily limit of " + account.getDailyLimit());
    }
}
```

---

### Scenario 7: Event Handling / Publish-Subscribe

| | OOP | Functional |
|--|-----|------------|
| **Approach** | `ApplicationEventPublisher` + `@EventListener` classes | Event emitter with callback functions / RxJS-style streams |
| **Verdict** | ✅ **OOP wins in Spring** — `@EventListener` + `@Async` + `@TransactionalEventListener` is very powerful | Functional is better in Node.js/TS where EventEmitter is native and async is natural |
| **Why** | Spring's `@TransactionalEventListener(phase = AFTER_COMMIT)` lets you publish domain events that only fire if the transaction commits. No equivalent in functional TS without framework support. |

```java
// Domain event (just a record)
public record PaymentSettled(UUID paymentId, BigDecimal amount, String currency) {}

// Publisher
@Service
public class PaymentService {
    private final ApplicationEventPublisher events;

    @Transactional
    public void settle(UUID paymentId) {
        Payment payment = findAndSettle(paymentId);
        events.publishEvent(new PaymentSettled(paymentId, payment.getAmount(), payment.getCurrency()));
        // Event fires AFTER the transaction commits, not before!
    }
}

// Listener — runs in background thread after tx commits
@Component
public class PaymentSettledListener {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentSettled(PaymentSettled event) {
        notificationService.notifyUser(event.paymentId());
        analyticsService.recordSettlement(event);
    }
}
```

---

### Scenario 8: Domain Model (Entities, Value Objects)

| | OOP | Functional |
|--|-----|------------|
| **Approach** | Entity classes (`Account`, `Payment`) + Value Object records (`Money`, `AccountId`) | Discriminated union types + pure functions |
| **Verdict** | ✅ **OOP wins for entities, records for value objects** | |
| **Why** | Entities have identity (`UUID`) and lifecycle (creation → modification → archival). This is fundamentally object-oriented. Value objects (amount, currency code, IBAN) are just data — use `record`. |

```java
// Entity — has identity and mutable state (use a class)
@Entity
public class Wallet {
    @Id private UUID id;
    private BigDecimal balance;
    private WalletStatus status;
    // behavior methods: deposit, withdraw, freeze
}

// Value Object — just data, no identity (use a record)
public record Money(BigDecimal amount, String currency) {
    public Money {
        Objects.requireNonNull(amount);
        if (amount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Negative amount");
        if (currency == null || currency.length() != 3) throw new IllegalArgumentException("Invalid currency");
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) throw new CurrencyMismatchException();
        return new Money(amount.add(other.amount), currency);
    }
}
```

---

### Scenario 9: Configuration / Settings

| | OOP | Functional |
|--|-----|------------|
| **Approach** | `@ConfigurationProperties` record/class | Config object passed as a parameter |
| **Verdict** | ✅ **OOP wins** in Spring Boot | Spring DI makes config injection seamless |
| **Why** | `@ConfigurationProperties` + `@Validated` gives you type-safe, validated, auto-documented config with zero boilerplate. Nothing in TS matches this ergonomics. |

---

### Scenario 10: Repository / Data Access

| | OOP | Functional |
|--|-----|------------|
| **Approach** | Spring Data `JpaRepository<T, ID>` with query methods | Repository functions passed as parameters |
| **Verdict** | ✅ **OOP wins decisively** | |
| **Why** | Spring Data generates the implementation from the interface. `findByStatusAndCurrencyAndAmountGreaterThan(...)` generates a full JPQL query. No functional approach in any language comes close to this DX. |

---

## Pattern Mapping Table

| OOP Pattern | Functional Equivalent | When OOP is Better | When Functional is Better |
|-------------|----------------------|--------------------|--------------------------|
| **Builder** | Pipeline of transform functions | Complex objects with many optional fields + validation | Simple object construction |
| **Strategy** | Function map / higher-order function | Strategies need Spring DI or complex initialization | Strategies are simple closures |
| **Observer** | Event emitter / RxJS streams | Spring event system with transactional guarantees | Node.js/browser event-driven UIs |
| **Factory** | Factory function returning discriminated union | Multiple concrete types via DI, Spring beans | Type-safe creation of simple variants |
| **Decorator** | Function composition / wrapper | AOP-managed cross-cutting concerns | Ad-hoc function wrapping |
| **Template Method** | Higher-order function with callbacks | Framework extension points | Process pipelines |
| **Iterator** | `Stream<T>` / lazy sequences | Custom iteration protocols | Data transformation pipelines |
| **State** | Entity class with state field + transitions | Persistent entities with lifecycle | Pure state machines without I/O |
| **Command** | `Runnable` / `Callable<T>` / event objects | Undo/redo, queuing, audit trails | Simple deferred computation |
| **Composite** | Hierarchical object graph | Tree structures (org chart, ledger entries) | Flat data transformations |

---

## The Hybrid Reality in Modern Java

Modern Java (17-21) has been actively borrowing from functional programming:

| Functional Concept | Java Mechanism |
|-------------------|---------------|
| Algebraic data types (sum types) | `sealed interface` + `record` subtypes |
| Pattern matching | `switch` expressions with record patterns (Java 21) |
| Immutable data | `record` (auto-final fields, canonical constructor) |
| Higher-order functions | Lambdas + `Function<T,R>`, `Predicate<T>`, etc. |
| Lazy evaluation | `Stream<T>` (intermediate operations are lazy) |
| Monadic option type | `Optional<T>` |
| Parallel data processing | Parallel streams / `CompletableFuture` |

**The result**: A modern Spring Boot service uses OOP for structure (classes, interfaces, DI) and functional style for data processing (streams, lambdas, method references). This hybrid is not a compromise — it's the right tool for each job.

---

## Team Readability Heuristic

Use this to decide when to reach for a pattern vs when to keep it simple:

```
When will your Java team THANK you for using a pattern?
✅ The behavior needs to be pluggable / replaceable (Strategy, DI)
✅ Multiple implementations of the same contract exist (Interface + implementations)
✅ Cross-cutting concerns touch many classes (AOP, Decorator)
✅ The class hierarchy models a real-world domain concept (Account, Payment, Wallet)
✅ The pattern is enforced by the framework you're using (Spring @Component hierarchy)

When will they think you OVER-ENGINEERED it?
❌ You created a class for a 3-line function that never changes
❌ You created a factory for an object with one concrete type
❌ You implemented Builder for a 2-field record
❌ You created an interface with exactly one implementation and no plans for more
❌ The abstraction makes the code harder to trace (too many layers of indirection)
```

---

## The One-Sentence Verdict

> **Use classes when you're modeling things that have identity, state, and behavior over time.**
> **Use functions when you're transforming data without side effects.**
> **Use interfaces when you need to swap implementations.**
> **Modern Java lets you use all three in the same codebase. Do that.**

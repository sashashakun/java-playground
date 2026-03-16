# SOLID Principles — Quick Reference for Fintech Engineers

> Each principle with a one-line definition, fintech before/after code in Java,
> the functional TypeScript equivalent, the smell that signals a violation,
> and an honest verdict on which version wins.

---

## S — Single Responsibility Principle

> **A class should have one reason to change.**

### Fintech Example: Payment Processor

**❌ BEFORE — God class violating SRP:**

```java
// One class does: validation + fraud check + execution + persistence + notification
public class PaymentProcessor {

    public void process(PaymentRequest request) {
        // 1. Validate
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (request.getCurrency() == null || request.getCurrency().length() != 3) {
            throw new IllegalArgumentException("Invalid currency");
        }

        // 2. Fraud check
        int recentCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM payments WHERE sender_id = ? AND created_at > ?",
            Integer.class, request.getSenderId(), Instant.now().minusSeconds(3600)
        );
        if (recentCount > 10) throw new FraudException("Velocity limit exceeded");

        // 3. Execute
        externalGateway.charge(request.getSenderId(), request.getAmount());

        // 4. Persist
        Payment payment = new Payment(request.getSenderId(), request.getAmount(), SETTLED);
        jdbcTemplate.update("INSERT INTO payments ...", ...);

        // 5. Notify
        String body = "Your payment of " + request.getAmount() + " was processed.";
        emailClient.send(request.getUserEmail(), "Payment Confirmed", body);
    }
}
```

**✅ AFTER — each class has one job:**

```java
@Component
public class PaymentRequestValidator {
    public void validate(PaymentRequest request) {
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException("Amount must be positive");
        if (request.currency() == null || request.currency().length() != 3)
            throw new ValidationException("Invalid ISO currency code");
    }
}

@Service
public class FraudDetectionService {
    public void checkVelocity(UUID senderId) {
        long recent = paymentRepository.countRecentBySender(senderId, Instant.now().minusSeconds(3600));
        if (recent >= 10) throw new FraudException("Velocity limit exceeded for sender " + senderId);
    }
}

@Service
public class PaymentExecutionService {
    public void charge(UUID senderId, BigDecimal amount, String currency) {
        gatewayClient.charge(new ChargeRequest(senderId, amount, currency));
    }
}

@Service
public class PaymentNotificationService {
    public void notifySuccess(Payment payment) {
        String body = "Your payment of %s %s was processed.".formatted(payment.amount(), payment.currency());
        emailClient.send(payment.userEmail(), "Payment Confirmed", body);
    }
}

// Orchestrator — its ONE job is to coordinate
@Service
public class PaymentService {
    public Payment process(PaymentRequest request) {
        validator.validate(request);
        fraudDetector.checkVelocity(request.senderId());
        executor.charge(request.senderId(), request.amount(), request.currency());
        Payment payment = paymentRepository.save(Payment.from(request));
        notifier.notifySuccess(payment);
        return payment;
    }
}
```

### Functional TypeScript Equivalent

```typescript
// SRP in functional TS = single-purpose functions
const validatePaymentRequest = (req: PaymentRequest): void => {
  if (req.amount <= 0) throw new Error("Amount must be positive");
  if (!/^[A-Z]{3}$/.test(req.currency)) throw new Error("Invalid currency");
};

const checkVelocityFraud = async (senderId: string, repo: PaymentRepo): Promise<void> => {
  const recent = await repo.countRecentBySender(senderId, subHours(new Date(), 1));
  if (recent >= 10) throw new Error("Velocity limit exceeded");
};

const chargeGateway = async (req: PaymentRequest, gateway: Gateway): Promise<void> => {
  await gateway.charge(req.senderId, req.amount, req.currency);
};

const notifyPaymentSuccess = async (payment: Payment, emailer: Emailer): Promise<void> => {
  await emailer.send(payment.userEmail, `Payment of ${payment.amount} ${payment.currency} confirmed.`);
};

// Orchestrator function
const processPayment = async (req: PaymentRequest, deps: Deps): Promise<Payment> => {
  validatePaymentRequest(req);
  await checkVelocityFraud(req.senderId, deps.repo);
  await chargeGateway(req, deps.gateway);
  const payment = await deps.repo.save(paymentFromRequest(req));
  await notifyPaymentSuccess(payment, deps.emailer);
  return payment;
};
```

### Smell That Signals Violation
The class name contains "And" (`ValidateAndProcess`, `LoadAndTransform`). Or the class has more than 5-7 methods that touch different concerns.

### Honest Verdict
**Tie.** Both versions are equally readable. In Java, the OOP version is slightly better because Spring DI lets you inject each sub-service independently and mock them in tests. In TypeScript, the functional version is simpler — no class ceremony, just functions. The key insight is the same in both: **separate concerns**. It's not about OOP vs functional; it's about decomposition.

---

## O — Open/Closed Principle

> **Open for extension, closed for modification.**
> Add new behavior by adding new code, not by changing existing code.

### Fintech Example: Fee Calculator

**❌ BEFORE — adding a new fee type requires modifying the calculator:**

```java
public class FeeCalculator {
    public BigDecimal calculate(Transaction tx, FeeType type) {
        return switch (type) {
            case FLAT -> new BigDecimal("0.50");
            case PERCENTAGE -> tx.getAmount().multiply(new BigDecimal("0.015"));
            case TIERED -> {
                // ... tiered logic
            }
            // Every new fee type requires changing this method!
        };
    }
}
```

**✅ AFTER — Strategy pattern, new fee types = new classes:**

```java
public interface FeeStrategy {
    BigDecimal calculate(Transaction transaction);
}

@Component
public class FlatFeeStrategy implements FeeStrategy {
    private static final BigDecimal FLAT_FEE = new BigDecimal("0.50");

    @Override
    public BigDecimal calculate(Transaction transaction) {
        return FLAT_FEE;
    }
}

@Component
public class PercentageFeeStrategy implements FeeStrategy {
    private final BigDecimal rate;

    public PercentageFeeStrategy(@Value("${fees.percentage-rate:0.015}") BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public BigDecimal calculate(Transaction transaction) {
        return transaction.getAmount().multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
    }
}

@Component
public class TieredFeeStrategy implements FeeStrategy {
    @Override
    public BigDecimal calculate(Transaction transaction) {
        BigDecimal amount = transaction.getAmount();
        if (amount.compareTo(new BigDecimal("1000")) < 0) return new BigDecimal("0.25");
        if (amount.compareTo(new BigDecimal("10000")) < 0) return new BigDecimal("1.00");
        return new BigDecimal("2.50");
    }
}

// Add CryptoNetworkFeeStrategy, ChainFeeStrategy, etc. WITHOUT changing any existing code
```

### Functional TypeScript Equivalent

```typescript
// OCP in functional TS = function composition, strategy map, higher-order functions
type FeeStrategy = (tx: Transaction) => BigDecimal;

const flatFee: FeeStrategy = () => new BigDecimal("0.50");

const percentageFee = (rate: string): FeeStrategy =>
  (tx) => tx.amount.mul(new BigDecimal(rate)).toDecimalPlaces(2);

const tieredFee: FeeStrategy = (tx) => {
  if (tx.amount.lt("1000")) return new BigDecimal("0.25");
  if (tx.amount.lt("10000")) return new BigDecimal("1.00");
  return new BigDecimal("2.50");
};

// Adding a new fee type = adding a new function. Zero changes to existing code.
const cryptoNetworkFee: FeeStrategy = (tx) => fetchNetworkFee(tx.network);

// Calculator is closed to modification, open to extension via strategy:
const calculateFee = (tx: Transaction, strategy: FeeStrategy): BigDecimal =>
  strategy(tx);
```

### Smell That Signals Violation
A `switch` or `if-else if` chain that you need to extend every time a new type is added. You're finding yourself modifying the same `calculate()` or `process()` method repeatedly.

### Honest Verdict
**Functional TS wins for simplicity.** A higher-order function is fewer lines than an interface + multiple implementing classes. The Java Strategy pattern has more ceremony. However, in a Spring Boot app, the OCP Java version integrates better with DI, allows strategies to be Spring beans (injectable, configurable from YAML), and is more discoverable for new team members (`IntelliJ → Find Usages → FeeStrategy` shows all implementations instantly).

---

## L — Liskov Substitution Principle

> **Subtypes must be substitutable for their base types without altering program correctness.**
> If `B extends A`, any code working with `A` should work transparently with `B`.

### Fintech Example: Account Hierarchy

**❌ BEFORE — FrozenAccount violates LSP:**

```java
public class Account {
    protected BigDecimal balance;

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(balance) > 0) throw new InsufficientFundsException();
        balance = balance.subtract(amount);
    }
}

// LSP VIOLATION: callers expect withdraw() to work, FrozenAccount breaks that contract
public class FrozenAccount extends Account {
    @Override
    public void withdraw(BigDecimal amount) {
        throw new AccountFrozenException("Account is frozen"); // strengthens precondition!
    }
}

// Code that worked with Account breaks with FrozenAccount:
void processWithdrawal(Account account, BigDecimal amount) {
    account.withdraw(amount); // May throw AccountFrozenException — caller didn't expect this!
}
```

**✅ AFTER — model the difference in the type hierarchy:**

```java
public interface Account {
    BigDecimal getBalance();
    String getId();
}

public interface WriteableAccount extends Account {
    void deposit(BigDecimal amount);
    void withdraw(BigDecimal amount); // only on accounts that allow withdrawals
}

public class ActiveAccount implements WriteableAccount {
    @Override
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(balance) > 0) throw new InsufficientFundsException();
        this.balance = balance.subtract(amount);
    }
    // ... other methods
}

public class FrozenAccount implements Account {
    // FrozenAccount only implements Account, NOT WriteableAccount
    // No withdraw() method — the TYPE SYSTEM prevents calling withdraw on frozen accounts!
}

// Code is now honest about what it requires:
void processWithdrawal(WriteableAccount account, BigDecimal amount) {
    account.withdraw(amount); // safe — FrozenAccount can't be passed here
}
```

### Functional TypeScript Equivalent

```typescript
// LSP in functional TS = exhaustive pattern matching on discriminated unions
type Account =
  | { type: "ACTIVE"; id: string; balance: BigDecimal }
  | { type: "FROZEN"; id: string; balance: BigDecimal; frozenAt: Date }
  | { type: "CLOSED"; id: string; closedAt: Date };

// Function that only accepts writeable accounts — type safety without inheritance
type WriteableAccount = Extract<Account, { type: "ACTIVE" }>;

const withdraw = (account: WriteableAccount, amount: BigDecimal): WriteableAccount => {
  if (amount.gt(account.balance)) throw new Error("Insufficient funds");
  return { ...account, balance: account.balance.sub(amount) };
};

// Process all account types honestly — compiler enforces exhaustiveness:
const describeAccount = (account: Account): string => {
  switch (account.type) {
    case "ACTIVE": return `Active balance: ${account.balance}`;
    case "FROZEN": return `Frozen since ${account.frozenAt}`;
    case "CLOSED": return `Closed on ${account.closedAt}`;
    // No default needed — TypeScript ensures all cases handled
  }
};
```

### Smell That Signals Violation
A subclass throws an exception in a method that the base class guarantees won't throw. Or a subclass ignores a method (implements it as a no-op). Or you see `instanceof` checks before calling a method on a base type.

### Honest Verdict
**Java OOP wins for enforcement.** When you model a `FrozenAccount` as not implementing `WriteableAccount`, the type system physically prevents passing it to `processWithdrawal`. In TypeScript, the discriminated union approach achieves the same safety but requires discipline — it's easy to accidentally accept the broader `Account` type where you meant `WriteableAccount`. Java's nominal typing makes the constraint more explicit and refactoring safer in large teams.

---

## I — Interface Segregation Principle

> **No client should be forced to depend on methods it doesn't use.**
> Prefer many small, focused interfaces over one large one.

### Fintech Example: Banking Service

**❌ BEFORE — fat interface forces implementations to implement methods they don't need:**

```java
public interface BankingService {
    // Wallet operations
    Wallet createWallet(CreateWalletRequest request);
    void deposit(UUID walletId, BigDecimal amount);
    void withdraw(UUID walletId, BigDecimal amount);
    BigDecimal getBalance(UUID walletId);

    // Payment operations
    Payment initiatePayment(PaymentRequest request);
    PaymentStatus getPaymentStatus(UUID paymentId);
    void cancelPayment(UUID paymentId);

    // KYC operations
    KycResult performKyc(UUID userId, KycDocuments documents);
    KycStatus getKycStatus(UUID userId);

    // Admin operations
    void freezeAccount(UUID walletId, String reason);
    List<AuditLog> getAuditLogs(UUID walletId);
    void generateReport(ReportType type, DateRange range);
}

// The read-only analytics service is FORCED to implement write methods it never uses:
public class AnalyticsService implements BankingService {
    public Wallet createWallet(...) { throw new UnsupportedOperationException(); }
    public void deposit(...) { throw new UnsupportedOperationException(); }
    // ... etc — tons of noise
}
```

**✅ AFTER — focused interfaces:**

```java
public interface WalletReader {
    Wallet findById(UUID walletId);
    BigDecimal getBalance(UUID walletId);
}

public interface WalletWriter {
    Wallet create(CreateWalletRequest request);
    void deposit(UUID walletId, BigDecimal amount);
    void withdraw(UUID walletId, BigDecimal amount);
}

public interface PaymentOperations {
    Payment initiate(PaymentRequest request);
    PaymentStatus getStatus(UUID paymentId);
    void cancel(UUID paymentId);
}

public interface KycOperations {
    KycResult perform(UUID userId, KycDocuments documents);
    KycStatus getStatus(UUID userId);
}

public interface ComplianceOperations {
    void freezeAccount(UUID walletId, String reason);
    List<AuditLog> getAuditLogs(UUID walletId);
}

// Each service only implements what it actually needs:
public class AnalyticsService implements WalletReader { ... }
public class TransferService implements WalletReader, WalletWriter, PaymentOperations { ... }
public class ComplianceService implements ComplianceOperations, WalletReader { ... }
```

### Functional TypeScript Equivalent

```typescript
// ISP in functional TS = narrow function signatures, pass only what you need
// No fat interfaces — just accept the exact dependencies as function parameters

// Analytics function only needs read capability
const analyzeWallet = async (
  walletId: string,
  getBalance: (id: string) => Promise<BigDecimal>,  // not the whole wallet service!
  getTransactions: (id: string) => Promise<Transaction[]>
): Promise<WalletAnalysis> => {
  const [balance, transactions] = await Promise.all([
    getBalance(walletId),
    getTransactions(walletId)
  ]);
  return computeAnalysis(balance, transactions);
};

// Transfer function accepts only write capability
const executeTransfer = async (
  from: string,
  to: string,
  amount: BigDecimal,
  debit: (id: string, amt: BigDecimal) => Promise<void>,
  credit: (id: string, amt: BigDecimal) => Promise<void>
) => { ... };
```

### Smell That Signals Violation
You see `throw new UnsupportedOperationException()` in interface implementations. Or you import a service just to call one method but now depend on 15 methods you don't use.

### Honest Verdict
**Tie, different mechanism.** Java ISP solves it with interface decomposition (better for large teams with clear architectural boundaries). TypeScript functional style solves it by passing individual functions as parameters — naturally narrow. For internal services in a monolith, functional TS is cleaner. For API boundaries between modules (especially with DI frameworks), Java interfaces make the contract explicit and IDE-navigable.

---

## D — Dependency Inversion Principle

> **High-level modules should not depend on low-level modules. Both should depend on abstractions.**
> Abstractions should not depend on details. Details should depend on abstractions.

### Fintech Example: Transaction Service

**❌ BEFORE — hardcoded dependency on implementation:**

```java
public class TransactionService {
    // Hardcoded concrete dependency — can't swap, can't mock, can't test
    private final PostgresTransactionRepository repository = new PostgresTransactionRepository();
    private final SmtpEmailSender emailSender = new SmtpEmailSender("smtp.example.com", 587);
    private final StripeGateway gateway = new StripeGateway(System.getenv("STRIPE_KEY"));

    public Transaction process(PaymentRequest request) {
        gateway.charge(request);
        Transaction tx = repository.save(Transaction.from(request));
        emailSender.send(request.getEmail(), "Payment processed");
        return tx;
    }
}
```

**✅ AFTER — depends on abstractions, injected at construction:**

```java
// Abstractions (high-level policy)
public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(UUID id);
    List<Transaction> findByWalletId(UUID walletId);
}

public interface NotificationSender {
    void send(String recipient, String message);
}

public interface PaymentGateway {
    void charge(PaymentRequest request);
}

// High-level module depends ONLY on abstractions:
@Service
public class TransactionService {
    private final TransactionRepository repository;
    private final NotificationSender notifier;
    private final PaymentGateway gateway;

    public TransactionService(
        TransactionRepository repository,
        NotificationSender notifier,
        PaymentGateway gateway
    ) {
        this.repository = repository;
        this.notifier = notifier;
        this.gateway = gateway;
    }

    public Transaction process(PaymentRequest request) {
        gateway.charge(request);
        Transaction tx = repository.save(Transaction.from(request));
        notifier.send(request.getEmail(), "Payment processed");
        return tx;
    }
}

// Low-level details implement the abstractions:
@Repository
public class JpaTransactionRepository implements TransactionRepository { ... }

@Component
public class SmtpNotificationSender implements NotificationSender { ... }

@Component
public class StripePaymentGateway implements PaymentGateway { ... }

// In tests — inject mocks:
var service = new TransactionService(
    mock(TransactionRepository.class),
    mock(NotificationSender.class),
    mock(PaymentGateway.class)
);
```

### Functional TypeScript Equivalent

```typescript
// DIP in functional TS = pass dependencies as function parameters (higher-order functions)
// No abstractions needed — just accept the functions you need

type TransactionRepo = {
  save: (tx: Transaction) => Promise<Transaction>;
  findById: (id: string) => Promise<Transaction | null>;
};

type Notifier = (recipient: string, message: string) => Promise<void>;
type Gateway = (req: PaymentRequest) => Promise<void>;

// High-level function receives its dependencies — it doesn't know or care about implementations:
const processTransaction = (repo: TransactionRepo, notify: Notifier, charge: Gateway) =>
  async (request: PaymentRequest): Promise<Transaction> => {
    await charge(request);
    const tx = await repo.save(transactionFromRequest(request));
    await notify(request.email, "Payment processed");
    return tx;
  };

// Wire up in production:
const process = processTransaction(
  postgresTransactionRepo,
  smtpNotifier,
  stripeGateway
);

// Wire up in tests:
const processTest = processTransaction(
  inMemoryRepo,
  () => Promise.resolve(), // no-op notifier
  mockGateway
);
```

### Smell That Signals Violation
`new SomeConcreteClass()` inside a service class. `System.getenv()` called deep inside business logic. Code that's impossible to unit test without a live database or external API.

### Honest Verdict
**Both are equally important; Java wins on visibility.** The Java version with interfaces makes the abstraction boundary a first-class artifact that shows up in IDE navigation (`Ctrl+Click` on an interface shows all implementations). The functional TypeScript version is more concise but the shape of the dependency (the function type) is implicit. In large teams, explicit interface contracts reduce misunderstandings. For small codebases, TypeScript's approach is simpler. In both cases, **the pattern — inject your dependencies, don't instantiate them** — is what matters. The mechanism is secondary.

---

## Summary Table

| Principle | Java Mechanism | Functional TS Equivalent | When Java Wins | When TS Wins |
|-----------|---------------|--------------------------|----------------|--------------|
| **SRP** | Separate classes | Separate functions | Large team, Spring DI | Small codebase, less ceremony |
| **OCP** | Strategy interface + implementations | Higher-order functions / strategy map | Spring beans, DI, IDE navigation | Simpler syntax, fewer files |
| **LSP** | Interface hierarchy, no `UnsupportedOperationException` | Discriminated unions with exhaustive matching | Nominal type enforcement | Compiler proves exhaustiveness |
| **ISP** | Multiple focused interfaces | Narrow function signatures | Module API boundaries | Internal function composition |
| **DIP** | Constructor injection of interface types | Higher-order functions accepting dependencies | IDE discoverability, Spring DI | Less ceremony, structural typing |

---

## The SOLID Meta-Principle

All 5 principles share a common insight: **depend on behavior contracts, not concrete implementations.** In Java, contracts are expressed as interfaces and abstract classes. In functional TypeScript, contracts are expressed as function types and discriminated unions.

The underlying design wisdom is identical. The syntax is different. Both approaches make code:
- Easier to test (can inject mocks)
- Easier to extend (don't modify what's working)
- Easier to reason about (each piece has one job)

Choose the mechanism that fits your language and team. Apply the wisdom regardless.

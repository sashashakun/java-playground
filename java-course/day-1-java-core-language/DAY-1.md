# Day 1: Java Core Language

> **Theme**: Build a Transaction Classification Engine
> **Total estimated time**: 6–8 hours
> **Difficulty**: 🟢🟢🟡🟡🟢🟡🟡 (per exercise)

By the end of today, you'll have built a working transaction classification engine that can parse CSV records, classify transactions using rich enums, store them in a queryable ledger, model them as immutable value objects, and reject invalid data with proper exceptions.

---

## What You're Building

A **Transaction Classification Engine** — a component that appears in every fintech backend. It takes raw transaction data, validates it, classifies it by type, enriches it with business logic, and makes it queryable.

```
CSV Input  →  Parser  →  Validator  →  Classifier  →  Ledger
"txn-001,2024-01-15T10:30:00Z,15099,USD,PURCHASE,Coffee"
                                              ↓
                              TransactionType.PURCHASE
                              Currency.USD
                              Money(150.99, "USD")
                              PaymentEvent.PaymentSettled(...)
```

---

## Setup: Running the Exercises

Each exercise is a self-contained Gradle project. Open the exercise directory in IntelliJ (File → Open → select the `0X-...` directory). IntelliJ will detect the `build.gradle.kts` and import it.

**To generate the Gradle wrapper** (run once per exercise if not using IntelliJ):
```bash
# Option A: if you have Gradle installed globally
cd exercises/01-tooling-and-gradle
gradle wrapper --gradle-version 8.7
./gradlew test

# Option B: IntelliJ does this automatically when you open the project
```

**Running tests from the command line** (after wrapper is generated):
```bash
./gradlew test           # Run all tests
./gradlew test --info    # Verbose output
./gradlew test --tests "*TransactionSummaryTest"  # Run specific test class
```

---

## TypeScript → Java Mental Model for Today

Before diving in, internalize these translations:

| What you know | What Java does |
|---------------|----------------|
| `const` | `final` on a variable (reference is immutable; object may not be) |
| TypeScript `interface` (structural) | Java `interface` (nominal — must explicitly `implements`) |
| TypeScript `type Foo = { ... }` | Java `record Foo(...)` — immutable data class |
| `switch (x) { case "A": return 1; }` | `switch (x) { case "A" -> 1; }` — switch EXPRESSION |
| `Array<T>` | `List<T>` — use `ArrayList<>` for mutable, `List.of()` for immutable |
| `undefined` / null coalescing `??` | Only `null` — use `Optional<T>` or `getOrDefault()` |
| `throw new Error("msg")` | `throw new RuntimeException("msg")` (unchecked) |

---

## Exercise 01 — Tooling & Gradle 🟢

**Estimated time**: 30–45 min | **File**: `exercises/01-tooling-and-gradle/`

### Learning Goals
- Understand Java project structure (`src/main/java/`, `src/test/java/`)
- Write your first Java methods
- Run tests with Gradle
- Understand `System.out.println` vs a logger

### Theory: Java Project Anatomy

```
01-tooling-and-gradle/
├── build.gradle.kts          ← dependency config (like package.json)
├── settings.gradle.kts       ← project name
└── src/
    ├── main/java/            ← production code
    │   └── com/example/fintech/day1/tooling/
    │       └── TransactionSummary.java
    └── test/java/            ← test code
        └── com/example/fintech/day1/tooling/
            └── TransactionSummaryTest.java
```

The package `com.example.fintech.day1.tooling` maps directly to the directory path. This is enforced by the Java compiler.

### TypeScript vs Java: Method Signatures

```typescript
// TypeScript
function countPositive(amounts: number[]): number {
  return amounts.filter(n => n > 0).length;
}
```

```java
// Java — return type BEFORE method name, no "function" keyword
public int countPositive(int[] amounts) {
    // arrays don't have .filter() — use a loop or stream
    int count = 0;
    for (int a : amounts) {
        if (a > 0) count++;
    }
    return count;
}
```

### Your Task
Open `TransactionSummary.java` and implement the 5 TODO methods. All tests in `TransactionSummaryTest.java` must pass.

### Checkpoint ✅
`./gradlew test` shows `BUILD SUCCESSFUL` with 5 tests passing.

### Break Exercise
After all tests pass: change `countPositive` to use `>=` instead of `>`. Notice which tests fail. Change it back.

---

## Exercise 02 — Type System & Primitives 🟢

**Estimated time**: 45–60 min | **File**: `exercises/02-type-system-and-primitives/`

### Learning Goals
- Why `double` is wrong for money
- How `BigDecimal` fixes precision
- The difference between `int`, `long`, and `BigDecimal` for financial amounts
- Rounding modes and why `HALF_EVEN` (banker's rounding) matters

### Theory: The Money Type Problem

```typescript
// TypeScript — same floating-point problem as JavaScript
console.log(0.1 + 0.2);  // 0.30000000000000004
```

```java
// Java double — same problem
System.out.println(0.1 + 0.2);  // 0.30000000000000004

// BigDecimal — exact
System.out.println(new BigDecimal("0.1").add(new BigDecimal("0.2")));  // 0.3
```

### Convention for Financial Amounts

| Amount | Storage | Representation |
|--------|---------|----------------|
| Card transaction ($15.99) | `long amountCents = 1599` | Store in minor units (cents) |
| Exchange rate (1.0847) | `BigDecimal rate = new BigDecimal("1.0847")` | Full precision |
| Display ("$15.99") | Format from cents | `"$%.2f".formatted(1599 / 100.0)` |
| Fee calculation | `BigDecimal.multiply(rate).setScale(2, HALF_EVEN)` | Always specify scale |

### Banker's Rounding (HALF_EVEN)

Used in financial systems because it's statistically unbiased — it rounds 0.5 to the nearest EVEN number:
- 2.5 → 2 (rounds down to even)
- 3.5 → 4 (rounds up to even)

```java
BigDecimal fee = new BigDecimal("12.345")
    .setScale(2, RoundingMode.HALF_EVEN);  // → 12.34
BigDecimal fee2 = new BigDecimal("12.355")
    .setScale(2, RoundingMode.HALF_EVEN);  // → 12.36
```

### Your Task
Implement `MoneyCalculator.java` — 5 methods that perform precise financial calculations. All tests must pass.

### Checkpoint ✅
`./gradlew test` shows `BUILD SUCCESSFUL`. Verify that the precision test passes — `0.1 + 0.2 = 0.3` exactly.

---

## Exercise 03 — Strings & Text 🟢

**Estimated time**: 45–60 min | **File**: `exercises/03-strings-and-text/`

### Learning Goals
- String methods: `split`, `strip`, `contains`, `startsWith`, `formatted`
- Java text blocks (multi-line strings)
- Parsing structured text (CSV)
- String → number conversion

### Key String Differences from TypeScript

```typescript
// TypeScript
const s = "  hello  ";
s.trim()              // "hello"
`Amount: ${amount}`   // template literal
s.split(",")          // returns string[]
```

```java
// Java
String s = "  hello  ";
s.strip()             // "hello" (Unicode-aware, prefer over trim())
"Amount: %s".formatted(amount)   // format string
// OR: "Amount: " + amount        // concatenation
s.split(",")          // returns String[]
```

### Text Blocks (Java 15+)

```java
String csvHeader = """
    id,timestamp,amount_cents,currency,type,description
    """;

// Text blocks trim the leading indentation based on the closing """
// Very useful for writing test fixtures inline!
```

### Your Task
Implement `TransactionCsvParser.java`. The CSV format is:
```
id,timestamp,amount_cents,currency,type,description
txn-001,2024-01-15T10:30:00Z,15099,USD,PURCHASE,Coffee at Starbucks
```

### Checkpoint ✅
`./gradlew test` passing. Then run the main method to see your parser in action.

---

## Exercise 04 — Collections Deep Dive 🟡

**Estimated time**: 60–75 min | **File**: `exercises/04-collections-deep-dive/`

### Learning Goals
- `List`, `Map`, `Set` — which to use when
- Streams: `filter`, `map`, `collect`, `groupingBy`, `reduce`
- `Optional<T>` — expressing absence without null
- Choosing between `ArrayList`, `LinkedList`, `HashMap`, `LinkedHashMap`, `TreeMap`

### The Collection Decision Tree

```
Need to store items in sequence?
  ├── Need random access by index?    → List<T>   (ArrayList — O(1) get)
  ├── Need frequent insert/delete?    → List<T>   (LinkedList — but rare in practice)
  └── Need uniqueness?                → Set<T>    (HashSet — unordered, O(1))
                                                  (LinkedHashSet — insertion order)
                                                  (TreeSet — sorted)

Need key→value lookup?
  ├── Order doesn't matter?           → Map<K,V>  (HashMap — O(1))
  ├── Need insertion order?           → Map<K,V>  (LinkedHashMap)
  └── Need sorted keys?               → Map<K,V>  (TreeMap — O(log n))
```

### Stream Basics — TypeScript vs Java

```typescript
// TypeScript
const totals = transactions
  .filter(t => t.currency === "USD")
  .map(t => t.amount)
  .reduce((sum, a) => sum + a, 0);
```

```java
// Java — almost identical mental model
long total = transactions.stream()
    .filter(t -> t.currency().equals("USD"))
    .mapToLong(Transaction::amountCents)  // mapToLong for primitive stream
    .sum();

// groupBy (no direct TS equivalent without reduce)
Map<String, List<Transaction>> byCurrency = transactions.stream()
    .collect(Collectors.groupingBy(Transaction::currency));
```

### `Optional<T>` — Not a Monad, Just a Null Container

```java
// Returns empty Optional if not found — no NullPointerException
Optional<Transaction> found = ledger.findById("txn-001");

// Consume it:
found.ifPresent(tx -> System.out.println(tx.id()));

// Or with fallback:
Transaction tx = found.orElseThrow(() -> new NoSuchElementException("Not found"));
```

### Your Task
Implement `TransactionLedger.java` with 7 methods. All tests must pass.

### Checkpoint ✅
All tests pass including the streaming/aggregation tests. The `groupByCurrency` test verifies your Map contains the right groupings.

---

## Exercise 05 — Enums as Classes 🟢

**Estimated time**: 45–60 min | **File**: `exercises/05-enums-as-classes/`

### Learning Goals
- Java enums are classes — they can have constructors, fields, and methods
- Enum `switch` expressions for type-based behavior
- `Enum.values()` and `Enum.valueOf()`
- When to use an enum vs a sealed class

### TypeScript vs Java Enums

```typescript
// TypeScript enum — just a string/number map
enum TransactionType {
  PURCHASE = "PURCHASE",
  REFUND = "REFUND",
}
// Can't add methods or fields
```

```java
// Java enum — a full class
public enum TransactionType {
    PURCHASE("PUR", false, "Merchant purchase"),
    REFUND("REF", true, "Merchant refund");

    private final String code;
    private final boolean isCredit;
    private final String description;

    // Constructor:
    TransactionType(String code, boolean isCredit, String description) {
        this.code = code;
        this.isCredit = isCredit;
        this.description = description;
    }

    // Methods:
    public String getCode() { return code; }
    public boolean isCredit() { return isCredit; }

    // Static lookup:
    public static TransactionType fromCode(String code) {
        for (TransactionType t : values()) {
            if (t.code.equals(code)) return t;
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
```

### Your Task
Implement the TODO methods in `TransactionType.java` and `Currency.java`. Then implement `FeeCalculator.java` which uses both enums to compute fees.

### Checkpoint ✅
`./gradlew test` passes. Verify `TransactionType.fromCode("PUR")` returns `PURCHASE` and `Currency.BTC.isCrypto()` returns `true`.

---

## Exercise 06 — Records & Sealed Classes 🟡

**Estimated time**: 60–75 min | **File**: `exercises/06-records-and-sealed-classes/`

### Learning Goals
- `record` — immutable data classes with auto-generated constructor, getters, `equals`, `hashCode`, `toString`
- Compact constructor for validation
- `sealed interface` — closed type hierarchies (discriminated unions)
- Pattern matching `switch` — Java 21's exhaustive switch on sealed types

### Records — TypeScript's `type` but Immutable

```typescript
// TypeScript
type Money = { amount: number; currency: string; };
const m = { amount: 100, currency: "USD" };
m.amount = 200; // allowed — plain object is mutable
```

```java
// Java record — truly immutable, with validation
public record Money(BigDecimal amount, String currency) {
    // Compact constructor — runs validation before assignment
    public Money {
        Objects.requireNonNull(amount, "amount must not be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("amount cannot be negative");
    }

    // You can add methods:
    public Money add(Money other) {
        if (!this.currency.equals(other.currency))
            throw new IllegalArgumentException("Currency mismatch");
        return new Money(this.amount.add(other.amount), this.currency);
    }
}

// Usage:
var m = new Money(new BigDecimal("100.00"), "USD");
// m.amount = ... — COMPILE ERROR! Records are final
```

### Sealed Classes — Java's Discriminated Unions

```typescript
// TypeScript discriminated union
type PaymentEvent =
  | { type: "INITIATED"; paymentId: string; amount: number }
  | { type: "SETTLED"; paymentId: string; txHash: string }
  | { type: "FAILED"; paymentId: string; reason: string };
```

```java
// Java sealed interface — compiler knows all subtypes
public sealed interface PaymentEvent
    permits PaymentInitiated, PaymentProcessing, PaymentSettled, PaymentFailed {}

public record PaymentInitiated(String paymentId, Money amount) implements PaymentEvent {}
public record PaymentSettled(String paymentId, Money amount, Instant settledAt) implements PaymentEvent {}
public record PaymentFailed(String paymentId, String reason, int code) implements PaymentEvent {}

// Pattern matching switch — EXHAUSTIVE (no default needed!)
String describe(PaymentEvent event) {
    return switch (event) {
        case PaymentInitiated e  -> "Initiated: " + e.amount();
        case PaymentProcessing e -> "Processing via " + e.gatewayRef();
        case PaymentSettled e    -> "Settled at " + e.settledAt();
        case PaymentFailed e     -> "Failed: " + e.reason();
    };
}
```

### Your Task
Implement `Money.java` (compact constructor + arithmetic methods), the `PaymentEvent` sealed hierarchy, and `PaymentEventProcessor.java` (uses pattern matching switch expressions).

### Checkpoint ✅
All tests pass. Pay special attention to the `Money` validation tests — negative amounts and null must throw exceptions.

---

## Exercise 07 — Exception Handling 🟡

**Estimated time**: 60–75 min | **File**: `exercises/07-exception-handling/`

### Learning Goals
- Checked vs unchecked exceptions — when each is appropriate
- Custom exception classes with domain context fields
- `try-with-resources` for automatic resource cleanup
- The "exception translation" pattern (wrap infrastructure exceptions)

### The Exception Hierarchy

```
Throwable
├── Error (JVM errors — don't catch these)
└── Exception
    ├── RuntimeException (unchecked — no need to declare or catch)
    │   ├── IllegalArgumentException
    │   ├── IllegalStateException
    │   ├── NullPointerException
    │   └── YourCustomRuntimeException
    └── IOException, SQLException, etc. (checked — MUST catch or declare)
```

### TypeScript vs Java Exception Handling

```typescript
// TypeScript — only unchecked exceptions, catch is optional
try {
  const result = riskyOperation();
} catch (e) {
  console.error(e instanceof Error ? e.message : e);
}
```

```java
// Java unchecked (RuntimeException) — same as TypeScript
try {
    String result = riskyOperation();
} catch (IllegalArgumentException e) {
    log.error("Bad input: {}", e.getMessage());
}

// Java CHECKED exception — compiler forces you to handle it
try {
    String content = Files.readString(Path.of("transactions.csv"));
} catch (IOException e) {
    throw new RuntimeException("Failed to read file", e); // wrap and re-throw
}

// OR declare it (propagates the obligation upward):
void loadFile(String path) throws IOException {
    Files.readString(Path.of(path));
}
```

### Try-With-Resources (like TypeScript's `using` or Python's `with`)

```java
// Resources that implement AutoCloseable are automatically closed:
try (
    var reader = new BufferedReader(new FileReader("transactions.csv"));
    var writer = new BufferedWriter(new FileWriter("output.csv"))
) {
    String line;
    while ((line = reader.readLine()) != null) {
        writer.write(processLine(line));
    }
} // reader and writer are closed here, even if exception is thrown
```

### Custom Domain Exceptions

```java
public class InsufficientFundsException extends RuntimeException {
    private final long availableBalance;
    private final long requestedAmount;

    public InsufficientFundsException(long availableBalance, long requestedAmount) {
        super("Insufficient funds: available %d, requested %d"
            .formatted(availableBalance, requestedAmount));
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
    }

    // Getters for structured error handling:
    public long getAvailableBalance() { return availableBalance; }
    public long getRequestedAmount() { return requestedAmount; }
}
```

### Your Task
1. Implement the 4 custom exception classes
2. Implement `TransactionValidator.java` — throws domain exceptions for invalid input
3. Implement `TransactionFileReader.java` — reads CSV files using try-with-resources

### Checkpoint ✅
All tests pass. Verify the `InsufficientFundsException` test: the exception carries both the available balance AND the requested amount as fields.

---

## Day 1 Complete — What You Built

Congratulations! You've implemented the core building blocks of a transaction classification engine:

```
TransactionCsvParser    →  parses "txn-001,2024-01-15,15099,USD,PURCHASE,Coffee"
       ↓
TransactionValidator    →  throws InsufficientFundsException / ValidationException
       ↓
Transaction (record)    →  immutable value object with equals/hashCode
       ↓
TransactionType (enum)  →  PURCHASE, REFUND, FEE with codes and credit flags
Currency (enum)         →  USD, EUR, BTC with symbols and decimal places
       ↓
TransactionLedger       →  queryable collection with groupBy and aggregation
       ↓
PaymentEvent (sealed)   →  Initiated → Processing → Settled | Failed
```

---

## Common Mistakes on Day 1

1. **Using `==` to compare Strings** → always use `.equals()`
2. **Using `double` for money** → always use `BigDecimal` or store as `long` cents
3. **Forgetting `BigDecimal` constructor uses String** → `new BigDecimal("0.1")` not `new BigDecimal(0.1)`
4. **Modifying a list while iterating** → use `removeIf()` or stream to a new list
5. **Missing `@Override` annotation** → won't cause a bug but IntelliJ will warn; always add it
6. **Not calling `useJUnitPlatform()` in `build.gradle.kts`** → tests won't run
7. **Package declaration doesn't match directory** → `package com.example.fintech.day1.tooling` must match the file path

---

## What's Coming in Day 2

Day 2 covers the **advanced language features** that make Java genuinely powerful:
- **Generics** — `<T extends Comparable<T>>` and wildcards (`? extends`, `? super`)
- **Streams mastery** — complex collectors, `flatMap`, `Collectors.toMap`, parallel streams
- **CompletableFuture** — async/await equivalent for concurrent exchange rate fetching
- **DateTime API** — `Instant`, `ZonedDateTime`, `Duration` for transaction timestamps
- **Optional deep dive** — when to use it, when it's overkill

The Day 2 exercise extends your transaction engine with a **multi-currency portfolio analyzer** that fetches exchange rates concurrently.

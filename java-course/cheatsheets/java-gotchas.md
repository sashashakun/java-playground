# Java Gotchas for TypeScript Developers

> The 20 things that will bite you in the first week.
> Bookmark this. You will need it.

---

## 1. `==` Compares References, Not Values (for Objects)

**The trap**: In JavaScript/TypeScript, `===` works for primitives AND objects (by reference). In Java, `==` on objects checks reference identity, not value.

```java
String a = new String("CHF");
String b = new String("CHF");

System.out.println(a == b);      // false! Different objects in heap
System.out.println(a.equals(b)); // true ✓

// WORKS for string literals (due to string pool):
String c = "CHF";
String d = "CHF";
System.out.println(c == d);      // true (same pooled object) — but don't rely on this!
```

**Rule**: Always use `.equals()` for String and Object comparison. Use `==` only for primitives and intentional reference equality checks.

---

## 2. `Integer` Cache Trap — `==` Fails Above 127

**The trap**: Java caches `Integer` objects for values -128 to 127. Above that, `==` returns `false`.

```java
Integer a = 127;
Integer b = 127;
System.out.println(a == b); // true (cached!)

Integer c = 128;
Integer d = 128;
System.out.println(c == d); // false! Different objects

// ALWAYS use:
System.out.println(c.equals(d)); // true ✓
```

**Why this matters**: If you have transaction counts or amounts stored as `Integer` (not `int`), comparison bugs will appear only for values > 127. Silent and maddening.

---

## 3. Autoboxing NullPointerException

**The trap**: Unboxing a `null` `Integer`/`Long`/`Boolean` to a primitive throws NPE.

```java
Map<String, Integer> balances = new HashMap<>();
// balances.get("wallet-123") returns null

int balance = balances.get("wallet-123"); // NullPointerException!

// Safe versions:
int balance = balances.getOrDefault("wallet-123", 0);
Integer raw = balances.get("wallet-123");
if (raw != null) { int b = raw; } // explicit null check
```

**Fintech impact**: Especially dangerous in balance calculations — a missing account returns null, which unboxes to NPE and crashes your transaction.

---

## 4. Checked Exceptions — The Java-Only Concept

**The trap**: Some Java exceptions are "checked" — the compiler forces you to handle them or declare them. There's no equivalent in TypeScript.

```java
// This won't compile:
public void readFile(String path) {
    Files.readAllBytes(Path.of(path)); // COMPILE ERROR: unhandled IOException
}

// Must either catch:
public void readFile(String path) {
    try {
        Files.readAllBytes(Path.of(path));
    } catch (IOException e) {
        throw new RuntimeException("Failed to read file", e);
    }
}

// Or declare:
public void readFile(String path) throws IOException {
    Files.readAllBytes(Path.of(path));
}
```

**Rule of thumb**: Catch checked exceptions at the boundary (service layer), wrap in unchecked exceptions (`RuntimeException`), and let them propagate. Don't declare `throws IOException` on every method in your call stack.

---

## 5. Type Erasure — Generics Don't Exist at Runtime

**The trap**: Java's generic type parameters are erased at runtime. You cannot do `instanceof List<String>`.

```java
// These won't compile:
if (obj instanceof List<String>) { }    // COMPILE ERROR
new ArrayList<String>().getClass()      // Returns ArrayList, not ArrayList<String>

// Also can't do:
T t = new T(); // COMPILE ERROR — T is unknown at runtime

// Workaround: pass the Class<T> token
public <T> T parse(String json, Class<T> type) {
    return objectMapper.readValue(json, type);
}
parse(json, Transaction.class); // explicit class token
```

**Why it exists**: Backward compatibility with Java 1.4 (pre-generics). You just have to know about it.

---

## 6. `Collections.unmodifiableList()` Is a VIEW, Not a Copy

**The trap**: The "unmodifiable" wrapper prevents mutation through the wrapper, but the original list can still be modified.

```java
List<Transaction> mutable = new ArrayList<>();
mutable.add(tx1);

List<Transaction> readOnly = Collections.unmodifiableList(mutable);

readOnly.add(tx2);  // UnsupportedOperationException ✓ (good)
mutable.add(tx2);   // Works fine — mutates the underlying list
// readOnly now contains tx2!

// True defensive copy:
List<Transaction> safe = List.copyOf(mutable); // Java 10+
// OR
List<Transaction> safe = new ArrayList<>(mutable); // old style
```

---

## 7. `Arrays.asList()` Returns a FIXED-SIZE List

**The trap**: `Arrays.asList()` returns a list backed by an array. You can update elements but CANNOT add or remove.

```java
List<String> currencies = Arrays.asList("USD", "EUR", "CHF");
currencies.set(0, "GBP");  // OK — updates element
currencies.add("JPY");     // UnsupportedOperationException!

// Use List.of() for truly immutable:
List<String> currencies = List.of("USD", "EUR", "CHF");

// Use new ArrayList<>() for mutable:
List<String> currencies = new ArrayList<>(List.of("USD", "EUR", "CHF"));
```

---

## 8. `ConcurrentModificationException` — Don't Modify While Iterating

**The trap**: Modifying a collection while iterating over it (with for-each or iterator) throws `ConcurrentModificationException`.

```java
List<Transaction> transactions = new ArrayList<>(loadTransactions());

// WRONG:
for (Transaction t : transactions) {
    if (t.getStatus() == FAILED) {
        transactions.remove(t); // ConcurrentModificationException!
    }
}

// RIGHT — removeIf:
transactions.removeIf(t -> t.getStatus() == FAILED);

// RIGHT — collect + reassign:
transactions = transactions.stream()
    .filter(t -> t.getStatus() != FAILED)
    .collect(Collectors.toCollection(ArrayList::new));

// RIGHT — Iterator explicitly:
Iterator<Transaction> it = transactions.iterator();
while (it.hasNext()) {
    if (it.next().getStatus() == FAILED) it.remove();
}
```

---

## 9. Integer Overflow Is Silent

**The trap**: Java integers overflow silently — no exception, no warning. Values just wrap around.

```java
int maxInt = Integer.MAX_VALUE; // 2,147,483,647
System.out.println(maxInt + 1); // -2,147,483,648 — wrapped!

// For financial amounts in cents, use long:
long amount = 9_999_999_999L; // 9.9 billion cents — fine as long

// For big money or crypto amounts, use BigDecimal:
BigDecimal cryptoAmount = new BigDecimal("21000000.00000000");
```

**Rule**: Transaction amounts → `long` (cents) or `BigDecimal`. Balance sums → `BigDecimal`. Never `int` for money.

---

## 10. `BigDecimal` — Use String Constructor, Not Double

**The trap**: `new BigDecimal(0.1)` doesn't give you 0.1 — it gives you the floating-point representation of 0.1.

```java
// WRONG — double precision problem:
BigDecimal fee = new BigDecimal(0.1);
System.out.println(fee); // 0.1000000000000000055511151231257827021181583404541015625

// RIGHT — use String constructor:
BigDecimal fee = new BigDecimal("0.1");
System.out.println(fee); // 0.1

// RIGHT — use constant:
BigDecimal fee = BigDecimal.valueOf(0.1); // Uses double's toString internally — safer
System.out.println(fee); // 0.1
```

---

## 11. `hashCode` / `equals` Contract

**The trap**: If you override `equals()`, you MUST override `hashCode()`. If two objects are `.equals()`, they must have the same `hashCode()`. Violation causes `HashMap` and `HashSet` to behave incorrectly.

```java
// BROKEN — equals without hashCode:
public class AccountId {
    private final String value;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AccountId other)) return false;
        return value.equals(other.value);
    }
    // Missing hashCode! HashMap lookups will fail.
}

// CORRECT:
@Override
public int hashCode() {
    return Objects.hash(value);
}

// Better: just use a record — it generates both:
public record AccountId(String value) {}
```

---

## 12. Default Visibility Is Package-Private, NOT `public`

**The trap**: In TypeScript, everything is effectively exported. In Java, if you write no modifier, the visibility is **package-private** — only accessible within the same package.

```java
class PaymentService { }         // package-private — NOT visible outside package
public class PaymentService { }  // public — visible everywhere
private class PaymentService { } // private — only in the same top-level class
```

**Rule**: Explicitly write `public` or `private`. Never rely on implicit package-private (except for test helper classes).

---

## 13. `String` Pool — `new String()` Bypasses It

**The trap**: String literals (`"hello"`) are interned in the string pool. Two literals with the same content point to the same object. `new String("hello")` creates a NEW object, bypassing the pool.

```java
String a = "hello";
String b = "hello";
System.out.println(a == b); // true — same pooled object

String c = new String("hello");
System.out.println(a == c); // false — c is a new heap object
System.out.println(a.equals(c)); // true ✓

// Force interning:
String d = c.intern(); // returns the pooled version
System.out.println(a == d); // true
```

**In practice**: Never use `new String("...")`. And always use `.equals()` anyway so none of this matters.

---

## 14. `switch` Falls Through by Default

**The trap**: Java's traditional `switch` statement falls through without `break`. TypeScript/JavaScript does the same, but Java developers are more likely to use switch for complex logic.

```java
// DANGER — falls through:
switch (status) {
    case PENDING:
        initiate();   // then falls through!
    case PROCESSING:
        process();    // runs for both PENDING and PROCESSING
        break;
    case SETTLED:
        settle();
        break;
}

// SAFE — switch expression (Java 14+), no fall-through:
String message = switch (status) {
    case PENDING     -> "Awaiting processing";
    case PROCESSING  -> "In flight";
    case SETTLED     -> "Complete";
    case FAILED      -> "Failed";
};
```

**Rule**: Always use switch expressions (`switch { case X -> ... }`) over switch statements. No fall-through, compiler checks exhaustiveness on sealed types.

---

## 15. `Optional` Is Not a Field Type

**The trap**: Using `Optional<T>` as a class field type. It's not serializable and wasn't designed for this.

```java
// WRONG:
public class Wallet {
    private Optional<String> label; // Don't do this
}

// RIGHT — just allow null and use Optional in method signatures:
public class Wallet {
    private String label; // nullable

    public Optional<String> getLabel() {
        return Optional.ofNullable(label);
    }
}
```

---

## 16. `LocalDateTime` Has No Timezone

**The trap**: `LocalDateTime` is a date-time without timezone. If you store it in a database, you may get surprising results when the JVM or DB timezone changes.

```java
LocalDateTime now = LocalDateTime.now(); // NO timezone — don't store this in DB!

// For DB storage, use:
Instant now = Instant.now(); // UTC epoch — always correct
OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC); // UTC with offset

// For display:
ZonedDateTime zurich = ZonedDateTime.now(ZoneId.of("Europe/Zurich"));
```

**Rule**: Store timestamps as `Instant` (UTC epoch) or `OffsetDateTime` (UTC). Use `ZonedDateTime` only for display/user-facing dates.

---

## 17. Static Fields Are Shared Across ALL Instances

**The trap**: Static fields belong to the class, not the instance. Mutation from one instance affects all.

```java
// DANGEROUS in a singleton service:
public class FraudDetector {
    private static int alertCount = 0; // shared across all threads!

    public void checkTransaction(Transaction t) {
        alertCount++; // race condition in multi-threaded app
    }
}

// Use AtomicInteger for thread-safe counting:
private static final AtomicInteger alertCount = new AtomicInteger(0);
alertCount.incrementAndGet(); // thread-safe
```

---

## 18. `for-each` Creates Iterators — Be Careful with Index

**The trap**: Java's for-each loop doesn't give you an index (unlike TypeScript's `for...of` with `entries()`).

```typescript
// TypeScript — easy:
for (const [i, tx] of transactions.entries()) {
    console.log(`${i}: ${tx.id}`);
}
```

```java
// Java — need explicit index:
for (int i = 0; i < transactions.size(); i++) {
    System.out.println(i + ": " + transactions.get(i).getId());
}

// Or with stream:
IntStream.range(0, transactions.size())
    .forEach(i -> System.out.println(i + ": " + transactions.get(i).getId()));
```

---

## 19. `final` on Objects ≠ Immutable

**The trap**: `final` on a reference means you can't reassign the variable. The object it points to can still be mutated.

```java
final List<Transaction> transactions = new ArrayList<>();
transactions = new ArrayList<>();   // COMPILE ERROR — can't reassign

transactions.add(new Transaction()); // FINE — mutates the list object!

// Immutable list:
final List<Transaction> transactions = List.of(tx1, tx2); // truly immutable
```

---

## 20. `int` Division Truncates — Always

**The trap**: Dividing two `int`s in Java always truncates, never rounds.

```java
int a = 7;
int b = 2;
System.out.println(a / b);     // 3 — truncates!
System.out.println(a % b);     // 1 — remainder

// For decimal result:
System.out.println((double) a / b); // 3.5

// For financial fee calculations — use BigDecimal with scale and rounding mode:
BigDecimal amount = new BigDecimal("7.00");
BigDecimal divisor = new BigDecimal("2");
BigDecimal result = amount.divide(divisor, 2, RoundingMode.HALF_EVEN); // 3.50

// NEVER use plain BigDecimal.divide() without rounding mode on non-terminating decimals:
new BigDecimal("1").divide(new BigDecimal("3")); // ArithmeticException: Non-terminating decimal expansion
new BigDecimal("1").divide(new BigDecimal("3"), 10, RoundingMode.HALF_EVEN); // 0.3333333333
```

---

## Bonus: `NullPointerException` Source Identification (Java 14+)

Java 14+ gives you helpful NPE messages that tell you exactly WHICH reference was null:

```
NullPointerException: Cannot invoke "String.length()" because "payment.getReference()" is null
```

vs the old useless:
```
NullPointerException
```

Enable with `-XX:+ShowCodeDetailsInExceptionMessages` (on by default in Java 17+).

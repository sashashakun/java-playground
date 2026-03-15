# TypeScript → Java Rosetta Stone

> 50+ side-by-side patterns. Use this as a lookup table when you know how to do
> something in TypeScript but can't remember the Java equivalent.

---

## Variables & Constants

| TypeScript | Java | Notes |
|-----------|------|-------|
| `let balance = 100;` | `int balance = 100;` | `let` → primitive type declaration |
| `let balance: number = 100;` | `int balance = 100;` | explicit type |
| `const MAX_RETRIES = 3;` | `final int MAX_RETRIES = 3;` | `const` → `final` (but `final` object fields are still mutable!) |
| `var x = 42;` | `var x = 42;` | Java 10+ `var` works only for local variables |
| `let amount: bigint = 1000n;` | `BigDecimal amount = new BigDecimal("1000");` | Always use `BigDecimal` for money, never `double` |
| `const obj = { ... } as const;` | `record Point(int x, int y) {}` | Immutable value objects → use `record` |

---

## Primitive Types

| TypeScript | Java | Notes |
|-----------|------|-------|
| `number` (integer use) | `int` / `long` | `int` = 32-bit, `long` = 64-bit. Use `long` for transaction amounts in cents |
| `number` (float use) | `double` | Never for money — use `BigDecimal` |
| `string` | `String` | Capital S — it's a class, not a primitive |
| `boolean` | `boolean` | Lowercase = primitive; `Boolean` (uppercase) = nullable wrapper |
| `bigint` | `BigInteger` / `BigDecimal` | `BigDecimal` for money, `BigInteger` for crypto amounts |
| `undefined` / `null` | `null` | Java has only `null`; use `Optional<T>` to express optionality explicitly |
| (no equivalent) | `byte`, `short`, `char` | Rarely used directly; come up in I/O and legacy code |

---

## Null & Optional Handling

| TypeScript | Java | Notes |
|-----------|------|-------|
| `value ?? "default"` | `Optional.ofNullable(value).orElse("default")` | Null coalescing |
| `value?.property` | `Optional.ofNullable(value).map(v -> v.property)` | Optional chaining |
| `T \| undefined` | `Optional<T>` | Java's Optional is explicit; don't use as field type |
| `T \| null` | `@Nullable T` (annotation) | Or just `T` — Java doesn't encode null in the type |
| `if (value !== null && value !== undefined)` | `if (value != null)` | Java `null` check |
| `value!` (non-null assertion) | `Objects.requireNonNull(value)` | Throws NPE immediately if null |
| `value !== undefined` | `value != null` | No `undefined` in Java |

```typescript
// TypeScript
const fee = transaction.fee ?? 0;
const currency = account?.wallet?.currency ?? "USD";
```

```java
// Java
int fee = transaction.getFee() != null ? transaction.getFee() : 0;
// Or with Optional:
String currency = Optional.ofNullable(account)
    .map(Account::getWallet)
    .map(Wallet::getCurrency)
    .orElse("USD");
```

---

## Strings

| TypeScript | Java | Notes |
|-----------|------|-------|
| `` `Hello ${name}` `` | `"Hello " + name` or `String.format("Hello %s", name)` | No template literals; use `+` or `String.format` |
| `` `Hello ${name}` `` | `"Hello %s".formatted(name)` | Java 15+ — `formatted()` on String |
| Multi-line template literal | Text block `"""..."""` | Java 15+ text blocks |
| `"hello".length` | `"hello".length()` | Method call, not property |
| `"hello".toUpperCase()` | `"hello".toUpperCase()` | Same! |
| `"hello".includes("ell")` | `"hello".contains("ell")` | `includes` → `contains` |
| `"hello".startsWith("he")` | `"hello".startsWith("he")` | Same! |
| `"hello".split(",")` | `"hello".split(",")` | Same! Returns array |
| `"  hello  ".trim()` | `"  hello  ".strip()` | Use `strip()` (Unicode-aware), not `trim()` |
| `String(42)` | `String.valueOf(42)` or `Integer.toString(42)` | Convert number to string |
| `parseInt("42")` | `Integer.parseInt("42")` | Parse string to int |
| `"a" === "b"` | `"a".equals("b")` | NEVER use `==` for String comparison! |
| `str.replace(/USD/g, "EUR")` | `str.replace("USD", "EUR")` | replaceAll for regex: `str.replaceAll("USD", "EUR")` |

```java
// Java text block (Java 15+) — great for SQL, JSON, HTML in tests
String json = """
    {
        "amount": 100,
        "currency": "CHF"
    }
    """;
```

---

## Arrays & Lists

| TypeScript | Java | Notes |
|-----------|------|-------|
| `number[]` or `Array<number>` | `int[]` (primitive) or `List<Integer>` (object) | Prefer `List<T>` over arrays in most cases |
| `[1, 2, 3]` | `List.of(1, 2, 3)` | Immutable list literal |
| `[1, 2, 3]` (mutable) | `new ArrayList<>(List.of(1, 2, 3))` | Mutable list |
| `arr.push(item)` | `list.add(item)` | Add to end |
| `arr.pop()` | `list.remove(list.size() - 1)` | Remove from end |
| `arr.length` | `list.size()` | Size property |
| `arr[0]` | `list.get(0)` | Index access |
| `arr.includes(item)` | `list.contains(item)` | Membership check |
| `arr.indexOf(item)` | `list.indexOf(item)` | Same! |
| `arr.map(fn)` | `list.stream().map(fn).toList()` | Java 16+ `.toList()` |
| `arr.filter(fn)` | `list.stream().filter(fn).toList()` | |
| `arr.reduce(fn, init)` | `list.stream().reduce(init, fn)` | |
| `arr.find(fn)` | `list.stream().filter(fn).findFirst()` | Returns `Optional<T>` |
| `arr.some(fn)` | `list.stream().anyMatch(fn)` | |
| `arr.every(fn)` | `list.stream().allMatch(fn)` | |
| `arr.forEach(fn)` | `list.forEach(fn)` | |
| `[...arr1, ...arr2]` | `Stream.concat(arr1.stream(), arr2.stream()).toList()` | |
| `arr.sort((a,b) => a-b)` | `list.sort(Comparator.naturalOrder())` | |
| `arr.reverse()` | `Collections.reverse(list)` | Mutates in-place |
| `arr.slice(1, 3)` | `list.subList(1, 3)` | Returns a view |
| `arr.flat()` | `list.stream().flatMap(Collection::stream).toList()` | |

---

## Objects & Maps

| TypeScript | Java | Notes |
|-----------|------|-------|
| `{ key: value }` | `Map.of("key", value)` | Immutable map literal (max 10 entries) |
| `new Map()` | `new HashMap<>()` | Mutable map |
| `map.set("k", v)` | `map.put("k", v)` | |
| `map.get("k")` | `map.get("k")` | Returns `null` if missing, not `undefined` |
| `map.has("k")` | `map.containsKey("k")` | |
| `map.delete("k")` | `map.remove("k")` | |
| `map.size` | `map.size()` | |
| `Object.keys(obj)` | `map.keySet()` | Returns `Set<K>` |
| `Object.values(obj)` | `map.values()` | Returns `Collection<V>` |
| `Object.entries(obj)` | `map.entrySet()` | Returns `Set<Map.Entry<K,V>>` |
| `{ ...obj, extra: 1 }` | No spread — use Builder or `new HashMap<>(existing)` + `put` | |
| `map.get("k") ?? "default"` | `map.getOrDefault("k", "default")` | |

---

## Sets

| TypeScript | Java | Notes |
|-----------|------|-------|
| `new Set([1, 2, 3])` | `Set.of(1, 2, 3)` | Immutable |
| `new Set()` | `new HashSet<>()` | Mutable, unordered |
| `set.add(item)` | `set.add(item)` | Same! |
| `set.has(item)` | `set.contains(item)` | |
| `set.delete(item)` | `set.remove(item)` | |
| `set.size` | `set.size()` | |
| (ordered) | `new LinkedHashSet<>()` | Maintains insertion order |
| (sorted) | `new TreeSet<>()` | Sorted by natural order |

---

## Functions & Methods

| TypeScript | Java | Notes |
|-----------|------|-------|
| `function add(a: number, b: number): number { return a + b; }` | `int add(int a, int b) { return a + b; }` | Return type before name, no `function` keyword |
| `const add = (a: number, b: number) => a + b;` | `BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;` | Lambda assigned to functional interface |
| `(x: number) => x * 2` | `x -> x * 2` | Lambda expression |
| `(x: number, y: number) => x + y` | `(x, y) -> x + y` | Multi-param lambda |
| `() => { doSomething(); }` | `() -> doSomething()` | Void lambda |
| `function(...args: number[])` | `void method(int... args)` | Varargs |
| `fn(arg1, ...arr)` | No spread into method call — use overloads or `List` | |

---

## Classes

| TypeScript | Java | |
|-----------|------|-|
| `class Wallet { }` | `class Wallet { }` | Same structure |
| `private balance: number = 0;` | `private int balance = 0;` | Type before name, not after |
| `constructor(private id: string) {}` | No shorthand — must write field + constructor explicitly | |
| `get balance(): number { ... }` | No property syntax — `getBalance()` method | Java getters are methods by convention |
| `set balance(v: number) { ... }` | `setBalance(int v) { ... }` | Java setters are methods |
| `readonly id: string` | `private final String id;` | `final` field |
| `static count = 0;` | `private static int count = 0;` | Static field |
| `static getInstance() { ... }` | `public static Wallet getInstance() { ... }` | Static method |
| `extends` | `extends` (single only!) | Java has no multiple inheritance |
| `implements` | `implements` (multiple OK) | Java interfaces: multiple allowed |
| `abstract class` | `abstract class` | Same concept |
| `interface` | `interface` | Java interfaces can have `default` method implementations |
| `#privateField` | `private field` | Java uses access modifiers, not `#` |
| `toString()` | `@Override public String toString()` | Override `Object.toString()` |

```typescript
// TypeScript class
class Transaction {
  constructor(
    private readonly id: string,
    private readonly amount: number,
    private status: TransactionStatus
  ) {}

  get isSettled(): boolean {
    return this.status === TransactionStatus.SETTLED;
  }
}
```

```java
// Java equivalent
public final class Transaction {
    private final String id;
    private final BigDecimal amount;
    private TransactionStatus status;

    public Transaction(String id, BigDecimal amount, TransactionStatus status) {
        this.id = id;
        this.amount = amount;
        this.status = status;
    }

    public boolean isSettled() {
        return status == TransactionStatus.SETTLED;
    }

    public String getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public TransactionStatus getStatus() { return status; }
}

// Or as a record (if fully immutable):
public record Transaction(String id, BigDecimal amount, TransactionStatus status) {
    public boolean isSettled() {
        return status == TransactionStatus.SETTLED;
    }
}
```

---

## Interfaces & Type Aliases

| TypeScript | Java | Notes |
|-----------|------|-------|
| `interface PaymentGateway { charge(): void; }` | `interface PaymentGateway { void charge(); }` | Same concept, different syntax |
| `type UserId = string;` | No direct equivalent — use `record UserId(String value) {}` | Type aliases → wrapper records |
| `type Amount = { value: number; currency: string; }` | `record Amount(BigDecimal value, String currency) {}` | Data types → records |
| `type PaymentEvent = Initiated \| Settled \| Failed` | `sealed interface PaymentEvent permits Initiated, Settled, Failed {}` | Discriminated unions → sealed classes |
| Structural typing (duck typing) | Nominal typing (explicit `implements`) | Java REQUIRES explicit declaration |
| `Partial<T>` | No direct equivalent — use Builder or dedicated request objects | |
| `Required<T>` | Default — all fields required unless `Optional<T>` | |
| `Pick<T, 'id' \| 'name'>` | No equivalent — create a new class/record | |
| `Readonly<T>` | `record T(...)` or `final` fields | Use records for immutable data |

---

## Generics

| TypeScript | Java | Notes |
|-----------|------|-------|
| `function identity<T>(x: T): T` | `<T> T identity(T x)` | Type param before return type |
| `class Box<T> { }` | `class Box<T> { }` | Same! |
| `T extends Comparable<T>` | `T extends Comparable<T>` | Same! (upper bound) |
| `T extends A & B` | `T extends A & B` | Same! |
| (no TS equivalent) | `? extends T` | Upper-bounded wildcard (read-only) |
| (no TS equivalent) | `? super T` | Lower-bounded wildcard (write-only) |
| `Array<T>` at runtime | `List<T>` erased to `List<Object>` at runtime | **Type erasure** — `instanceof List<String>` doesn't compile |
| `instanceof` | `instanceof` + pattern matching: `if (obj instanceof String s)` | Java 16+ pattern matching |

---

## Async / Concurrency

| TypeScript | Java | Notes |
|-----------|------|-------|
| `Promise<T>` | `CompletableFuture<T>` | Java's async primitive |
| `async function fetchRate(): Promise<Rate>` | `CompletableFuture<Rate> fetchRate()` | No `async` keyword — method returns CF |
| `await promise` | `.join()` or `.get()` | **Blocks current thread** — use carefully |
| `Promise.resolve(value)` | `CompletableFuture.completedFuture(value)` | |
| `Promise.reject(error)` | `CompletableFuture.failedFuture(error)` | |
| `Promise.all([p1, p2])` | `CompletableFuture.allOf(cf1, cf2)` | |
| `Promise.race([p1, p2])` | `CompletableFuture.anyOf(cf1, cf2)` | |
| `.then(fn)` | `.thenApply(fn)` | Transform the value (like `.map()`) |
| `.then(async fn)` | `.thenCompose(fn)` | Flat-map (fn returns a CF) |
| `.then(void fn)` | `.thenAccept(fn)` | Consume the value |
| `.catch(fn)` | `.exceptionally(fn)` | Handle errors |
| `setTimeout(fn, 1000)` | `ScheduledExecutorService.schedule(fn, 1, TimeUnit.SECONDS)` | |
| Web Workers | `Thread` / `ExecutorService` | Java is truly multi-threaded |
| Single-threaded event loop | Multi-threaded + virtual threads (Java 21) | Completely different model |

```typescript
// TypeScript async
async function fetchExchangeRate(currency: string): Promise<number> {
  const response = await fetch(`/rates/${currency}`);
  return response.json();
}

const [usdRate, eurRate] = await Promise.all([
  fetchExchangeRate("USD"),
  fetchExchangeRate("EUR"),
]);
```

```java
// Java CompletableFuture
CompletableFuture<BigDecimal> fetchExchangeRate(String currency) {
    return CompletableFuture.supplyAsync(() -> {
        // HTTP call here
        return rateApiClient.getRate(currency);
    });
}

CompletableFuture<BigDecimal> usdRate = fetchExchangeRate("USD");
CompletableFuture<BigDecimal> eurRate = fetchExchangeRate("EUR");

CompletableFuture.allOf(usdRate, eurRate).join();
BigDecimal usd = usdRate.join(); // safe after allOf
BigDecimal eur = eurRate.join();
```

---

## Error Handling

| TypeScript | Java | Notes |
|-----------|------|-------|
| `throw new Error("msg")` | `throw new RuntimeException("msg")` | Unchecked |
| `throw new Error("msg")` | `throw new IllegalArgumentException("msg")` | For bad input |
| `try { } catch (e) { }` | `try { } catch (Exception e) { }` | Must specify the type |
| `catch (e: unknown)` | `catch (SpecificException e)` | Java uses typed catches |
| Multi-catch | `catch (IOException \| SQLException e)` | Multi-catch with `\|` |
| `finally { }` | `finally { }` | Same |
| `try/catch/finally` | `try-with-resources`: `try (Resource r = new Resource())` | Auto-closes resources |
| Custom error class | `class InsufficientFundsException extends RuntimeException` | Unchecked custom exception |
| Checked error (no TS equiv) | `class SettlementException extends Exception` | Must be declared or caught |
| `e.message` | `e.getMessage()` | |
| `e.stack` | `e.getStackTrace()` / `e.printStackTrace()` | |

---

## Modules & Packages

| TypeScript | Java | Notes |
|-----------|------|-------|
| `export class Foo { }` | `public class Foo { }` (in its own file) | Every public class = its own file |
| `export default Foo` | No direct equivalent — use `public` | |
| `import { Foo } from './foo'` | `import com.example.payments.Foo;` | Full package path |
| `import * as utils from './utils'` | `import com.example.utils.*;` | Wildcard import (avoid in production) |
| npm package | Maven/Gradle dependency in `build.gradle.kts` | |
| `node_modules/` | `~/.gradle/caches/` | Dependencies cached locally |
| `package.json` | `build.gradle.kts` | Build config + dependency declaration |
| `tsconfig.json` | `build.gradle.kts` (Java version config) | Compiler settings in Gradle |
| `src/` | `src/main/java/` | Maven standard layout |
| `__tests__/` | `src/test/java/` | Test source root |

```kotlin
// build.gradle.kts — the combined package.json + tsconfig.json of Java
plugins {
    java
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}
```

---

## Enums

| TypeScript | Java | Notes |
|-----------|------|-------|
| `enum Direction { UP, DOWN }` | `enum Direction { UP, DOWN }` | Similar basic syntax |
| `enum Status { PENDING = "PENDING" }` | `enum Status { PENDING }` (String value is `.name()`) | Java enums have `.name()` and `.ordinal()` |
| TS enum is just object | Java enum is a **full class** with constructors, methods, fields | Huge difference! |
| No constructor on TS enums | `enum Currency { USD("$", 2), EUR("€", 2); }` | Java enums can have constructor params |

```java
// Java enum as a rich class:
public enum TransactionType {
    DEPOSIT("DEP", true),
    WITHDRAWAL("WDR", false),
    TRANSFER("TRF", false),
    FEE("FEE", false);

    private final String code;
    private final boolean isCredit;

    TransactionType(String code, boolean isCredit) {
        this.code = code;
        this.isCredit = isCredit;
    }

    public String getCode() { return code; }
    public boolean isCredit() { return isCredit; }
}
```

---

## Discriminated Unions → Sealed Classes

```typescript
// TypeScript discriminated union
type PaymentEvent =
  | { type: "INITIATED"; amount: number; currency: string }
  | { type: "SETTLED"; settledAt: Date; txHash: string }
  | { type: "FAILED"; reason: string; code: number };

function handle(event: PaymentEvent): string {
  switch (event.type) {
    case "INITIATED": return `Initiating ${event.amount} ${event.currency}`;
    case "SETTLED":   return `Settled: ${event.txHash}`;
    case "FAILED":    return `Failed: ${event.reason}`;
  }
}
```

```java
// Java sealed classes + records + switch expression
public sealed interface PaymentEvent
    permits PaymentInitiated, PaymentSettled, PaymentFailed {}

public record PaymentInitiated(BigDecimal amount, String currency) implements PaymentEvent {}
public record PaymentSettled(Instant settledAt, String txHash) implements PaymentEvent {}
public record PaymentFailed(String reason, int code) implements PaymentEvent {}

String handle(PaymentEvent event) {
    return switch (event) {
        case PaymentInitiated e -> "Initiating %s %s".formatted(e.amount(), e.currency());
        case PaymentSettled e   -> "Settled: " + e.txHash();
        case PaymentFailed e    -> "Failed: " + e.reason();
    };
}
```

---

## Dependency Injection

| TypeScript (NestJS) | Java (Spring) | Notes |
|--------------------|---------------|-------|
| `@Injectable()` class | `@Service` or `@Component` class | Spring detects and manages the bean |
| Constructor injection (NestJS preferred) | Constructor injection (Spring preferred) | Same pattern! |
| `@Inject()` | `@Autowired` (or omit — Spring auto-injects single constructors) | |
| Module providers | `@Configuration` class with `@Bean` methods | Explicit bean registration |
| `@Module({ imports: [OtherModule] })` | `@Import(OtherConfig.class)` | Import other config |

```java
// Spring DI — constructor injection (preferred)
@Service
public class PaymentService {
    private final PaymentRepository repository;
    private final FraudDetector fraudDetector;

    // @Autowired is optional when there's a single constructor (Spring 4.3+)
    public PaymentService(PaymentRepository repository, FraudDetector fraudDetector) {
        this.repository = repository;
        this.fraudDetector = fraudDetector;
    }
}
```

---

## JSON Serialization

| TypeScript | Java (Jackson) | Notes |
|-----------|----------------|-------|
| `JSON.stringify(obj)` | `objectMapper.writeValueAsString(obj)` | |
| `JSON.parse(str) as T` | `objectMapper.readValue(str, T.class)` | Type-safe |
| `interface Foo { bar: string }` | `record Foo(String bar) {}` or class with fields | |
| `@JsonProperty` decorator (rare) | `@JsonProperty("bar")` field annotation | Rename JSON key |
| `undefined` fields omitted | `@JsonInclude(NON_NULL)` to omit null fields | Explicit in Java |

---

## Testing

| TypeScript (Jest) | Java (JUnit 5 + AssertJ) | Notes |
|------------------|--------------------------|-------|
| `describe("Wallet", () => { })` | `@Nested class WalletTest { }` | Grouping |
| `it("should...", () => { })` | `@Test void should...() { }` | Test method |
| `expect(x).toBe(y)` | `assertThat(x).isEqualTo(y)` | AssertJ fluent assertion |
| `expect(x).toEqual(y)` | `assertThat(x).isEqualTo(y)` | Deep equality |
| `expect(x).toBeTruthy()` | `assertThat(x).isTrue()` | |
| `expect(x).toBeNull()` | `assertThat(x).isNull()` | |
| `expect(fn).toThrow(Error)` | `assertThatThrownBy(fn).isInstanceOf(Exception.class)` | |
| `jest.fn()` | `Mockito.mock(Service.class)` | Mock object |
| `jest.spyOn(obj, 'method').mockReturnValue(v)` | `when(mock.method()).thenReturn(v)` | Stub a call |
| `expect(mock.fn).toHaveBeenCalledWith(arg)` | `verify(mock).method(arg)` | Verify call |
| `beforeEach(() => { })` | `@BeforeEach void setUp() { }` | Setup |
| `afterEach(() => { })` | `@AfterEach void tearDown() { }` | Teardown |
| `test.each(cases)` | `@ParameterizedTest @MethodSource("cases")` | Parameterized tests |

---

## Stream / Array Pipeline Comparison

```typescript
// TypeScript
const totalFees = transactions
  .filter(t => t.type === "FEE")
  .map(t => t.amount)
  .reduce((sum, amt) => sum + amt, 0);
```

```java
// Java
BigDecimal totalFees = transactions.stream()
    .filter(t -> t.getType() == TransactionType.FEE)
    .map(Transaction::getAmount)
    .reduce(BigDecimal.ZERO, BigDecimal::add);
```

```typescript
// Group by currency
const byCurrency = transactions.reduce((acc, t) => {
  (acc[t.currency] ??= []).push(t);
  return acc;
}, {} as Record<string, Transaction[]>);
```

```java
// Java
Map<String, List<Transaction>> byCurrency = transactions.stream()
    .collect(Collectors.groupingBy(Transaction::getCurrency));
```

---

## Quick-Reference: Common Traps

| You might write (thinking TS) | Actually need in Java |
|-------------------------------|----------------------|
| `str1 == str2` | `str1.equals(str2)` |
| `list[0]` | `list.get(0)` |
| `list.length` | `list.size()` |
| `map["key"]` | `map.get("key")` |
| `obj.field` (public access) | `obj.getField()` (encapsulated) |
| `catch (e)` | `catch (SpecificException e)` |
| `console.log(x)` | `System.out.println(x)` or `log.info("{}", x)` |
| `process.exit(1)` | `System.exit(1)` |
| `Math.round(x)` | `Math.round(x)` — same! |
| `Math.floor(x)` | `(long) Math.floor(x)` or `(int) x` for truncation |

# Day 2: Java Advanced Language

> **Theme**: Build a Multi-Currency Portfolio Analyzer
> **Total estimated time**: 6–8 hours
> **Difficulty**: 🟡🟢🟡🟡🟢🟡🟢🔴 (per exercise)

By the end of today, you'll have built a complete portfolio analyzer that loads assets in multiple currencies, fetches exchange rates **concurrently**, computes valuations, and serializes results to JSON — covering the advanced language features that make Java genuinely powerful.

---

## What You're Building

A **Multi-Currency Portfolio Analyzer**:

```
Portfolio (BTC, ETH, USD, EUR assets)
    ↓
ExchangeRateService (fetches rates concurrently via CompletableFuture)
    ↓
PortfolioAnalyzer (Stream pipelines, groupingBy, reduce)
    ↓
PortfolioReport (JSON serialization, date ranges, performance)
```

Each exercise adds one layer. By Exercise 08, you connect them all.

---

## Exercise 01 — Generics & Wildcards 🟡

**Estimated time**: 60–75 min | **File**: `exercises/01-generics-and-wildcards/`

### Learning Goals
- Generic classes and bounded type parameters (`<T extends Comparable<T>>`)
- Upper-bounded wildcards (`? extends T`) — read-only, covariant
- Lower-bounded wildcards (`? super T`) — write-only, contravariant
- The PECS rule: **P**roducer **E**xtends, **C**onsumer **S**uper

### Theory: Why Generics?

```java
// Without generics — unsafe:
List prices = new ArrayList();
prices.add("oops"); // compiles — runtime ClassCastException later
Double p = (Double) prices.get(0);

// With generics — type-safe at compile time:
List<Double> prices = new ArrayList<>();
prices.add("oops"); // COMPILE ERROR — caught immediately
Double p = prices.get(0); // no cast needed
```

### Wildcards — The PECS Rule

```java
// ? extends T — you can READ from it (it's a Producer of T)
double sum(List<? extends Number> numbers) {
    double total = 0;
    for (Number n : numbers) total += n.doubleValue(); // OK — reading
    numbers.add(3.14); // COMPILE ERROR — can't write
    return total;
}
sum(new ArrayList<Integer>());  // accepts List<Integer>
sum(new ArrayList<Double>());   // accepts List<Double>

// ? super T — you can WRITE to it (it's a Consumer of T)
void addDefaults(List<? super Integer> list) {
    list.add(0); // OK — writing Integer is safe
    Integer x = list.get(0); // COMPILE ERROR — can't read as Integer
}
addDefaults(new ArrayList<Integer>()); // accepts List<Integer>
addDefaults(new ArrayList<Number>());  // accepts List<Number>
```

### Your Task
Implement `GenericRepository<T>` and `PortfolioUtils` with wildcard methods.

### Checkpoint ✅
`./gradlew test` green. Verify that `sumValues(List<? extends Number>)` accepts both `List<Integer>` and `List<Double>`.

---

## Exercise 02 — Functional Interfaces & Lambdas 🟢

**Estimated time**: 45–60 min | **File**: `exercises/02-functional-interfaces-and-lambdas/`

### Learning Goals
- Built-in functional interfaces: `Function<T,R>`, `Predicate<T>`, `Consumer<T>`, `Supplier<T>`, `BiFunction<T,U,R>`
- Method references: `ClassName::method`, `instance::method`, `ClassName::new`
- Composing functions: `andThen`, `compose`, `Predicate.and/or/negate`
- Custom `@FunctionalInterface` annotations

### Built-in Functional Interfaces

```java
// Function<T, R> — takes T, returns R
Function<String, BigDecimal> parse = BigDecimal::new;
Function<BigDecimal, String> format = bd -> bd.toPlainString();
Function<String, String> parseAndFormat = parse.andThen(format);

// Predicate<T> — takes T, returns boolean
Predicate<BigDecimal> isPositive = bd -> bd.compareTo(BigDecimal.ZERO) > 0;
Predicate<BigDecimal> isLarge    = bd -> bd.compareTo(new BigDecimal("10000")) > 0;
Predicate<BigDecimal> isLargePositive = isPositive.and(isLarge);

// Consumer<T> — takes T, returns void
Consumer<String> log  = System.out::println;
Consumer<String> audit = msg -> auditLog.add(msg);
Consumer<String> logAndAudit = log.andThen(audit);

// Supplier<T> — takes nothing, returns T
Supplier<BigDecimal> zero = BigDecimal::ZERO;   // method ref to static field? No — use:
Supplier<BigDecimal> zero = () -> BigDecimal.ZERO;
```

### Method References — 4 Forms

```java
// 1. Static method ref
Function<String, Integer>  parseInt   = Integer::parseInt;

// 2. Instance method ref on a specific instance
String prefix = "TXN-";
Predicate<String> hasPrefix = prefix::startsWith;  // wrong direction
Predicate<String> startsWith = s -> s.startsWith(prefix);  // clearer

// 3. Instance method ref on arbitrary instance of a type
Function<String, String>   toUpper    = String::toUpperCase;
Function<String, Integer>  strLength  = String::length;

// 4. Constructor ref
Supplier<ArrayList<String>> newList   = ArrayList::new;
Function<String, BigDecimal> newBD    = BigDecimal::new;
```

### Your Task
Implement `AssetTransformer` — a pipeline that applies chains of `Function`, `Predicate`, and `Consumer` operations to asset data using lambdas and method references.

### Checkpoint ✅
All tests pass. Make sure the `compose` vs `andThen` test passes — they apply functions in opposite orders.

---

## Exercise 03 — Streams Mastery 🟡

**Estimated time**: 60–75 min | **File**: `exercises/03-streams-mastery/`

### Learning Goals
- `Collectors.groupingBy` with downstream collectors
- `Collectors.toMap` with merge function for duplicate keys
- `flatMap` for nested collections
- `Collectors.summarizingLong` / `summarizingDouble` — stats in one pass
- Parallel streams — when they help, when they hurt

### Advanced Collectors

```java
// groupingBy + counting
Map<String, Long> countByCurrency = assets.stream()
    .collect(Collectors.groupingBy(Asset::currency, Collectors.counting()));

// groupingBy + summingLong
Map<String, Long> totalByCurrency = assets.stream()
    .collect(Collectors.groupingBy(
        Asset::currency,
        Collectors.summingLong(Asset::valueInCents)));

// groupingBy + mapping + toList (transform before collecting)
Map<String, List<String>> idsByCurrency = assets.stream()
    .collect(Collectors.groupingBy(
        Asset::currency,
        Collectors.mapping(Asset::id, Collectors.toList())));

// toMap with merge function (handles duplicate keys)
Map<String, BigDecimal> rateMap = rates.stream()
    .collect(Collectors.toMap(
        Rate::currency,
        Rate::value,
        (existing, replacement) -> replacement)); // last wins

// flatMap — flatten nested lists
List<String> allTags = portfolios.stream()
    .flatMap(p -> p.tags().stream())  // each portfolio has List<String> tags
    .distinct()
    .sorted()
    .toList();
```

### Your Task
Implement `PortfolioAnalytics` — 8 stream pipeline methods over a portfolio of assets.

### Checkpoint ✅
All tests pass. The `summarizeByType` test should produce correct min/max/sum/count stats.

---

## Exercise 04 — Optional In Depth 🟡

**Estimated time**: 45–60 min | **File**: `exercises/04-optional-in-depth/`

### Learning Goals
- `Optional.map`, `flatMap`, `filter`, `or`, `orElseGet`, `ifPresentOrElse`
- When `Optional` is appropriate vs overkill
- The "railway" pattern — chaining optional operations without null checks

### Optional Method Map

```java
Optional<String> name = Optional.ofNullable(user.getName());

// map — transform if present
Optional<Integer> len = name.map(String::length);

// flatMap — when the mapping fn itself returns Optional
Optional<User> owner = Optional.ofNullable(wallet.getOwner());
Optional<String> ownerName = owner.flatMap(u -> Optional.ofNullable(u.getName()));

// filter — keep only if condition holds
Optional<String> longName = name.filter(n -> n.length() > 3);

// or — provide alternative Optional (Java 9+)
Optional<String> result = name.or(() -> Optional.of("Anonymous"));

// orElseGet — lazily compute fallback (vs orElse which always evaluates)
String displayName = name.orElseGet(() -> computeDefaultName()); // lazy
String eager = name.orElse(computeDefaultName()); // ALWAYS calls computeDefaultName!

// ifPresentOrElse — branch on presence (Java 9+)
name.ifPresentOrElse(
    n -> System.out.println("Hello " + n),
    () -> System.out.println("Hello stranger")
);
```

### When NOT to Use Optional

```java
// ❌ Don't use as field type — not serializable
class Wallet { private Optional<String> label; }

// ❌ Don't use for primitive types — use OptionalInt/OptionalLong instead
Optional<Integer> count = ...; // boxing overhead → use OptionalInt

// ❌ Don't use in collections — just use an empty collection
Optional<List<Transaction>> txs = ...; // → just return empty List

// ✅ DO use as method return type to signal "might not exist"
Optional<Wallet> findWallet(String id) { ... }
```

### Your Task
Implement `PortfolioLookup` — 6 methods that chain `Optional` operations to navigate a portfolio data structure without null checks.

---

## Exercise 05 — DateTime API 🟢

**Estimated time**: 45–60 min | **File**: `exercises/05-datetime-api/`

### Learning Goals
- `Instant` — a point in time (UTC epoch), always use for DB storage
- `LocalDate` — a date with no time or zone
- `ZonedDateTime` — date+time+zone, for user-facing display
- `Duration` — time-based amount (hours, minutes, seconds)
- `Period` — date-based amount (days, months, years)
- `DateTimeFormatter` — parsing and formatting

### The DateTime Type Decision Tree

```
Do you need a specific zone?
  YES → ZonedDateTime ("2024-01-15T10:30:00+01:00[Europe/Zurich]")
  NO  → Does it have time?
          YES → Does it represent a moment in UTC?
                  YES → Instant ("2024-01-15T09:30:00Z")
                  NO  → LocalDateTime (rare — avoids zones but loses info)
          NO  → LocalDate ("2024-01-15")

For DB/API storage: ALWAYS use Instant or OffsetDateTime
For user display: ZonedDateTime
For business logic (billing cycle, age): LocalDate + Period
For durations: Duration (time-based) or Period (date-based)
```

### Key Operations

```java
Instant now = Instant.now();
Instant later = now.plus(Duration.ofHours(24));
long secondsBetween = Duration.between(now, later).toSeconds();

LocalDate today = LocalDate.now();
LocalDate nextMonth = today.plusMonths(1);
Period age = Period.between(LocalDate.of(1990, 1, 1), today);
int years = age.getYears();

// Timezone-aware
ZonedDateTime zurich = ZonedDateTime.now(ZoneId.of("Europe/Zurich"));
ZonedDateTime utc = zurich.withZoneSameInstant(ZoneOffset.UTC);

// Formatting
DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");
String display = zurich.format(fmt);   // "2024-01-15 10:30 CET"
LocalDate parsed = LocalDate.parse("2024-01-15"); // ISO-8601 default
```

### Your Task
Implement `TradeTimestampService` — methods for computing settlement windows, business day offsets, and formatting trade timestamps for multiple timezones.

---

## Exercise 06 — I/O & Serialization 🟡

**Estimated time**: 60–75 min | **File**: `exercises/06-io-and-serialization/`

### Learning Goals
- `Path` and `Files` API — modern file I/O
- `BufferedReader`/`BufferedWriter` — streaming large files
- Jackson `ObjectMapper` — JSON serialization/deserialization
- `@JsonProperty`, `@JsonIgnore`, `@JsonInclude`

### Modern File I/O

```java
// Read entire file as String (small files)
String content = Files.readString(Path.of("portfolio.json"));

// Write string to file
Files.writeString(Path.of("output.json"), json, StandardOpenOption.CREATE);

// Stream lines lazily (large files)
try (Stream<String> lines = Files.lines(Path.of("transactions.csv"))) {
    lines.filter(l -> !l.startsWith("#"))
         .map(this::parseLine)
         .forEach(this::process);
}

// Check existence, create directories
Path dir = Path.of("output", "reports");
Files.createDirectories(dir);
boolean exists = Files.exists(dir);
```

### Jackson Basics

```java
ObjectMapper mapper = new ObjectMapper()
    .registerModule(new JavaTimeModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

// Serialize
String json = mapper.writeValueAsString(portfolio);

// Deserialize
Portfolio loaded = mapper.readValue(json, Portfolio.class);
List<Asset> assets = mapper.readValue(json,
    new TypeReference<List<Asset>>() {}); // for generic types

// Annotations on records:
public record Asset(
    @JsonProperty("asset_id") String id,
    @JsonIgnore String internalCode,
    BigDecimal value
) {}
```

### Your Task
Implement `PortfolioSerializer` — read a portfolio from JSON, write it back, and handle file I/O with proper resource management.

---

## Exercise 07 — Annotations & Packages 🟢

**Estimated time**: 30–45 min | **File**: `exercises/07-annotations-and-packages/`

### Learning Goals
- Custom annotation declarations (`@interface`)
- Retention policies: `SOURCE`, `CLASS`, `RUNTIME`
- Reading annotations at runtime via reflection
- Package organization conventions

### Custom Annotations

```java
// Declare an annotation
@Retention(RetentionPolicy.RUNTIME)  // available at runtime via reflection
@Target(ElementType.METHOD)          // can only be placed on methods
public @interface Audited {
    String action() default "UNKNOWN";
    boolean logResult() default false;
}

// Use it
@Audited(action = "WITHDRAW", logResult = true)
public void withdraw(BigDecimal amount) { ... }

// Read it at runtime
Method method = WalletService.class.getMethod("withdraw", BigDecimal.class);
Audited annotation = method.getAnnotation(Audited.class);
if (annotation != null) {
    System.out.println("Action: " + annotation.action());
}
```

### Your Task
Define `@RateLimit` and `@Idempotent` annotations, then implement a `AnnotationScanner` that inspects methods at runtime.

---

## Exercise 08 — Concurrency Foundations 🔴

**Estimated time**: 75–90 min | **File**: `exercises/08-concurrency-foundations/`

### Learning Goals
- `CompletableFuture` — Java's async primitive
- `ExecutorService` and thread pools
- `CompletableFuture.allOf` — fan-out concurrent operations
- Thread safety: `AtomicLong`, `ConcurrentHashMap`
- The complete Portfolio Analyzer — ties all Day 2 concepts together

### CompletableFuture — The Full API

```java
// Create
CompletableFuture<BigDecimal> rate =
    CompletableFuture.supplyAsync(() -> fetchRate("USD"), executor);

// Transform (like Promise.then)
CompletableFuture<String> display = rate.thenApply(r -> "$" + r.toPlainString());

// Chain (flat-map — when mapping fn returns CF)
CompletableFuture<Portfolio> portfolio =
    fetchUser(id).thenCompose(user -> fetchPortfolio(user.portfolioId()));

// Fan out — wait for ALL
CompletableFuture<BigDecimal> usd = fetchRate("USD");
CompletableFuture<BigDecimal> eur = fetchRate("EUR");
CompletableFuture<BigDecimal> btc = fetchRate("BTC");

CompletableFuture.allOf(usd, eur, btc)
    .thenRun(() -> {
        // All completed — safe to .join()
        BigDecimal usdRate = usd.join();
        BigDecimal eurRate = eur.join();
    });

// Error handling
rate.exceptionally(ex -> {
    log.error("Rate fetch failed", ex);
    return BigDecimal.ONE; // fallback
});

// Timeout (Java 9+)
rate.orTimeout(5, TimeUnit.SECONDS)
    .exceptionally(ex -> BigDecimal.ONE);
```

### Thread Safety

```java
// AtomicLong — lock-free counter (preferred over synchronized for simple counts)
AtomicLong requestCount = new AtomicLong(0);
requestCount.incrementAndGet();  // thread-safe

// ConcurrentHashMap — thread-safe map
ConcurrentHashMap<String, BigDecimal> rateCache = new ConcurrentHashMap<>();
rateCache.putIfAbsent("USD", BigDecimal.ONE);
rateCache.computeIfAbsent("EUR", key -> fetchRateSync(key));
```

### Your Task
Implement `ExchangeRateService` that fetches rates concurrently, and `PortfolioAnalyzer` that uses it to compute total portfolio value in USD — tying together generics, streams, Optional, and DateTime from today's earlier exercises.

### Checkpoint ✅
The `PortfolioAnalyzerTest` passes — concurrent fetching completes and produces correct valuations. The thread-safety test verifies no race conditions in the rate cache.

---

## Day 2 Complete — What You Built

```
GenericRepository<T>            → type-safe generic storage
AssetTransformer                → Function/Predicate/Consumer pipelines
PortfolioAnalytics              → advanced Stream collectors
PortfolioLookup                 → Optional chains, no null checks
TradeTimestampService           → Instant/ZonedDateTime/Duration
PortfolioSerializer             → Jackson JSON + Files API
AnnotationScanner               → runtime annotation inspection
ExchangeRateService             → CompletableFuture concurrent fetching
PortfolioAnalyzer               → everything connected
```

---

## Common Mistakes on Day 2

1. **`orElse` vs `orElseGet`** — `orElse(computeX())` ALWAYS calls `computeX()`. Use `orElseGet(() -> computeX())` for lazy evaluation.
2. **`thenApply` vs `thenCompose`** — `thenApply` is like `.map()`, `thenCompose` is like `.flatMap()`. Use `thenCompose` when your function returns a `CompletableFuture`.
3. **`.join()` blocks the calling thread** — don't call `.join()` on the main/request thread inside a web handler. Only join after `allOf` completes in a background thread.
4. **Type erasure with `TypeReference`** — `mapper.readValue(json, List<Asset>.class)` doesn't compile. Use `new TypeReference<List<Asset>>() {}`.
5. **Parallel streams on small collections** — overhead of thread coordination often exceeds the gain. Only use parallel streams for CPU-bound operations on large datasets (1000+ elements).
6. **`LocalDateTime` for DB storage** — always use `Instant` or `OffsetDateTime` for persistence.

---

## What's Coming in Day 3

Day 3 covers **OOP + SOLID principles** with full TypeScript functional mirrors. For the first time, every Java exercise has a side-by-side TypeScript functional solution in `ts-functional-alt/`. You'll build a banking domain model (Account, Ledger, FeeCalculator) and then rebuild it in pure TypeScript functions — then compare honestly which is cleaner.

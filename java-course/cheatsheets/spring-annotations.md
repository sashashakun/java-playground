# Spring Boot Annotations Reference

> Every annotation you'll encounter in a production Spring Boot application,
> with real fintech examples and the "why" behind each one.

---

## Component Stereotypes

These tell Spring "manage this class as a bean in the application context."

### `@Component`
Generic stereotype. Use when the class doesn't fit any specific role.

```java
@Component
public class CurrencyFormatter {
    public String format(BigDecimal amount, String currency) {
        return "%s %.2f".formatted(currency, amount);
    }
}
```

### `@Service`
Business logic layer. Semantically identical to `@Component` but communicates intent: this class contains business logic.

```java
@Service
public class PaymentService {
    private final PaymentRepository repository;
    private final FraudDetector fraudDetector;

    public PaymentService(PaymentRepository repository, FraudDetector fraudDetector) {
        this.repository = repository;
        this.fraudDetector = fraudDetector;
    }

    public Payment initiatePayment(PaymentRequest request) {
        fraudDetector.check(request);
        Payment payment = Payment.from(request);
        return repository.save(payment);
    }
}
```

### `@Repository`
Data access layer. Same as `@Component` but additionally enables Spring's exception translation — `SQLException` and JPA `PersistenceException` are translated to Spring's `DataAccessException` hierarchy.

```java
@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> findByOwnerId(UUID ownerId);
    List<Wallet> findByStatus(WalletStatus status);
}
```

### `@Controller`
MVC controller. Returns view names (for server-side rendering). Rarely used in REST APIs.

### `@RestController`
= `@Controller` + `@ResponseBody`. Every method return value is serialized to JSON and written directly to the HTTP response. Use this for all REST APIs.

```java
@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{walletId}")
    public WalletResponse getWallet(@PathVariable UUID walletId) {
        return walletService.getWallet(walletId);
    }
}
```

---

## Dependency Injection

### `@Autowired`
Injects a dependency. Optional since Spring 4.3 when there's a single constructor.

```java
// Field injection — works but makes testing harder (avoid):
@Autowired
private PaymentRepository repository;

// Constructor injection — preferred:
private final PaymentRepository repository;

@Autowired // optional if only one constructor
public PaymentService(PaymentRepository repository) {
    this.repository = repository;
}
```

### `@Qualifier`
When multiple beans implement the same interface, use `@Qualifier` to specify which one to inject.

```java
@Service("stripeGateway")
public class StripePaymentGateway implements PaymentGateway { ... }

@Service("cryptoGateway")
public class CryptoPaymentGateway implements PaymentGateway { ... }

// Inject a specific one:
@Autowired
@Qualifier("stripeGateway")
private PaymentGateway paymentGateway;
```

### `@Value`
Inject a value from `application.yml`, environment variables, or Spring Expression Language.

```java
@Value("${payment.max-amount}")
private BigDecimal maxPaymentAmount;

@Value("${payment.currency:CHF}") // with default value
private String defaultCurrency;

@Value("${STRIPE_API_KEY}") // from environment variable
private String stripeApiKey;
```

### `@Bean`
Declares a bean in a `@Configuration` class. Use when you can't annotate the class directly (third-party classes, complex setup).

```java
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
}
```

### `@Configuration`
Marks a class as a source of `@Bean` definitions. Spring processes it specially (beans are proxied for singleton scoping).

### `@Primary`
When multiple beans of the same type exist, mark one as the default when no `@Qualifier` is specified.

```java
@Primary
@Service
public class DefaultPaymentGateway implements PaymentGateway { ... }
```

### `@Lazy`
Delays bean initialization until first use. Rarely needed but useful for slow-starting beans.

---

## Web MVC — Request Mapping

### `@RequestMapping`
Maps HTTP requests to handler methods. Can be on class (base path) or method (sub-path).

```java
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    // All methods here are relative to /api/v1/payments
}
```

### `@GetMapping`, `@PostMapping`, `@PutMapping`, `@PatchMapping`, `@DeleteMapping`
Shorthand for `@RequestMapping(method = RequestMethod.X)`. Use these.

```java
@GetMapping          // GET /api/v1/payments
@GetMapping("/{id}") // GET /api/v1/payments/{id}
@PostMapping         // POST /api/v1/payments
@PutMapping("/{id}") // PUT /api/v1/payments/{id}
@DeleteMapping("/{id}") // DELETE /api/v1/payments/{id}
```

### `@PathVariable`
Extracts values from the URI path.

```java
@GetMapping("/{paymentId}/status")
public PaymentStatus getStatus(@PathVariable UUID paymentId) {
    return paymentService.getStatus(paymentId);
}
// GET /api/v1/payments/550e8400-e29b-41d4-a716-446655440000/status
```

### `@RequestParam`
Extracts query parameters.

```java
@GetMapping
public Page<Payment> listPayments(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    @RequestParam(required = false) String currency,
    @RequestParam(required = false) PaymentStatus status
) {
    return paymentService.list(page, size, currency, status);
}
// GET /api/v1/payments?page=0&size=20&currency=CHF&status=SETTLED
```

### `@RequestBody`
Deserializes the HTTP request body (JSON) into a Java object.

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public PaymentResponse createPayment(@RequestBody @Valid PaymentRequest request) {
    return paymentService.create(request);
}
```

### `@ResponseBody`
Serializes the return value to JSON. Already included in `@RestController` — you won't use this directly often.

### `@ResponseStatus`
Sets the HTTP status code for a handler method.

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)    // 201
public PaymentResponse create(...) { ... }

@DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT) // 204
public void delete(@PathVariable UUID id) { ... }
```

### `@RequestHeader`
Extracts HTTP header values.

```java
@PostMapping("/webhook")
public void handleWebhook(
    @RequestHeader("X-Stripe-Signature") String signature,
    @RequestBody String payload
) {
    webhookService.process(signature, payload);
}
```

---

## Validation (Jakarta Bean Validation)

Always pair with `@Valid` on `@RequestBody` parameters, and `@Validated` on the class to enable method-level validation.

### `@Valid`
Triggers validation on a method parameter or nested object.

```java
@PostMapping
public PaymentResponse create(@RequestBody @Valid PaymentRequest request) { ... }
```

### `@NotNull`, `@NotBlank`, `@NotEmpty`
- `@NotNull` — rejects null (but allows empty string)
- `@NotBlank` — rejects null, empty string, and whitespace-only string
- `@NotEmpty` — rejects null and empty string/collection (allows whitespace)

```java
public record PaymentRequest(
    @NotNull UUID senderId,
    @NotNull UUID recipientId,
    @NotNull @Positive BigDecimal amount,
    @NotBlank @Size(min = 3, max = 3) String currency
) {}
```

### `@Size`, `@Min`, `@Max`, `@Positive`, `@PositiveOrZero`, `@Negative`

```java
@Size(min = 2, max = 50) String name;        // string/collection length
@Min(1) @Max(1_000_000) int amount;           // numeric range
@Positive BigDecimal fee;                      // > 0
@PositiveOrZero BigDecimal balance;            // >= 0
```

### `@Pattern`
Regex validation.

```java
@Pattern(regexp = "^[A-Z]{3}$", message = "Must be a 3-letter ISO currency code")
String currency;
```

### `@Email`
Validates email format.

```java
@Email String email;
```

### `@Future`, `@FutureOrPresent`, `@Past`, `@PastOrPresent`
Date/time validators.

```java
@Future Instant settlementDate;   // must be in the future
@Past Instant createdAt;          // must be in the past
```

### `@Validated`
Class-level annotation to enable method-level validation (different from `@Valid` which is for parameters).

```java
@Service
@Validated
public class WalletService {
    public Wallet create(@Valid WalletRequest request) { ... }
    public BigDecimal getBalance(@NotNull @Positive UUID walletId) { ... }
}
```

---

## JPA / Persistence

### `@Entity`
Marks a class as a JPA entity (mapped to a DB table).

```java
@Entity
@Table(name = "payments")
public class Payment { ... }
```

### `@Table`
Specifies the DB table name and schema.

```java
@Entity
@Table(name = "payments", schema = "ledger",
       uniqueConstraints = @UniqueConstraint(columnNames = {"idempotency_key"}))
public class Payment { ... }
```

### `@Id`, `@GeneratedValue`
```java
@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;

// Or for auto-increment:
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

### `@Column`
Maps a field to a DB column with constraints.

```java
@Column(name = "amount", precision = 19, scale = 4, nullable = false)
private BigDecimal amount;

@Column(name = "currency", length = 3, nullable = false)
private String currency;

@Column(name = "idempotency_key", unique = true)
private String idempotencyKey;
```

### `@OneToMany`, `@ManyToOne`, `@OneToOne`, `@ManyToMany`

```java
@Entity
public class Wallet {
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();
}

@Entity
public class Transaction {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;
}
```

### `@Transactional`
Wraps a method or class in a database transaction. Critical for fintech.

```java
@Service
public class TransferService {

    @Transactional // rolls back on any RuntimeException by default
    public void transfer(UUID fromWalletId, UUID toWalletId, BigDecimal amount) {
        Wallet from = walletRepository.findById(fromWalletId).orElseThrow();
        Wallet to = walletRepository.findById(toWalletId).orElseThrow();
        from.debit(amount);
        to.credit(amount);
        // Both saves happen atomically — if either fails, both roll back
        walletRepository.save(from);
        walletRepository.save(to);
    }

    @Transactional(readOnly = true) // optimization for reads
    public List<Transaction> getHistory(UUID walletId) { ... }

    @Transactional(isolation = Isolation.SERIALIZABLE) // strictest isolation
    public void processSettlement(...) { ... }
}
```

### `@CreatedDate`, `@LastModifiedDate`, `@CreatedBy`, `@LastModifiedBy`
JPA auditing. Requires `@EnableJpaAuditing` on a `@Configuration` class.

```java
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Payment {
    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
```

---

## Spring Boot Configuration

### `@SpringBootApplication`
The main annotation — combines `@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`. Goes on your main class.

```java
@SpringBootApplication
public class WalletApplication {
    public static void main(String[] args) {
        SpringApplication.run(WalletApplication.class, args);
    }
}
```

### `@ConfigurationProperties`
Binds a `application.yml` prefix to a Java record or class. Much cleaner than multiple `@Value` annotations.

```yaml
# application.yml
payment:
  max-amount: 50000
  currencies:
    - USD
    - EUR
    - CHF
  fraud:
    velocity-limit: 10
    window-minutes: 60
```

```java
@ConfigurationProperties(prefix = "payment")
@Validated
public record PaymentProperties(
    @NotNull BigDecimal maxAmount,
    @NotEmpty List<String> currencies,
    FraudProperties fraud
) {
    public record FraudProperties(int velocityLimit, int windowMinutes) {}
}
// Also needs: @EnableConfigurationProperties(PaymentProperties.class)
// or @SpringBootApplication automatically picks it up with @ConfigurationPropertiesScan
```

### `@Profile`
Only registers a bean when the specified profile is active.

```java
@Bean
@Profile("test")
public PaymentGateway mockPaymentGateway() {
    return new MockPaymentGateway();
}

@Bean
@Profile("!test") // all profiles except test
public PaymentGateway realPaymentGateway() {
    return new StripePaymentGateway(apiKey);
}
```

---

## Error Handling

### `@ControllerAdvice` / `@RestControllerAdvice`
Global exception handler for all controllers. `@RestControllerAdvice` = `@ControllerAdvice` + `@ResponseBody`.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WalletNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleWalletNotFound(WalletNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Wallet not found");
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ProblemDetail handleInsufficientFunds(InsufficientFundsException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problem.setTitle("Insufficient funds");
        problem.setDetail(ex.getMessage());
        problem.setProperty("balance", ex.getAvailableBalance());
        problem.setProperty("requested", ex.getRequestedAmount());
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation failed");
        Map<String, String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                fe -> Objects.requireNonNullElse(fe.getDefaultMessage(), "Invalid")
            ));
        problem.setProperty("errors", errors);
        return problem;
    }
}
```

### `@ExceptionHandler`
Handles a specific exception type within `@ControllerAdvice` (or within a single controller).

---

## Testing Annotations

### `@SpringBootTest`
Loads the full Spring application context. Use for integration tests.

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WalletApiIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    // ...
}
```

### `@WebMvcTest`
Loads only the web layer (controllers, filters, advice). Mocks the service layer. Faster than `@SpringBootTest`.

```java
@WebMvcTest(WalletController.class)
class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;
    // ...
}
```

### `@DataJpaTest`
Loads only the JPA layer (repositories, entities). Uses an in-memory H2 database by default.

```java
@DataJpaTest
class WalletRepositoryTest {
    @Autowired
    private WalletRepository walletRepository;
    // ...
}
```

### `@MockBean`
Creates a Mockito mock and registers it as a Spring bean, replacing the real one.

```java
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {
    @MockBean
    private PaymentService paymentService; // Spring injects this mock
}
```

### `@TestConfiguration`
Extra bean configuration only active during tests.

```java
@TestConfiguration
public class TestSecurityConfig {
    @Bean
    @Primary
    public SecurityFilterChain testSecurityChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
```

---

## Async & Scheduling

### `@Async`
Makes a method execute in a separate thread pool. Method must return `void` or `CompletableFuture<T>`. Requires `@EnableAsync` on a `@Configuration` class.

```java
@Service
public class NotificationService {

    @Async
    public CompletableFuture<Void> sendSettlementNotification(Payment payment) {
        emailClient.send(payment.getUserEmail(), buildEmailBody(payment));
        return CompletableFuture.completedFuture(null);
    }
}
```

### `@Scheduled`
Runs a method on a schedule. Requires `@EnableScheduling` on a `@Configuration` class.

```java
@Service
public class ReconciliationJob {

    @Scheduled(cron = "0 0 2 * * *") // daily at 2:00 AM
    public void reconcileSettlements() { ... }

    @Scheduled(fixedDelay = 30_000)  // 30 seconds after last execution completes
    public void pollPendingPayments() { ... }

    @Scheduled(fixedRate = 60_000)   // every 60 seconds regardless of execution time
    public void refreshExchangeRates() { ... }
}
```

---

## Caching

### `@Cacheable`, `@CacheEvict`, `@CachePut`
Requires a `CacheManager` bean (e.g., Caffeine, Redis).

```java
@Service
public class ExchangeRateService {

    @Cacheable(value = "exchange-rates", key = "#currency")
    public BigDecimal getRate(String currency) {
        return externalRateApi.fetch(currency); // only called on cache miss
    }

    @CacheEvict(value = "exchange-rates", allEntries = true)
    @Scheduled(fixedRate = 300_000) // clear cache every 5 minutes
    public void evictRatesCache() {}

    @CachePut(value = "exchange-rates", key = "#currency")
    public BigDecimal refreshRate(String currency) {
        return externalRateApi.fetch(currency); // always calls API, updates cache
    }
}
```

---

## Security

### `@PreAuthorize`
Method-level security with Spring Expression Language. Requires `@EnableMethodSecurity`.

```java
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping("/wallets")
    @PreAuthorize("hasRole('ADMIN')")
    public List<WalletSummary> getAllWallets() { ... }

    @PostMapping("/freeze/{walletId}")
    @PreAuthorize("hasRole('COMPLIANCE') or hasRole('ADMIN')")
    public void freezeWallet(@PathVariable UUID walletId) { ... }

    @GetMapping("/my-wallet")
    @PreAuthorize("@walletService.isOwner(authentication.name, #walletId)")
    public WalletDetail getMyWallet(@PathVariable UUID walletId) { ... }
}
```

---

## AOP (Aspect-Oriented Programming)

### `@Aspect`, `@Around`, `@Before`, `@After`, `@Pointcut`
Used for cross-cutting concerns: logging, metrics, audit trails. Requires `@EnableAspectJAutoProxy`.

```java
@Aspect
@Component
public class AuditLogAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditLogAspect.class);

    @Pointcut("@annotation(com.example.Audited)")
    public void auditedMethods() {}

    @Around("auditedMethods()")
    public Object logAuditTrail(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().getName();
        log.info("AUDIT: {} called with args: {}", method, joinPoint.getArgs());
        try {
            Object result = joinPoint.proceed();
            log.info("AUDIT: {} completed successfully", method);
            return result;
        } catch (Exception e) {
            log.error("AUDIT: {} failed: {}", method, e.getMessage());
            throw e;
        }
    }
}
```

---

## Quick Reference Card

```
COMPONENT STEREOTYPES
@Component         generic Spring bean
@Service           business logic layer
@Repository        data access layer (+ exception translation)
@RestController    HTTP controller (auto-serializes to JSON)

DI
@Autowired         inject dependency (optional with single constructor)
@Qualifier("name") pick specific bean when multiple candidates
@Value("${prop}")  inject config value
@Bean              declare bean in @Configuration class
@Primary           default bean when multiple of same type

WEB MVC
@RequestMapping    base path for controller
@GetMapping        GET handler
@PostMapping       POST handler
@PutMapping        PUT handler
@DeleteMapping     DELETE handler
@PathVariable      extract {id} from URL path
@RequestParam      extract ?key=value query param
@RequestBody       deserialize JSON request body
@ResponseStatus    set HTTP status code

VALIDATION
@Valid             trigger validation on parameter
@NotNull           reject null
@NotBlank          reject null, empty, whitespace
@Size(min,max)     string/collection length
@Positive          numeric > 0
@Pattern(regexp)   regex validation

JPA
@Entity            JPA entity (maps to DB table)
@Table(name="...")  specify table name
@Id                primary key field
@GeneratedValue    auto-generate ID
@Column(...)       column mapping
@OneToMany         1-to-many relationship
@ManyToOne         many-to-1 relationship
@Transactional     database transaction boundary

TESTING
@SpringBootTest    full context integration test
@WebMvcTest        controller slice test (no DB)
@DataJpaTest       repository slice test (no web)
@MockBean          mock a Spring bean in test context

ASYNC/SCHEDULING
@Async             run method in separate thread
@Scheduled(cron)   cron-triggered job
@Scheduled(fixedRate) fixed-rate job

SECURITY
@PreAuthorize      method-level access control (SpEL)
```

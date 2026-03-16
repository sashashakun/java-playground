# Day 4 — Spring Boot Core

**Theme: Payment Processing REST API**

You're building the backend of a payments service — REST endpoints, proper
dependency injection, request validation, configuration, error handling, and
testing. Today you'll go from zero to a production-ready Spring Boot application
structure.

---

## What you're building

A **Payment Processing REST API** that grows through 7 exercises:

| Exercise | What you add |
|----------|-------------|
| 01 REST Controller | CRUD endpoints, ResponseEntity, in-memory store |
| 02 Dependency Injection | @Service, constructor injection, @Component |
| 03 Bean Validation | @Valid, @NotNull, @DecimalMin, ConstraintViolation |
| 04 Config Properties | @ConfigurationProperties, application.yml |
| 05 Exception Handling | @ControllerAdvice, ProblemDetail (RFC 9457) |
| 06 MockMvc Testing | @WebMvcTest, MockMvc, @MockitoBean — YOU write the tests |
| 07 Profiles & Actuator | @Profile, mock vs live gateway, /actuator/health |

---

## Mental model: Express/NestJS → Spring Boot

| NestJS/Express | Spring Boot |
|----------------|-------------|
| `@Controller()` class | `@RestController` class |
| `@Get(':id')` | `@GetMapping("/{id}")` |
| `@Body()` | `@RequestBody` |
| `@Param('id')` | `@PathVariable String id` |
| `@Query('page')` | `@RequestParam int page` |
| `new HttpException(msg, 404)` | `ResponseEntity.notFound().build()` |
| Module / Provider / Injectable | @Component / @Service / @Repository |
| Constructor injection | Same — Java's constructor injection |
| ConfigService / .env | @ConfigurationProperties + application.yml |
| Global exception filter | @ControllerAdvice |
| Jest + supertest | @WebMvcTest + MockMvc |

**The critical difference:** Spring Boot uses _annotation-driven_ class scanning
(no explicit `module.ts`). Put your classes in the right package and Spring
picks them up automatically. `@SpringBootApplication` on the main class enables
`@ComponentScan` for the whole package tree.

---

## Setup for each exercise

```bash
cd exercises/01-rest-controller
./gradlew bootRun     # starts on port 8080
./gradlew test        # runs tests without starting a server
```

For all exercises: Spring Boot's `@WebMvcTest` and `MockMvc` let you test
controllers **without** starting an HTTP server — much faster.

---

## Exercises

### Exercise 01 — REST Controller Basics

**File:** `PaymentController.java`

Build a `PaymentController` backed by an in-memory `ConcurrentHashMap`.
No service layer yet — just get comfortable with the Spring Web annotations.

**Endpoints to implement:**

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/payments` | List all payments |
| `GET` | `/api/payments/{id}` | Get by id → 404 if missing |
| `POST` | `/api/payments` | Create → 201 Created with Location header |
| `DELETE` | `/api/payments/{id}` | Delete → 204 No Content, 404 if missing |

**Key annotations:**
```java
@RestController              // = @Controller + @ResponseBody
@RequestMapping("/api/payments")
@GetMapping("/{id}")
@PostMapping
@DeleteMapping("/{id}")
@PathVariable String id
@RequestBody PaymentRequest request
```

**ResponseEntity idioms:**
```java
ResponseEntity.ok(body)                       // 200
ResponseEntity.created(uri).body(payment)     // 201
ResponseEntity.notFound().build()             // 404
ResponseEntity.noContent().build()            // 204
```

**Break it:** What happens if `@RequestBody` is missing and you POST JSON?
What HTTP status does Spring return for a malformed JSON body?

---

### Exercise 02 — Dependency Injection

**File:** `PaymentService.java` (interface) + `InMemoryPaymentService.java`

Refactor the controller to depend on a `PaymentService` interface, injected
via the constructor. This is the most important Spring pattern.

```java
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    // Constructor injection — no @Autowired needed in Spring Boot
    public PaymentController(PaymentService service) {
        this.service = service;
    }
}
```

**DI rules:**
- Single constructor → Spring auto-injects (no `@Autowired` needed)
- Multiple constructors → annotate the one Spring should use
- `@Service` = `@Component` with a semantic label (used on service classes)
- `@Repository` = `@Component` with persistence semantics
- Prefer `final` fields + constructor injection (testability)

**Break it:** Add a second `PaymentService` implementation and start the app.
What error do you get? Fix it with `@Primary` or `@Qualifier`.

---

### Exercise 03 — Bean Validation

**File:** `CreatePaymentRequest.java` (add annotations), `PaymentController.java` (add `@Valid`)

Jakarta Bean Validation lets you declare constraints on DTOs rather than
writing `if (amount == null) throw ...` everywhere.

```java
public record CreatePaymentRequest(
    @NotBlank(message = "Currency is required")
    String currency,

    @NotNull @DecimalMin(value = "0.01", message = "Amount must be positive")
    BigDecimal amount,

    @NotBlank @Size(max = 100)
    String description
) {}
```

**In the controller:**
```java
@PostMapping
public ResponseEntity<Payment> create(@Valid @RequestBody CreatePaymentRequest request) {
    // ...
}
```

When validation fails, Spring throws `MethodArgumentNotValidException`.
Add a `@ControllerAdvice` that catches it and returns a `400 Bad Request`
with a clear JSON error body.

**Break it:** Remove `@Valid` from the controller. What happens when you POST
an empty JSON `{}`? Why didn't the validation fire?

---

### Exercise 04 — Configuration Properties

**File:** `PaymentConfig.java`, `application.yml`

Hardcoded values like fee rates and API keys belong in configuration, not code.

```yaml
# application.yml
payment:
  max-amount: 50000.00
  supported-currencies:
    - USD
    - EUR
    - GBP
  default-fee-rate: 0.015
  gateway-url: "https://api.example.com/v1"
```

```java
@ConfigurationProperties(prefix = "payment")
@Validated  // triggers Bean Validation on the config itself
public class PaymentConfig {
    @NotNull private BigDecimal maxAmount;
    @NotEmpty private List<String> supportedCurrencies;
    private BigDecimal defaultFeeRate;
    private String gatewayUrl;
    // getters/setters (or records in Spring Boot 3.x)
}
```

**Also implement** `@Value` injection for a single property:
```java
@Value("${payment.gateway-url}")
private String gatewayUrl;
```

**Break it:** Rename `max-amount` to `maxAmount` in the YAML. Does it still
bind? (Hint: Spring's relaxed binding — it should.) Now try a completely wrong
key name. What happens?

---

### Exercise 05 — Exception Handling

**File:** `GlobalExceptionHandler.java`, custom exception classes

Build a centralized error handler that returns RFC 9457 `ProblemDetail` responses.

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(PaymentNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Payment Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex) {
        // ... collect field errors
    }
}
```

**ProblemDetail** (Spring 6+ / Boot 3+):
```json
{
  "type": "about:blank",
  "title": "Payment Not Found",
  "status": 404,
  "detail": "Payment pay-abc not found",
  "instance": "/api/payments/pay-abc"
}
```

**Custom exceptions to implement:**
- `PaymentNotFoundException(String paymentId)`
- `DuplicatePaymentException(String idempotencyKey)`
- `PaymentLimitExceededException(BigDecimal limit)`

---

### Exercise 06 — MockMvc Testing (Write the Tests!)

**This exercise flips the model: the implementation is provided. You write
the tests.**

Given a working `PaymentController`, write comprehensive `@WebMvcTest` tests:

```java
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean PaymentService service;   // replaces real bean in context

    @Test
    void getByIdReturns200() throws Exception {
        when(service.findById("p1")).thenReturn(Optional.of(payment));

        mockMvc.perform(get("/api/payments/p1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("p1"))
            .andExpect(jsonPath("$.amount").value(100.0));
    }
}
```

**You must test:**
- `GET /api/payments` — 200, returns list
- `GET /api/payments/{id}` — 200 when found; 404 when not
- `POST /api/payments` — 201 Created, Location header, 400 for invalid body
- `DELETE /api/payments/{id}` — 204 when found; 404 when not

**Key MockMvc helpers:**
```java
mockMvc.perform(get("/path"))
       .andExpect(status().isOk())
       .andExpect(content().contentType(MediaType.APPLICATION_JSON))
       .andExpect(jsonPath("$.field").value("expected"))
       .andExpect(header().string("Location", containsString("/api/payments/")));
```

---

### Exercise 07 — Profiles & Actuator

**File:** `PaymentGateway.java`, `MockPaymentGateway.java`, `LivePaymentGateway.java`

Use Spring profiles to switch between a mock gateway (tests/local) and a
"live" gateway (production) without changing code.

```java
@Component
@Profile({"default", "test"})
public class MockPaymentGateway implements PaymentGateway { ... }

@Component
@Profile("live")
public class LivePaymentGateway implements PaymentGateway { ... }
```

**Activate a profile:**
```bash
./gradlew bootRun --args='--spring.profiles.active=live'
# or in application.yml:
spring.profiles.active: mock
```

**Actuator** (add `spring-boot-starter-actuator` dependency):
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always
```

Now `GET /actuator/health` returns:
```json
{"status": "UP", "components": {...}}
```

---

## Checkpoints

After completing all exercises:

- [ ] Can you explain the difference between `@Controller` and `@RestController`?
- [ ] Why is constructor injection preferred over `@Autowired` field injection?
- [ ] When does `@Valid` trigger and when does it NOT?
- [ ] What is `@ControllerAdvice` and how does it relate to `@ExceptionHandler`?
- [ ] What does `@WebMvcTest` load vs `@SpringBootTest`?
- [ ] How do profiles let you switch implementations without `if` statements?

---

## Spring Boot Application Structure Quick Reference

```
src/
├── main/
│   ├── java/com/example/fintech/
│   │   ├── Application.java          (@SpringBootApplication)
│   │   ├── controller/               (@RestController)
│   │   ├── service/                  (@Service, interface + impl)
│   │   ├── model/                    (records, DTOs, enums)
│   │   ├── config/                   (@ConfigurationProperties)
│   │   └── exception/                (custom exceptions + @ControllerAdvice)
│   └── resources/
│       ├── application.yml           (main config)
│       └── application-test.yml     (test profile overrides)
└── test/
    └── java/com/example/fintech/
        ├── controller/               (@WebMvcTest)
        └── service/                  (plain JUnit + Mockito)
```

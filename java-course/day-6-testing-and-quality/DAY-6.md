# Day 6 — Testing and Quality

## Overview

Professional Java engineers spend ~30-40% of their time writing tests. Today you go deep on the
full Spring Boot testing toolkit — from unit-level Mockito mocks to full-stack `@SpringBootTest`
integration tests, plus architecture enforcement with ArchUnit.

**Target audience**: Senior TypeScript/Node.js engineers.

---

## Prerequisites

- Completed Days 1–5
- Gradle 8.x, Java 21, IntelliJ IDEA

---

## Learning Objectives

By the end of Day 6 you will be able to:

1. Write parameterized tests with `@ParameterizedTest`, `@CsvSource`, `@MethodSource`, `@EnumSource`
2. Use `@Nested` to group related tests and `@TestFactory` for dynamic test generation
3. Mock, stub, and verify with Mockito — `@Mock`, `@InjectMocks`, `ArgumentCaptor`, `@Spy`
4. Write focused controller tests with `@WebMvcTest` and `MockMvc`
5. Write full-stack integration tests with `@SpringBootTest` and `TestRestTemplate`
6. Use AssertJ's advanced API — soft assertions, `extracting`, `filteredOn`, custom `Condition<T>`
7. Build reusable test fixtures with the TestDataBuilder pattern
8. Enforce architectural constraints with ArchUnit rules

---

## TypeScript ↔ Java Mapping

| TypeScript / Jest                    | Java / JUnit 5 + Spring                        |
|--------------------------------------|------------------------------------------------|
| `test.each([…])(…)`                  | `@ParameterizedTest` + `@CsvSource` / `@MethodSource` |
| `describe` block                     | `@Nested` inner class                          |
| `jest.fn()` / `vi.fn()`              | `@Mock` (Mockito)                              |
| `mockFn.mockReturnValue(...)`        | `when(mock.method()).thenReturn(...)`           |
| `expect(fn).toThrow(Error)`          | `assertThatThrownBy(() -> …).isInstanceOf(…)` |
| `expect(mockFn).toHaveBeenCalledWith`| `verify(mock).method(argCaptor.capture())`     |
| `supertest(app).get('/path')`        | `mockMvc.perform(get("/path"))`                |
| Jest + in-memory DB                  | `@DataJpaTest` or `@SpringBootTest`            |

---

## Exercises

| # | Topic | Key Annotations / Tools |
|---|-------|------------------------|
| 01 | JUnit 5 Parameterized Tests | `@ParameterizedTest`, `@CsvSource`, `@MethodSource`, `@EnumSource`, `@Nested` |
| 02 | Mockito | `@Mock`, `@InjectMocks`, `when/thenReturn`, `verify`, `ArgumentCaptor`, `@Spy` |
| 03 | @WebMvcTest | MockMvc, `perform`, `andExpect`, JSON matchers, error scenarios |
| 04 | @SpringBootTest | Full context, `TestRestTemplate`, `@DirtiesContext` |
| 05 | AssertJ Advanced | Soft assertions, `extracting`, `filteredOn`, custom `Condition<T>` |
| 06 | Test Fixtures | `TestDataBuilder`, `@TestInstance(PER_CLASS)`, `@BeforeAll`, fixture factories |
| 07 | ArchUnit | Layered architecture rules, package dependencies, naming conventions |

---

## Quick Reference

```java
// Parameterized test
@ParameterizedTest
@CsvSource({"USD,100,true", "XYZ,100,false", "USD,-1,false"})
void validatePayment(String currency, int amount, boolean expected) { ... }

// Mockito
@ExtendWith(MockitoExtension.class)
class ServiceTest {
    @Mock FraudDetector fraud;
    @InjectMocks PaymentService service;

    @Test void capturesArguments() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(fraud).check(captor.capture());
        assertThat(captor.getValue()).startsWith("user-");
    }
}

// WebMvcTest
mockMvc.perform(post("/payments")
    .contentType(APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(request)))
  .andExpect(status().isCreated())
  .andExpect(jsonPath("$.id").isNotEmpty());

// Soft assertions — collect ALL failures, not just first
SoftAssertions.assertSoftly(soft -> {
    soft.assertThat(payment.getId()).isNotNull();
    soft.assertThat(payment.getStatus()).isEqualTo(PENDING);
});

// ArchUnit
@AnalyzeClasses(packages = "com.example.fintech")
class ArchTest {
    @ArchTest
    ArchRule controllers_must_not_call_repos =
        noClasses().that().resideInPackage("..web..")
            .should().dependOnClassesThat().resideInPackage("..repository..");
}
```

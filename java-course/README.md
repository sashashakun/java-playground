# Java Backend Course: TypeScript → Java for Fintech Engineers

> An 8-day intensive course for senior engineers who already know TypeScript/Node.js
> and want to become production-grade Java backend engineers.

---

## Who This Course Is For

You are a senior engineer — 5-10+ years of TypeScript, Node.js, React, AWS. You've shipped production systems. You understand async patterns, REST APIs, databases, and what good code looks like.

**This course does NOT re-teach programming.** It bridges from what you already know to Java and the Spring ecosystem. Every concept is anchored in **fintech domain examples**: transaction engines, wallets, ledgers, payment processing, KYC workflows.

By Day 8, you'll have a portfolio-ready Spring Boot payment service and enough Java fluency to pass code review at any Java shop.

---

## Prerequisites Checklist

Complete this before Day 1:

- [ ] JDK 21 installed — `java --version` shows `21.x.x`
- [ ] IntelliJ IDEA (Community or Ultimate) installed
- [ ] Docker Desktop installed (needed from Day 5 onward)
- [ ] Node.js 20+ installed (for `ts-functional-alt` exercises in Days 3 & 7)
- [ ] Git configured
- [ ] You've read `cheatsheets/ts-to-java-rosetta.md`
- [ ] You've read `cheatsheets/java-gotchas.md`

---

## Installation Guide

### JDK 21

**macOS (Homebrew — recommended):**
```bash
brew install --cask temurin@21
# Verify:
java --version   # → openjdk 21.x.x
```

**macOS/Linux (SDKMAN — best for managing multiple JDKs):**
```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 21.0.4-tem
sdk use java 21.0.4-tem
# Verify:
java --version
```

**Windows:**
1. Download Eclipse Temurin 21 from https://adoptium.net/
2. Run the installer — it sets `JAVA_HOME` automatically
3. Verify in a new terminal: `java --version`

### Gradle

Each exercise ships with a Gradle wrapper (`./gradlew`). You do **not** need Gradle installed globally. Just run `./gradlew <task>` from any exercise directory.

To install globally (optional): `sdk install gradle 8.7`

### IntelliJ IDEA Setup

1. Download from https://www.jetbrains.com/idea/ (Community edition is free and sufficient)
2. **Opening a project**: File → Open → select any directory containing `build.gradle.kts`
3. IntelliJ auto-detects Gradle and imports dependencies (~30 seconds first time)
4. Wait for the "Gradle sync" to complete before running anything

**Essential shortcuts to memorize on Day 1:**

| Action | macOS | Windows/Linux |
|--------|-------|---------------|
| Run current class | `Ctrl+Shift+R` | `Ctrl+Shift+F10` |
| Run last config | `Ctrl+R` | `Shift+F10` |
| Debug | `Ctrl+D` | `Shift+F9` |
| Rename refactor | `Shift+F6` | `Shift+F6` |
| Find any action | `Cmd+Shift+A` | `Ctrl+Shift+A` |
| Generate code | `Cmd+N` | `Alt+Insert` |
| Quick-fix / intentions | `Alt+Enter` | `Alt+Enter` |
| Go to definition | `Cmd+B` | `Ctrl+B` |
| Format code | `Cmd+Alt+L` | `Ctrl+Alt+L` |
| Search everywhere | `Shift+Shift` | `Shift+Shift` |
| Inline documentation | `F1` | `Ctrl+Q` |
| Evaluate expression (debug) | `Alt+F8` | `Alt+F8` |

---

## 8-Day Roadmap

Check off each day as you complete it. Estimated hours are for focused work — not including breaks.

| # | Day | Topic | Hours | Done? |
|---|-----|-------|-------|-------|
| 0 | Setup | README + Cheatsheets | ~1h | ☐ |
| 1 | Day 1 | Java Core Language — Types, Collections, Enums, Records | 6–8h | ☐ |
| 2 | Day 2 | Java Advanced Language — Generics, Streams, Concurrency | 6–8h | ☐ |
| 3 | Day 3 | OOP + SOLID Principles (with TypeScript functional mirrors) | 6–8h | ☐ |
| 4 | Day 4 | Spring Boot Core — REST API, DI, Validation, Error Handling | 6–8h | ☐ |
| 5 | Day 5 | Data Layer — JPA, Hibernate, Flyway, Transaction Management | 6–8h | ☐ |
| 6 | Day 6 | Testing & Code Quality — JUnit 5, Mockito, Testcontainers | 5–7h | ☐ |
| 7 | Day 7 | Advanced Patterns — Security, Caching, Async, Docker | 6–8h | ☐ |
| 8 | Final | Capstone: Payment Processing Service | 8h | ☐ |

**Total: ~55–65 hours**

---

## Domain: Fintech / Crypto / Payments

Every exercise is grounded in financial domain models. Here's what you build across the course:

| Days | What You Build |
|------|----------------|
| 1 | **Transaction Classification Engine** — parse CSVs, classify transactions with enums + sealed classes, validate with custom exceptions |
| 2 | **Multi-Currency Portfolio Analyzer** — stream processing, generics, `CompletableFuture` for concurrent exchange rate fetching |
| 3 | **Banking Domain Model** — `Account`, `Transaction`, `Ledger`, `FeeCalculator` with full SOLID compliance + functional TypeScript mirrors |
| 4 | **Crypto Wallet REST API** — CRUD endpoints, deposit/withdraw, validation, error responses, layered architecture |
| 5 | **Ledger Persistence Layer** — double-entry bookkeeping with JPA, concurrent balance updates, Flyway migrations |
| 6 | **Test Suite for Crypto Wallet API** — 80%+ coverage with unit, integration, and API tests |
| 7 | **Secured + Cached Wallet API** — JWT auth, rate limiting, Redis caching, async processing, Docker |
| 8 | **Payment Processing Service** — the boss fight. Complete microservice with events, idempotency, and Docker Compose |

---

## How to Use This Course

### The Build → Break → Fix Methodology

Every exercise follows this pattern:

```
1. BUILD  → implement until all tests go green
2. BREAK  → the exercise asks you to trigger a specific failure mode
3. FIX    → fix it properly (not with a hack)
```

This is how production engineers think. If you only ever build the happy path, you don't actually understand the system.

### Running Exercises

```bash
# From any Java exercise directory:
./gradlew test              # Run all tests
./gradlew test --info       # Verbose output
./gradlew run               # Run main class
./gradlew test --tests "*.TransactionClassifierTest"  # Run specific test

# From any TypeScript exercise directory (ts-functional-alt/):
npm install
npm test
npm start
```

### ADHD-Friendly Checkpoints

Each day has **checkpoints every 45–60 minutes** — a small win, a green test, a working curl command. When you hit a checkpoint, take a 5-minute break. Seriously.

If you're stuck for more than **20 minutes**: open the `solutions/` directory. Reading a good solution is legitimate learning. The goal is understanding, not suffering through a blank editor.

### Difficulty Ratings

| Rating | Meaning |
|--------|---------|
| 🟢 Easy | One concept, focused scope |
| 🟡 Medium | Combines 2-3 concepts, production-level complexity |
| 🔴 Hard | "Boss fight" — synthesize multiple concepts under constraints |
| 💀 Brutal | Optional stretch goal. Proceed only if you want the scars |

### The TypeScript Functional Mirror

Days 3 and 7 include `ts-functional-alt/` directories alongside every Java exercise. These are **complete TypeScript functional solutions** to the same problem — no classes allowed. They exist to force a genuine comparison between paradigms.

Sometimes Java OOP is genuinely cleaner. Sometimes the functional TypeScript version is. The `DAY-3.md` and `DAY-7.md` files include honest "🔀 Paradigm Comparison" sections after each exercise that call it straight.

---

## Cheatsheets Reference

Read these in the order listed. You'll reference them throughout the course.

| Cheatsheet | Read When | What It Covers |
|------------|-----------|----------------|
| [`ts-to-java-rosetta.md`](cheatsheets/ts-to-java-rosetta.md) | **Before Day 1** | 50+ side-by-side TS↔Java pattern translations |
| [`java-gotchas.md`](cheatsheets/java-gotchas.md) | **Before Day 1** | Top 20 traps that trip up TypeScript developers |
| [`solid-quick-ref.md`](cheatsheets/solid-quick-ref.md) | Before Day 3 | SOLID principles with fintech before/after examples |
| [`spring-annotations.md`](cheatsheets/spring-annotations.md) | Before Day 4 | Every key Spring annotation explained with real examples |
| [`oop-vs-functional.md`](cheatsheets/oop-vs-functional.md) | After Day 3 | When to use OOP vs functional — honest decision framework |

---

## Project Structure

```
java-course/
├── README.md                     ← You are here
├── PLAN.md                       ← Generation progress tracker
├── .gitignore
├── settings.gradle.kts           ← Root Gradle multi-project config
├── cheatsheets/
│   ├── ts-to-java-rosetta.md
│   ├── java-gotchas.md
│   ├── spring-annotations.md
│   ├── solid-quick-ref.md
│   └── oop-vs-functional.md
├── day-1-java-core-language/
│   ├── DAY-1.md                  ← Day guide with theory + exercises
│   ├── exercises/
│   │   ├── 01-tooling-and-gradle/
│   │   ├── 02-type-system-and-primitives/
│   │   ├── 03-strings-and-text/
│   │   ├── 04-collections-deep-dive/
│   │   ├── 05-enums-as-classes/
│   │   ├── 06-records-and-sealed-classes/
│   │   └── 07-exception-handling/
│   └── solutions/
├── day-2-java-advanced-language/
│   ├── DAY-2.md
│   ├── exercises/
│   │   ├── 01-generics-and-wildcards/
│   │   ├── 02-functional-interfaces-and-lambdas/
│   │   ├── 03-streams-mastery/
│   │   ├── 04-optional-in-depth/
│   │   ├── 05-datetime-api/
│   │   ├── 06-io-and-serialization/
│   │   ├── 07-annotations-and-packages/
│   │   └── 08-concurrency-foundations/
│   └── solutions/
├── day-3-oop-and-solid/
│   ├── DAY-3.md
│   ├── exercises/
│   │   ├── 01-encapsulation-and-immutability/
│   │   │   ├── src/
│   │   │   └── ts-functional-alt/
│   │   ├── 02-inheritance-vs-composition/
│   │   │   ├── src/
│   │   │   └── ts-functional-alt/
│   │   └── ... (08 exercises total)
│   └── solutions/
├── day-4-spring-boot-core/
│   ├── DAY-4.md
│   ├── exercises/
│   └── solutions/
├── day-5-data-layer/
│   ├── DAY-5.md
│   ├── exercises/
│   └── solutions/
├── day-6-testing-and-quality/
│   ├── DAY-6.md
│   ├── exercises/
│   └── solutions/
├── day-7-advanced-patterns/
│   ├── DAY-7.md
│   ├── exercises/
│   │   └── (each has src/ + ts-functional-alt/)
│   └── solutions/
└── day-8-capstone-project/
    ├── DAY-8.md
    ├── starter/                  ← Scaffold with TODO markers
    └── solution/                 ← Complete reference implementation
```

---

## External Resources

- **Java 21 API Docs**: https://docs.oracle.com/en/java/docs/api/
- **Spring Boot Reference**: https://docs.spring.io/spring-boot/docs/current/reference/html/
- **Baeldung**: https://www.baeldung.com/ — the Spring ecosystem's best tutorial site
- **Effective Java (3rd ed.)** by Joshua Bloch — the canonical Java style guide, worth reading alongside this course
- **Spring in Action (6th ed.)** by Craig Walls — deep Spring reference

---

*Good luck. By Day 8, you'll be writing production-quality Spring Boot services.*

// Exercise 06 — SOLID: DIP (Dependency Inversion)
// Functional TypeScript equivalent: NO class keyword
// DIP: high-level orchestrator depends on abstractions (type aliases), not concretions
// Partial application replaces constructor injection

type Payment = {
  id: string;
  accountId: string;
  amount: number;
  status: "PENDING" | "COMPLETED" | "FAILED";
};

// Abstractions (interfaces as type aliases) — the orchestrator only knows these
type PaymentRepository = {
  save: (payment: Payment) => void;
  findById: (id: string) => Payment | undefined;
  findAll: () => Payment[];
  delete: (id: string) => boolean;
};

type PaymentEvent =
  | { type: "CREATED";   payment: Payment }
  | { type: "COMPLETED"; payment: Payment }
  | { type: "FAILED";    payment: Payment; reason: string };

type NotificationPort = { notify: (event: PaymentEvent) => void };

// DIP: orchestrator is a factory that accepts abstractions — same as constructor injection
// Partial application IS dependency injection in functional style
const makeOrchestrator = (repo: PaymentRepository, notifier: NotificationPort) => {
  const nextId = (() => {
    let counter = 0;
    return () => `pay-${(++counter).toString().padStart(4, "0")}`;
  })();

  return {
    processPayment: (accountId: string, amount: number): Payment => {
      const payment: Payment = { id: nextId(), accountId, amount, status: "PENDING" };
      repo.save(payment);
      notifier.notify({ type: "CREATED", payment });
      return payment;
    },

    completePayment: (paymentId: string): Payment => {
      const payment = repo.findById(paymentId);
      if (!payment) throw new Error(`Payment not found: ${paymentId}`);
      const completed: Payment = { ...payment, status: "COMPLETED" };
      repo.save(completed);
      notifier.notify({ type: "COMPLETED", payment: completed });
      return completed;
    },

    failPayment: (paymentId: string, reason: string): Payment => {
      const payment = repo.findById(paymentId);
      if (!payment) throw new Error(`Payment not found: ${paymentId}`);
      const failed: Payment = { ...payment, status: "FAILED" };
      repo.save(failed);
      notifier.notify({ type: "FAILED", payment: failed, reason });
      return failed;
    },

    getAllPayments: (): Payment[] => repo.findAll(),
  };
};

// --- Concrete implementations (low-level details) ---

// InMemoryPaymentRepository — concrete, injected
const makeInMemoryRepository = (): PaymentRepository => {
  const store = new Map<string, Payment>();
  return {
    save:     (p) => store.set(p.id, p),
    findById: (id) => store.get(id),
    findAll:  () => [...store.values()],
    delete:   (id) => store.delete(id),
  };
};

// LoggingNotification — concrete, injected
const makeLoggingNotifier = (): NotificationPort => ({
  notify: (event) => {
    switch (event.type) {
      case "CREATED":   console.log(`[NOTIFY] Payment created:   ${event.payment.id} — $${event.payment.amount} for ${event.payment.accountId}`); break;
      case "COMPLETED": console.log(`[NOTIFY] Payment completed: ${event.payment.id}`); break;
      case "FAILED":    console.log(`[NOTIFY] Payment FAILED:    ${event.payment.id} — ${event.reason}`); break;
    }
  },
});

// Demo — composition root: wire concretions together
console.log("=== Exercise 06: DIP via Partial Application (Functional Constructor Injection) ===\n");

const repo = makeInMemoryRepository();
const notifier = makeLoggingNotifier();

// Inject dependencies — same as `new PaymentOrchestrator(repo, notifier)` in Java
const orchestrator = makeOrchestrator(repo, notifier);

const payment1 = orchestrator.processPayment("acc-1", 150.00);
console.log("Created:", payment1);

const payment2 = orchestrator.processPayment("acc-2", 75.50);
console.log("Created:", payment2);

const completed = orchestrator.completePayment(payment1.id);
console.log("Completed:", completed);

const failed = orchestrator.failPayment(payment2.id, "Insufficient funds");
console.log("Failed:", failed);

console.log("\nAll payments in repository:", orchestrator.getAllPayments());

// Exercise 07 — Design Patterns Toolkit
// Functional TypeScript equivalent: NO class keyword
// Factory → factory functions
// Observer → typed event bus via closure
// Strategy → HOF accepting the strategy function
// Builder → plain object builder via chained functions

// =============================================================================
// FACTORY PATTERN — factory function replaces factory class
// =============================================================================
type Gateway = { name: string; process: (amount: number) => string };

const createGateway = (type: "stripe" | "paypal" | "square"): Gateway => {
  switch (type) {
    case "stripe":
      return {
        name: "Stripe",
        process: (amount) => `stripe_txn_${amount.toFixed(2).replace(".", "_")}`,
      };
    case "paypal":
      return {
        name: "PayPal",
        process: (amount) => `pp_auth_${amount.toFixed(2).replace(".", "_")}`,
      };
    case "square":
      return {
        name: "Square",
        process: (amount) => `sq_charge_${amount.toFixed(2).replace(".", "_")}`,
      };
  }
};

// =============================================================================
// BUILDER PATTERN — chained builder functions (no mutable class needed)
// =============================================================================
type PaymentRequest = {
  amount?: number;
  currency?: string;
  merchantId?: string;
  idempotencyKey?: string;
  description?: string;
};

type ValidPaymentRequest = Required<PaymentRequest>;

const makePaymentRequestBuilder = () => {
  let req: PaymentRequest = {};
  const builder = {
    amount:         (v: number)  => { req = { ...req, amount: v };         return builder; },
    currency:       (v: string)  => { req = { ...req, currency: v };       return builder; },
    merchantId:     (v: string)  => { req = { ...req, merchantId: v };     return builder; },
    idempotencyKey: (v: string)  => { req = { ...req, idempotencyKey: v }; return builder; },
    description:    (v: string)  => { req = { ...req, description: v };    return builder; },
    build: (): ValidPaymentRequest => {
      const { amount, currency, merchantId, idempotencyKey, description } = req;
      if (amount == null)       throw new Error("amount is required");
      if (!currency)            throw new Error("currency is required");
      if (!merchantId)          throw new Error("merchantId is required");
      if (!idempotencyKey)      throw new Error("idempotencyKey is required");
      return { amount, currency, merchantId, idempotencyKey, description: description ?? "" };
    },
  };
  return builder;
};

// =============================================================================
// OBSERVER PATTERN — typed event bus via closure (no class, just shared state)
// =============================================================================
type PaymentEvent =
  | { type: "created";   paymentId: string; amount: number }
  | { type: "completed"; paymentId: string; amount: number }
  | { type: "failed";    paymentId: string; reason: string };

type Listener<T> = (event: T) => void;

const makeEventBus = <T>() => {
  const listeners: Listener<T>[] = [];
  return {
    subscribe:   (listener: Listener<T>) => { listeners.push(listener); },
    unsubscribe: (listener: Listener<T>) => {
      const idx = listeners.indexOf(listener);
      if (idx !== -1) listeners.splice(idx, 1);
    },
    publish: (event: T) => listeners.forEach((l) => l(event)),
    listenerCount: () => listeners.length,
  };
};

// =============================================================================
// STRATEGY PATTERN — HOF that accepts the strategy function
// =============================================================================
type PricingStrategy = (baseAmount: number, quantity: number) => number;

const standardPricing: PricingStrategy = (base, qty) => base * qty;
const bulkPricing:     PricingStrategy = (base, qty) => base * qty * (qty > 10 ? 0.85 : 1.0);
const premiumPricing:  PricingStrategy = (base, qty) => base * qty * 1.2;

// Curried: bind the strategy, return a function that applies it
const calculatePrice =
  (strategy: PricingStrategy) =>
  (base: number, qty: number): number =>
    strategy(base, qty);

// =============================================================================
// Demo
// =============================================================================
console.log("=== Exercise 07: Design Patterns — Functional Style ===\n");

// Factory
console.log("--- Factory ---");
(["stripe", "paypal", "square"] as const).forEach((type) => {
  const gw = createGateway(type);
  console.log(`  ${gw.name}: ${gw.process(99.99)}`);
});

// Builder
console.log("\n--- Builder ---");
const paymentReq = makePaymentRequestBuilder()
  .amount(100.00)
  .currency("USD")
  .merchantId("merchant-123")
  .idempotencyKey("idem-key-abc")
  .description("Coffee subscription")
  .build();
console.log("  Built request:", paymentReq);

// Observer
console.log("\n--- Observer ---");
const bus = makeEventBus<PaymentEvent>();

const auditListener: Listener<PaymentEvent> = (e) =>
  console.log(`  [Audit] event=${e.type}, paymentId=${e.type !== "failed" ? e.paymentId : e.paymentId}`);

const notifyListener: Listener<PaymentEvent> = (e) => {
  if (e.type === "failed") console.log(`  [Notify] Payment FAILED: ${e.reason}`);
  else console.log(`  [Notify] Payment ${e.type} for $${e.amount}`);
};

bus.subscribe(auditListener);
bus.subscribe(notifyListener);
console.log(`  Listeners registered: ${bus.listenerCount()}`);

bus.publish({ type: "created",   paymentId: "pay-1", amount: 100 });
bus.publish({ type: "completed", paymentId: "pay-1", amount: 100 });
bus.publish({ type: "failed",    paymentId: "pay-2", reason: "Card declined" });

// Strategy
console.log("\n--- Strategy ---");
const standardCalc = calculatePrice(standardPricing);
const bulkCalc     = calculatePrice(bulkPricing);
const premiumCalc  = calculatePrice(premiumPricing);

const base = 10, qty = 15;
console.log(`  Standard (base=${base}, qty=${qty}): $${standardCalc(base, qty).toFixed(2)}`);
console.log(`  Bulk     (base=${base}, qty=${qty}): $${bulkCalc(base, qty).toFixed(2)} (15% discount applied)`);
console.log(`  Premium  (base=${base}, qty=${qty}): $${premiumCalc(base, qty).toFixed(2)} (20% markup)`);

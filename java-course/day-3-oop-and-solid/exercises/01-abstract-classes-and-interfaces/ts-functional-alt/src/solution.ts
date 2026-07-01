// Exercise 01 — Abstract Classes & Interfaces
// Functional TypeScript equivalent: NO class keyword
// Pattern: Template Method → HOF composition

type ChargeRequest = { requestId: string; amount: number; currency: string; customerId: string };
type ChargeResult = { transactionId: string; success: boolean; message: string };
type GatewayImpl = {
  name: string;
  authenticate: () => void;
  submitCharge: (req: ChargeRequest) => ChargeResult;
};

// Concrete "implementations" — plain objects, no classes
const stripeImpl: GatewayImpl = {
  name: "Stripe",
  authenticate: () => {
    // Validate API key (simulated)
    console.log("[Stripe] Authenticating with API key...");
  },
  submitCharge: (req) => ({
    transactionId: `stripe_${req.requestId}_${Date.now()}`,
    success: true,
    message: `Charged ${req.amount} ${req.currency}`,
  }),
};

const paypalImpl: GatewayImpl = {
  name: "PayPal",
  authenticate: () => {
    // Validate OAuth credentials (simulated)
    console.log("[PayPal] Authenticating with OAuth credentials...");
  },
  submitCharge: (req) => ({
    transactionId: `pp_${req.requestId}_${Date.now()}`,
    success: true,
    message: `Authorized ${req.amount} ${req.currency}`,
  }),
};

// Template method as a HOF — the algorithm (authenticate → submit → log) is fixed;
// the impl is pluggable. This is exactly what Java's abstract class does, but explicit.
const processPayment =
  (impl: GatewayImpl) =>
  (req: ChargeRequest): ChargeResult => {
    impl.authenticate();
    const result = impl.submitCharge(req);
    const entry = `[${impl.name}] ${result.success ? "CHARGED" : "FAILED"} ${req.amount} ${req.currency} → ${result.transactionId}`;
    console.log("Audit:", entry);
    return result;
  };

// Demo
const stripeProcess = processPayment(stripeImpl);
const paypalProcess = processPayment(paypalImpl);

const req: ChargeRequest = {
  requestId: "req-1",
  amount: 99.99,
  currency: "USD",
  customerId: "cust-1",
};

console.log("\n=== Exercise 01: Template Method via HOF ===\n");
console.log("Stripe result:", stripeProcess(req));
console.log();
console.log("PayPal result:", paypalProcess({ ...req, requestId: "req-2", amount: 49.00 }));

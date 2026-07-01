// Exercise 05 — SOLID: LSP & ISP
// Functional TypeScript equivalent: NO class keyword
// ISP: TypeScript intersection types replace explicit interface lists
// LSP: structural typing means any object with the right shape is substitutable

// Segregated "interfaces" — type aliases for capabilities (ISP)
type Chargeable  = { charge: (amount: number, currency: string) => string };
type Refundable  = { refund: (transactionId: string, amount: number) => string };
type Auditable   = { getAuditLog: () => string[] };

// Full service: intersection type (equivalent to implements Chargeable, Refundable, Auditable)
type FullPaymentService  = Chargeable & Refundable & Auditable;

// Read-only audit service: only Auditable (ISP — no charge/refund methods exposed)
type ReadOnlyAuditService = Auditable;

// Factory function replaces class constructor
const makeFullPaymentService = (): FullPaymentService => {
  const log: string[] = [];

  return {
    charge: (amount, currency) => {
      const id = `txn-${Date.now()}-${Math.random().toString(36).slice(2, 6)}`;
      const entry = `CHARGE: ${amount} ${currency} → ${id}`;
      log.push(entry);
      console.log(`[PaymentService] ${entry}`);
      return id;
    },

    refund: (txnId, amount) => {
      const id = `refund-${Date.now()}-${Math.random().toString(36).slice(2, 6)}`;
      const entry = `REFUND: ${amount} from ${txnId} → ${id}`;
      log.push(entry);
      console.log(`[PaymentService] ${entry}`);
      return id;
    },

    getAuditLog: () => [...log],  // defensive copy
  };
};

// LSP: read-only view wraps any Auditable — no charge/refund surface exposed
const makeReadOnlyAuditService = (source: Auditable): ReadOnlyAuditService => ({
  getAuditLog: source.getAuditLog,
});

// LSP demonstration: any Chargeable is substitutable wherever a Chargeable is expected
const processCharge = (svc: Chargeable, amount: number, currency: string): string => {
  console.log(`[processCharge] Processing ${amount} ${currency}...`);
  return svc.charge(amount, currency);
};

// LSP: InternationalPaymentRequest extends the base contract — all base clients still work
type PaymentRequest = { amount: number; currency: string };
type InternationalPaymentRequest = PaymentRequest & { swiftCode: string; correspondentBank: string };

const processPaymentRequest = (req: PaymentRequest): void => {
  console.log(`Processing payment: ${req.amount} ${req.currency}`);
};

// Demo
console.log("=== Exercise 05: LSP & ISP via Structural Types ===\n");

const fullSvc = makeFullPaymentService();

// ISP: each client only sees what it needs
const txn1 = processCharge(fullSvc, 100, "USD");   // only sees Chargeable
const txn2 = processCharge(fullSvc, 50, "EUR");    // only sees Chargeable
fullSvc.refund(txn1, 25);                           // Refundable

// ISP: audit service only exposes read access
const auditOnly = makeReadOnlyAuditService(fullSvc);
console.log("\nAudit log (read-only view):", auditOnly.getAuditLog());

// LSP: InternationalPaymentRequest substitutes for PaymentRequest
const intlReq: InternationalPaymentRequest = {
  amount: 500,
  currency: "GBP",
  swiftCode: "BARCGB22",
  correspondentBank: "Barclays",
};
console.log("\nLSP — processing InternationalPaymentRequest as PaymentRequest:");
processPaymentRequest(intlReq);  // works perfectly — Liskov satisfied

// Exercise 03 — Composition Over Inheritance
// Functional TypeScript equivalent: NO class keyword
// Note: This is where the paradigms converge most closely.
// Java's Validator<T> functional interface IS the functional approach.

type ValidationResult = { valid: boolean; errors: string[] };
type Validator<T> = (input: T) => ValidationResult;

type PaymentRequest = { amount: number; currency: string; idempotencyKey: string };

// Constructors for results
const valid = (): ValidationResult => ({ valid: true, errors: [] });
const invalid = (...errors: string[]): ValidationResult => ({ valid: false, errors });

// Combinator — the same concept as Java's Validator.and() instance method,
// but expressed as a free function. Both collect all errors (not fail-fast).
const and = <T>(a: Validator<T>, b: Validator<T>): Validator<T> =>
  (input) => {
    const ra = a(input);
    const rb = b(input);
    return { valid: ra.valid && rb.valid, errors: [...ra.errors, ...rb.errors] };
  };

// Combine an array of validators — pipe over the list
const all = <T>(...validators: Validator<T>[]): Validator<T> =>
  validators.reduce(and);

// Individual validators — pure functions, stateless where possible
const amountValidator: Validator<PaymentRequest> = (req) =>
  req.amount > 0 ? valid() : invalid(`Amount must be positive, got: ${req.amount}`);

const SUPPORTED_CURRENCIES = new Set(["USD", "EUR", "GBP", "JPY", "CAD"]);
const currencyValidator: Validator<PaymentRequest> = (req) =>
  SUPPORTED_CURRENCIES.has(req.currency)
    ? valid()
    : invalid(`Unsupported currency: ${req.currency}. Supported: ${[...SUPPORTED_CURRENCIES].join(", ")}`);

// Stateful validator — closure captures the seen-keys set
const makeDuplicateValidator = (): Validator<PaymentRequest> => {
  const seen = new Set<string>();
  return (req) => {
    if (seen.has(req.idempotencyKey))
      return invalid(`Duplicate idempotency key: ${req.idempotencyKey}`);
    seen.add(req.idempotencyKey);
    return valid();
  };
};

const duplicateValidator = makeDuplicateValidator();

// Composed validator — same as Java's chain of .and() calls
const paymentValidator: Validator<PaymentRequest> = all(
  amountValidator,
  currencyValidator,
  duplicateValidator
);

// Demo
console.log("=== Exercise 03: Composition via HOF Combinators ===\n");

const req1: PaymentRequest = { amount: 100, currency: "USD", idempotencyKey: "key-1" };
const req2: PaymentRequest = { amount: -5,  currency: "USD", idempotencyKey: "key-2" };
const req3: PaymentRequest = { amount: 50,  currency: "XYZ", idempotencyKey: "key-3" };
const req4: PaymentRequest = { amount: 100, currency: "USD", idempotencyKey: "key-1" }; // duplicate

console.log("Valid request:", paymentValidator(req1));
console.log("Negative amount:", paymentValidator(req2));
console.log("Bad currency:", paymentValidator(req3));
console.log("Duplicate key:", paymentValidator(req4));

// Demonstrate composing a subset (e.g., only amount + currency, no duplicate check)
const quickValidator: Validator<PaymentRequest> = and(amountValidator, currencyValidator);
console.log("\nQuick validate (no dup check):", quickValidator({ amount: 25, currency: "EUR", idempotencyKey: "any" }));

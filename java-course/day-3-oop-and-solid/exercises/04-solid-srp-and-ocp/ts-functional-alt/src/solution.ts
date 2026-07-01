// Exercise 04 — SOLID: SRP & OCP
// Functional TypeScript equivalent: NO class keyword
// SRP: each function does one thing
// OCP: registry map = add new strategies without modifying existing ones

type TransactionType = "PURCHASE" | "TRANSFER" | "WITHDRAWAL" | "CRYPTO_SWAP";
type FeeStrategy = (amount: number) => number;

// OCP: the registry is the extension point — add new entries, never edit old ones
const feeRegistry: Map<TransactionType, FeeStrategy> = new Map([
  ["PURCHASE",    (amount) => amount * 0.015],                            // 1.5%
  ["TRANSFER",    (amount) => Math.min(amount * 0.005, 25.0)],           // 0.5%, max $25
  ["WITHDRAWAL",  (_) => 2.50],                                           // flat fee
  ["CRYPTO_SWAP", (amount) => amount * 0.025 + 1.0],                     // 2.5% + $1
]);

// SRP: calculateFee has one job — look up and apply the strategy
const calculateFee = (type: TransactionType, amount: number): number => {
  const strategy = feeRegistry.get(type);
  if (!strategy) throw new Error(`No fee strategy registered for: ${type}`);
  return strategy(amount);
};

// SRP: formatFee has one job — format for display
const formatFee = (amount: number): string => `$${amount.toFixed(2)}`;

// SRP: processTransaction has one job — orchestrate the flow
const processTransaction = (type: TransactionType, amount: number): void => {
  const fee = calculateFee(type, amount);
  console.log(`  ${type}: amount=${formatFee(amount)}, fee=${formatFee(fee)}, total=${formatFee(amount + fee)}`);
};

// Demo
console.log("=== Exercise 04: SRP & OCP via Registry Map ===\n");
console.log("Standard transactions:");
processTransaction("PURCHASE", 100);
processTransaction("TRANSFER", 1000);
processTransaction("WITHDRAWAL", 500);
processTransaction("CRYPTO_SWAP", 500);

// OCP demonstration: extend with a new type WITHOUT touching the existing code above
console.log("\nExtending registry with WIRE_TRANSFER (OCP — no existing code modified):");
(feeRegistry as Map<string, FeeStrategy>).set(
  "WIRE_TRANSFER",
  (amount) => Math.max(amount * 0.001, 15.0)  // 0.1%, min $15
);
const wireStrategy = feeRegistry.get("WIRE_TRANSFER" as TransactionType);
if (wireStrategy) {
  const wireAmount = 5000;
  const wireFee = wireStrategy(wireAmount);
  console.log(`  WIRE_TRANSFER: amount=${formatFee(wireAmount)}, fee=${formatFee(wireFee)}`);
}

// Show all registered strategies
console.log("\nAll registered strategies:", [...feeRegistry.keys()]);

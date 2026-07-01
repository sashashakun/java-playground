// Exercise 02 — Inheritance & Polymorphism
// Functional TypeScript equivalent: NO class keyword
// Pattern: Class hierarchy → discriminated union + exhaustive switch

// Result type — functional error handling without exceptions
type Result<T, E> = { ok: true; value: T } | { ok: false; error: E };
const ok = <T>(value: T): Result<T, never> => ({ ok: true, value });
const err = <E>(error: E): Result<never, E> => ({ ok: false, error });

// Discriminated union replaces abstract class + subclasses
type CheckingAccount = { kind: "checking"; id: string; balance: number; overdraftLimit: number };
type SavingsAccount  = { kind: "savings";  id: string; balance: number; interestRate: number };
type CryptoAccount   = { kind: "crypto";   id: string; balance: number; walletAddress: string; feePercent: number };
type Account = CheckingAccount | SavingsAccount | CryptoAccount;

// Polymorphic withdraw — exhaustive switch guarantees all variants are handled.
// TypeScript will error at compile time if a new Account type is added without updating this.
const withdraw = (account: Account, amount: number): Result<Account, string> => {
  switch (account.kind) {
    case "checking": {
      if (amount > account.balance + account.overdraftLimit)
        return err(`Exceeds overdraft limit (balance: ${account.balance}, limit: ${account.overdraftLimit})`);
      return ok({ ...account, balance: account.balance - amount });
    }
    case "savings": {
      if (amount > account.balance)
        return err(`Insufficient funds (balance: ${account.balance})`);
      return ok({ ...account, balance: account.balance - amount });
    }
    case "crypto": {
      const fee = amount * account.feePercent;
      const total = amount + fee;
      if (total > account.balance)
        return err(`Insufficient crypto balance (need ${total.toFixed(4)}, have ${account.balance})`);
      return ok({ ...account, balance: account.balance - total });
    }
  }
};

// Polymorphic deposit — shared behaviour (no override variation needed)
const deposit = (account: Account, amount: number): Account => ({
  ...account,
  balance: account.balance + amount,
});

const getAccountType = (account: Account): string => {
  switch (account.kind) {
    case "checking": return `Checking (overdraft limit: $${account.overdraftLimit})`;
    case "savings":  return `Savings (interest rate: ${account.interestRate}%)`;
    case "crypto":   return `Crypto (wallet: ${account.walletAddress}, fee: ${account.feePercent * 100}%)`;
  }
};

const getStatement = (account: Account): string =>
  `[${account.id}] ${getAccountType(account)} — Balance: $${account.balance.toFixed(2)}`;

// Demo
console.log("=== Exercise 02: Polymorphism via Discriminated Union ===\n");

const checking: CheckingAccount = { kind: "checking", id: "acc-1", balance: 500, overdraftLimit: 100 };
const savings: SavingsAccount   = { kind: "savings",  id: "acc-2", balance: 1000, interestRate: 2.5 };
const crypto: CryptoAccount     = { kind: "crypto",   id: "acc-3", balance: 2.5, walletAddress: "0xABCD", feePercent: 0.01 };

// Polymorphic deposit — same call, different data shapes
const accounts: Account[] = [checking, savings, crypto];
const afterDeposit = accounts.map((a) => deposit(a, 100));
console.log("After deposit $100 to all accounts:");
afterDeposit.forEach((a) => console.log(" ", getStatement(a)));

console.log();

// Polymorphic withdraw
const r1 = withdraw(checking, 550);
console.log("Withdraw $550 from checking (balance=$500, overdraft=$100):", r1);

const r2 = withdraw(savings, 1500);
console.log("Withdraw $1500 from savings (balance=$1000):", r2);

const r3 = withdraw(crypto, 1.0);
console.log("Withdraw 1.0 BTC from crypto (balance=2.5, fee=1%):", r3);

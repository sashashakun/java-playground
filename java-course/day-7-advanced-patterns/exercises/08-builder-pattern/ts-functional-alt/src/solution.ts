/**
 * Exercise 08 — Builder Pattern (TypeScript Functional Alternative)
 *
 * Instead of a mutable builder class, we use a pipeline of small functions that
 * each accept a config object and return a new immutable object with one field set.
 * Validation is the final step in the pipe — same semantics as Java's build().
 */

type PaymentConfig = {
  amount?: number
  currency?: string
  merchantId?: string
  description?: string
  idempotencyKey?: string
  metadata?: Record<string, string>
}

// Each "with*" function is a curried transformer: takes a value, returns a config -> config fn
const withAmount = (n: number) => (cfg: PaymentConfig): PaymentConfig => ({ ...cfg, amount: n })
const withCurrency = (c: string) => (cfg: PaymentConfig): PaymentConfig => ({ ...cfg, currency: c })
const withMerchantId = (m: string) => (cfg: PaymentConfig): PaymentConfig => ({ ...cfg, merchantId: m })
const withDescription = (d: string) => (cfg: PaymentConfig): PaymentConfig => ({ ...cfg, description: d })
const withIdempotencyKey = (k: string) => (cfg: PaymentConfig): PaymentConfig => ({ ...cfg, idempotencyKey: k })
const withMetadata = (meta: Record<string, string>) => (cfg: PaymentConfig): PaymentConfig => ({
  ...cfg,
  metadata: { ...cfg.metadata, ...meta },
})

// Validation — equivalent to Java's build() throwing IllegalStateException
const validate = (cfg: PaymentConfig): Required<Pick<PaymentConfig, 'amount' | 'currency' | 'merchantId'>> & PaymentConfig => {
  if (cfg.amount === undefined || cfg.amount === null) {
    throw new Error('amount is required')
  }
  if (cfg.amount <= 0) {
    throw new Error(`amount must be greater than 0, got: ${cfg.amount}`)
  }
  if (!cfg.currency) {
    throw new Error('currency is required')
  }
  if (cfg.currency.length !== 3) {
    throw new Error(`currency must be exactly 3 characters, got: '${cfg.currency}'`)
  }
  if (!cfg.merchantId) {
    throw new Error('merchantId is required')
  }
  return cfg as Required<Pick<PaymentConfig, 'amount' | 'currency' | 'merchantId'>> & PaymentConfig
}

// A simple pipe utility — reads left to right (unlike compose which reads right to left)
const pipe = <T>(...fns: Array<(x: T) => T>) =>
  (initial: T): T =>
    fns.reduce((acc, fn) => fn(acc), initial)

// ---- Demo ----

console.log('=== Builder Pattern — Functional Pipe Approach ===\n')

// Happy path: all required fields + optionals
const req1 = validate(
  pipe(
    withAmount(100),
    withCurrency('USD'),
    withMerchantId('merchant-001'),
    withDescription('Software license'),
    withIdempotencyKey('idem-key-abc'),
    withMetadata({ region: 'US', channel: 'web' }),
  )({})
)
console.log('Built request:', req1)

// Required fields only
const req2 = validate(
  withAmount(49.99)(withCurrency('EUR')(withMerchantId('merchant-002')({})))
)
console.log('\nMinimal request:', req2)

// Error case: missing amount
console.log('\n--- Error cases ---')
try {
  validate(withCurrency('GBP')(withMerchantId('merchant-003')({})))
} catch (e) {
  console.log('Missing amount:', (e as Error).message)
}

// Error case: invalid amount
try {
  validate(withAmount(-5)(withCurrency('USD')(withMerchantId('merchant-004')({}))))
} catch (e) {
  console.log('Negative amount:', (e as Error).message)
}

// Error case: invalid currency length
try {
  validate(withAmount(100)(withCurrency('US')(withMerchantId('merchant-005')({}))))
} catch (e) {
  console.log('Bad currency:', (e as Error).message)
}

console.log('\nKey insight: Each step returns a NEW object (immutable spread).')
console.log('validate() is the last fn in the pipe — same semantics as Java build().')

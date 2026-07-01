/**
 * Exercise 10 — Strategy Pattern (TypeScript Functional Alternative)
 *
 * In TypeScript, a strategy is simply a function type. No interface, no class —
 * just pass the function directly. The "engine" is a closure that holds the
 * current strategy and exposes assessRisk().
 *
 * This is where Java and TS converge most: both boil down to "pass a function."
 */

type RiskLevel = 'LOW' | 'MEDIUM' | 'HIGH'
type RiskStrategy = (amount: number, currency: string) => RiskLevel

// Concrete strategies as named arrow functions
const highValueStrategy: RiskStrategy = (amount) =>
  amount > 1000 ? 'HIGH' : amount > 100 ? 'MEDIUM' : 'LOW'

const cryptoStrategy: RiskStrategy = (amount, currency) =>
  currency === 'BTC' ? 'HIGH' : highValueStrategy(amount, currency)

const flatRiskStrategy: RiskStrategy = (_amount, _currency) => 'MEDIUM'

// "Engine" — holds the active strategy; can be swapped at runtime
const riskEngine = (initialStrategy: RiskStrategy) => {
  let strategy = initialStrategy
  return {
    setStrategy: (s: RiskStrategy) => { strategy = s },
    assessRisk: (amount: number, currency: string): RiskLevel => strategy(amount, currency),
  }
}

// ---- Demo ----

console.log('=== Strategy Pattern — Functional Approach ===\n')

const engine = riskEngine(highValueStrategy)

console.log('--- High-value strategy ---')
console.log(`$50 USD:   ${engine.assessRisk(50, 'USD')}`)    // LOW
console.log(`$500 USD:  ${engine.assessRisk(500, 'USD')}`)   // MEDIUM
console.log(`$5000 USD: ${engine.assessRisk(5000, 'USD')}`)  // HIGH

console.log('\n--- Swap to crypto strategy at runtime ---')
engine.setStrategy(cryptoStrategy)
console.log(`$10 BTC:   ${engine.assessRisk(10, 'BTC')}`)    // HIGH (always)
console.log(`$10 ETH:   ${engine.assessRisk(10, 'ETH')}`)    // LOW
console.log(`$500 ETH:  ${engine.assessRisk(500, 'ETH')}`)   // MEDIUM

console.log('\n--- Swap to flat strategy ---')
engine.setStrategy(flatRiskStrategy)
console.log(`$1 USD:    ${engine.assessRisk(1, 'USD')}`)     // MEDIUM (flat)
console.log(`$999 BTC:  ${engine.assessRisk(999, 'BTC')}`)   // MEDIUM (flat)

console.log('\nKey insight:')
console.log('  Java @FunctionalInterface = TS function type alias (type RiskStrategy = ...)')
console.log('  Spring @Qualifier DI = just pass the function directly to riskEngine()')
console.log('  Both allow runtime strategy swap — TS is just less ceremony.')

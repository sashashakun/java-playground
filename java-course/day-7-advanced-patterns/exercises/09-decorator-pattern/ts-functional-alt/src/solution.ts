/**
 * Exercise 09 — Decorator Pattern (TypeScript Functional Alternative)
 *
 * Instead of a class hierarchy (AuditingPaymentService implements PaymentService),
 * we use Higher-Order Functions (HOFs). A decorator is just a function that wraps
 * another function and adds behaviour before/after the call.
 *
 * Stacking decorators: withAudit(withMetrics(simplePaymentFn))
 * — no class, no DI container, fully transparent.
 */

import { randomUUID } from 'node:crypto'

// The "interface" is just a function type
type PaymentFn = (merchantId: string, amount: number, currency: string) => string

// Core implementation — equivalent to SimplePaymentService
const simplePaymentFn: PaymentFn = (_merchantId, _amount, _currency) =>
  `Payment processed: ${randomUUID()}`

// Audit decorator — equivalent to AuditingPaymentService
const withAudit = (fn: PaymentFn): PaymentFn =>
  (merchantId, amount, currency) => {
    console.log(`[AUDIT] Processing payment: merchant=${merchantId} amount=${amount} ${currency}`)
    const result = fn(merchantId, amount, currency)
    console.log(`[AUDIT] Payment result: ${result}`)
    return result
  }

// Timing decorator — showing how easy it is to stack another concern
const withTiming = (fn: PaymentFn): PaymentFn =>
  (merchantId, amount, currency) => {
    const start = Date.now()
    const result = fn(merchantId, amount, currency)
    const elapsed = Date.now() - start
    console.log(`[TIMING] Payment took ${elapsed}ms`)
    return result
  }

// ---- Demo ----

console.log('=== Decorator Pattern — HOF Approach ===\n')

// Single decoration
console.log('--- Single layer (audit only) ---')
const auditedPayment = withAudit(simplePaymentFn)
const result1 = auditedPayment('merchant-001', 150.00, 'USD')
console.log('Return value:', result1)

// Stacked decoration: audit wraps timing wraps core
console.log('\n--- Stacked layers (audit + timing) ---')
const fullyDecoratedPayment = withAudit(withTiming(simplePaymentFn))
const result2 = fullyDecoratedPayment('merchant-002', 2500.00, 'EUR')
console.log('Return value:', result2)

console.log('\nKey insight:')
console.log('  Java: new AuditingPaymentService(new TimingPaymentService(new SimplePaymentService()))')
console.log('  TS:   withAudit(withTiming(simplePaymentFn))')
console.log('  Same semantics, same execution order — but TS needs no class hierarchy.')

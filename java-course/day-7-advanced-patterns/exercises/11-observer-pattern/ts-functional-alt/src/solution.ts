/**
 * Exercise 11 — Observer Pattern (TypeScript Functional Alternative)
 *
 * TypeScript's discriminated unions replace Java's sealed interfaces.
 * The event bus is a factory function returning a closure — no class needed.
 * Exhaustive switch dispatch narrows the union type at compile time,
 * equivalent to Java 21 pattern matching on sealed types.
 */

// Discriminated union — equivalent to Java's sealed interface + records
type TransactionCreated = { type: 'TransactionCreated'; txId: string; amount: number }
type TransactionFlagged = { type: 'TransactionFlagged'; txId: string; reason: string }
type ComplianceEvent = TransactionCreated | TransactionFlagged

type Handler = (event: ComplianceEvent) => void

// Event bus factory — equivalent to ComplianceEventBus class
const createEventBus = () => {
  const handlers: Handler[] = []
  return {
    subscribe: (h: Handler): void => { handlers.push(h) },
    publish: (e: ComplianceEvent): void => { handlers.forEach(h => h(e)) },
    listenerCount: (): number => handlers.length,
  }
}

// ---- Demo ----

console.log('=== Observer Pattern — Functional Event Bus ===\n')

const bus = createEventBus()

// Subscriber 1: logs all events
bus.subscribe(event => {
  // Exhaustive switch: TypeScript narrows the type in each branch
  switch (event.type) {
    case 'TransactionCreated':
      console.log(`[LOG] Transaction created: ${event.txId} amount=$${event.amount}`)
      break
    case 'TransactionFlagged':
      console.log(`[LOG] Transaction flagged: ${event.txId} reason="${event.reason}"`)
      break
    default: {
      // This branch is unreachable — TypeScript enforces exhaustiveness
      const _exhaustive: never = event
      throw new Error(`Unhandled event type: ${JSON.stringify(_exhaustive)}`)
    }
  }
})

// Subscriber 2: compliance alert only for flagged events
bus.subscribe(event => {
  if (event.type === 'TransactionFlagged') {
    console.log(`[COMPLIANCE ALERT] ${event.txId}: ${event.reason}`)
  }
})

// Subscriber 3: audit trail (receives all events)
const auditLog: ComplianceEvent[] = []
bus.subscribe(event => auditLog.push(event))

console.log(`Subscribers registered: ${bus.listenerCount()}\n`)

// Publish events
bus.publish({ type: 'TransactionCreated', txId: 'tx-001', amount: 500.00 })
console.log()
bus.publish({ type: 'TransactionFlagged', txId: 'tx-002', reason: 'Amount exceeds daily limit' })
console.log()
bus.publish({ type: 'TransactionCreated', txId: 'tx-003', amount: 25.00 })

console.log(`\nAudit log captured ${auditLog.length} events`)

console.log('\nKey insight:')
console.log('  Java sealed interface + instanceof = TS discriminated union + switch(event.type)')
console.log('  Both give exhaustiveness checking at compile time.')
console.log('  TS: narrowing is structural ("type" field); Java 21: pattern matching on class.')

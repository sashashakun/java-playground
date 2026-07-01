/**
 * Exercise 12 — Abstract Factory Pattern (TypeScript Functional Alternative)
 *
 * Instead of interface + implementing classes, we use object literals with
 * function properties. The factory function returns a PaymentChannel object
 * (validate + process) based on a discriminated union ChannelType.
 *
 * Adding a new channel: add a case to the switch + add the type to the union.
 * TypeScript enforces exhaustiveness — if you forget a case, it won't compile.
 */

type ChannelType = 'CARD' | 'BANK_TRANSFER'

type PaymentChannel = {
  validate: (payload: string) => boolean
  process: (payload: string) => string
}

const createChannel = (type: ChannelType): PaymentChannel => {
  switch (type) {
    case 'CARD':
      return {
        // Card number: exactly 16 digits
        validate: (payload) => /^\d{16}$/.test(payload),
        process: (payload) => `Card payment processed: ${payload.slice(-4)}`,
      }

    case 'BANK_TRANSFER':
      return {
        // IBAN-style: 2 uppercase letters followed by digits
        validate: (payload) => /^[A-Z]{2}\d+$/.test(payload),
        process: (payload) => `Bank transfer processed: ${payload}`,
      }

    default: {
      // Exhaustiveness check — TypeScript will error if a ChannelType is unhandled
      const _exhaustive: never = type
      throw new Error(`Unknown channel type: ${JSON.stringify(_exhaustive)}`)
    }
  }
}

// ---- Demo ----

console.log('=== Abstract Factory Pattern — Functional Approach ===\n')

// CARD channel
const cardChannel = createChannel('CARD')
const cardPayloads = [
  { payload: '4111111111111111', label: 'Valid 16-digit card' },
  { payload: '411111111111111',  label: 'Too short (15 digits)' },
  { payload: '4111111111111111X', label: 'Contains non-digit' },
]

console.log('--- CARD channel ---')
for (const { payload, label } of cardPayloads) {
  const valid = cardChannel.validate(payload)
  console.log(`${label} [${payload}]: valid=${valid}`)
  if (valid) {
    console.log(`  -> ${cardChannel.process(payload)}`)
  }
}

// BANK_TRANSFER channel
const bankChannel = createChannel('BANK_TRANSFER')
const bankPayloads = [
  { payload: 'DE89370400440532013000', label: 'Valid German IBAN' },
  { payload: 'GB29NWBK60161331926819', label: 'Invalid: contains letters after country code' },
  { payload: '12345678',              label: 'No country code (digits only)' },
  { payload: 'de89370400440532013000', label: 'Lowercase country code (invalid)' },
]

console.log('\n--- BANK_TRANSFER channel ---')
for (const { payload, label } of bankPayloads) {
  const valid = bankChannel.validate(payload)
  console.log(`${label} [${payload}]: valid=${valid}`)
  if (valid) {
    console.log(`  -> ${bankChannel.process(payload)}`)
  }
}

console.log('\nKey insight:')
console.log('  Java: PaymentChannel interface + CardChannel class + BankTransferChannel class')
console.log('  TS:   createChannel() returns an object literal — same shape, zero classes')
console.log('  Both: adding a channel requires touching the factory + (in TS) the union type')
console.log('  Both: structural contract enforced at compile time via types/interfaces')

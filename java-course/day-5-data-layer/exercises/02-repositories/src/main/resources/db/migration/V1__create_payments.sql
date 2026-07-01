CREATE TABLE payments (
    id              VARCHAR(36)     NOT NULL PRIMARY KEY,
    amount          NUMERIC(19,4)   NOT NULL,
    currency        VARCHAR(3)      NOT NULL,
    status          VARCHAR(20)     NOT NULL,
    merchant_id     VARCHAR(255)    NOT NULL,
    description     VARCHAR(200)    NOT NULL,
    idempotency_key VARCHAR(255)    UNIQUE,
    created_at      TIMESTAMP       NOT NULL,
    updated_at      TIMESTAMP
);

CREATE INDEX idx_payment_status   ON payments(status);
CREATE INDEX idx_payment_merchant ON payments(merchant_id);

CREATE TABLE player (
    id BIGINT PRIMARY KEY NOT NULL,
    balance DOUBLE NOT NULL CHECK ( balance >= 0 ),
    currency TEXT NOT NULL CHECK ( currency IN ( 'USD', 'EUR' ) )
);

CREATE TABLE payment_transaction (
    id UUID PRIMARY KEY NOT NULL,
    player BIGINT NOT NULL REFERENCES player (id),
    status TEXT NOT NULL CHECK ( status IN ( 'SUCCESSFUL', 'FAILED' ) )
);

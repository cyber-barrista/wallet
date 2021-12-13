CREATE TABLE player (
    id IDENTITY PRIMARY KEY NOT NULL,
    balance DOUBLE NOT NULL CHECK ( balance >= 0 )
);

CREATE TABLE payment_transaction (
    id UUID PRIMARY KEY NOT NULL,
    payment_value DOUBLE PRECISION NOT NULL,
    player_id BIGINT NOT NULL REFERENCES player (id),
    status TEXT NOT NULL CHECK ( status IN ( 'SUCCESSFUL', 'FAILED' ) )
);

CREATE TABLE client
(
    id   bigserial NOT NULL PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE address
(
    id        bigserial    NOT NULL PRIMARY KEY,
    street    VARCHAR(250) NOT NULL,
    client_id bigint REFERENCES client (id)
);

CREATE TABLE phone
(
    id           bigserial   NOT NULL PRIMARY KEY,
    order_column INT         NOT NULL,
    number       VARCHAR(15) NOT NULL,
    client_id    bigint REFERENCES client (id)
);
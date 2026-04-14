create sequence address_SEQ start with 1 increment by 1;
create sequence phone_SEQ start with 1 increment by 1;

CREATE TABLE address
(
    id        bigint       NOT NULL PRIMARY KEY,
    street    VARCHAR(250) NOT NULL,
    client_id bigint REFERENCES client (id)
);

CREATE TABLE phone
(
    id        bigint      NOT NULL PRIMARY KEY,
    number    VARCHAR(15) NOT NULL,
    client_id bigint REFERENCES client (id)
);
CREATE SEQUENCE email_seq
    INCREMENT 1
    START 1000;

CREATE TABLE email_verification (
    id BIGINT NOT NULL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    code VARCHAR(20) NOT NULL,
    expiration_time TIMESTAMP NOT NULL
);
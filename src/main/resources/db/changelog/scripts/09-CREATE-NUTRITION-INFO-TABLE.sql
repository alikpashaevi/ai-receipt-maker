CREATE SEQUENCE receipts.nutrition_info_seq
    INCREMENT 1
    START 1;

CREATE TABLE receipts.nutrition_info (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    value DOUBLE PRECISION,
    unit VARCHAR(255),
    nutrition_id BIGINT REFERENCES receipts.nutrition(id)
);
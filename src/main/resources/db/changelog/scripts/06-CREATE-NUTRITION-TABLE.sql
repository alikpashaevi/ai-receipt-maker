-- Create sequence for nutrition
CREATE SEQUENCE receipts.nutrition_seq
    INCREMENT 1
    START 1000;

-- Create nutrition table with string-based columns
CREATE TABLE receipts.nutrition (
    id BIGINT PRIMARY KEY,
    calories VARCHAR(50) NOT NULL,
    fat VARCHAR(50) NOT NULL,
    protein VARCHAR(50) NOT NULL,
    carbs VARCHAR(50) NOT NULL
);

CREATE SEQUENCE receipts.history_seq
    INCREMENT 1
    START 1000;

ALTER TABLE receipts.user_history
    ALTER COLUMN id SET DATA TYPE BIGINT;
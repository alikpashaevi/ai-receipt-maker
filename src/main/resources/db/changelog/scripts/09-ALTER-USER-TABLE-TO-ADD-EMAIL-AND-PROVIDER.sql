ALTER TABLE receipts.app_user
    ADD COLUMN email VARCHAR(255) UNIQUE,
    ADD COLUMN provider VARCHAR(50) NOT NULL DEFAULT 'LOCAL',
    ADD COLUMN provider_id VARCHAR(255);

ALTER TABLE receipts.app_user
    ALTER COLUMN PASSWORD DROP NOT NULL;

ALTER TABLE receipts.app_user
    ADD CONSTRAINT unique_provider_user UNIQUE (provider, provider_id);

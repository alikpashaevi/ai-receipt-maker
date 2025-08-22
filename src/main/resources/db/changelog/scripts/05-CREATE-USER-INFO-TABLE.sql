-- Sequence for UserInfo
CREATE SEQUENCE receipts.user_info_seq
    INCREMENT 1
    START 1000;

-- Main table for user info
CREATE TABLE receipts.user_info (
    id BIGINT PRIMARY KEY DEFAULT nextval('receipts.user_info_seq'),
    favorite_cuisine VARCHAR(255),
    vegetarian BOOLEAN,
    vegan BOOLEAN,
    gluten_free BOOLEAN,
    user_id BIGINT UNIQUE,
    CONSTRAINT fk_user_info_user FOREIGN KEY (user_id) REFERENCES receipts.app_user(id)
);

-- Table for disliked ingredients (ElementCollection)
CREATE TABLE receipts.disliked_ingredients (
    user_info_id BIGINT NOT NULL,
    disliked_ingredients VARCHAR(255),
    CONSTRAINT fk_disliked_ingredients_user_info FOREIGN KEY (user_info_id) REFERENCES receipts.user_info(id) ON DELETE CASCADE
);

-- Table for allergies (ElementCollection)
CREATE TABLE receipts.allergies (
    user_info_id BIGINT NOT NULL,
    allergies VARCHAR(255),
    CONSTRAINT fk_allergies_user_info FOREIGN KEY (user_info_id) REFERENCES receipts.user_info(id) ON DELETE CASCADE
);

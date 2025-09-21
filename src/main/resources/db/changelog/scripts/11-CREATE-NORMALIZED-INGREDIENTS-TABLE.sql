-- Normalized ingredients table (element collection)
CREATE TABLE receipts.normalized_ingredients (
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE receipts.recipe_normalized_ingredients (
    recipe_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    PRIMARY KEY (recipe_id, ingredient_id),
    FOREIGN KEY (recipe_id) REFERENCES receipts.recipes (id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES receipts.normalized_ingredients (id) ON DELETE CASCADE
);

CREATE SEQUENCE receipts.normalized_ingredients_seq
    INCREMENT 1
    START 1000;
-- Main recipes table
CREATE TABLE receipts.recipes (
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR(255),
    estimated_time INT,
    servings INT
);

-- Sequence for recipes IDs
CREATE SEQUENCE receipts.recipes_seq_gen
    INCREMENT 1
    START 1000;

-- Ingredients table (element collection)
CREATE TABLE receipts.recipe_ingredients (
    recipe_id BIGINT NOT NULL,
    ingredient VARCHAR(255) NOT NULL,
    PRIMARY KEY (recipe_id, ingredient),
    FOREIGN KEY (recipe_id) REFERENCES receipts.recipes (id) ON DELETE CASCADE
);

-- Instructions table (element collection)
CREATE TABLE receipts.recipe_instructions (
    recipe_id BIGINT NOT NULL,
    instruction VARCHAR(1000) NOT NULL,
    PRIMARY KEY (recipe_id, instruction),
    FOREIGN KEY (recipe_id) REFERENCES receipts.recipes (id) ON DELETE CASCADE
);

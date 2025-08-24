ALTER TABLE receipts.recipes
ADD COLUMN nutrition_id BIGINT,
ADD CONSTRAINT fk_nutrition FOREIGN KEY (nutrition_id) REFERENCES receipts.nutrition(id) ON DELETE SET NULL;
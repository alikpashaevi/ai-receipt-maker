ALTER TABLE receipts.nutrition
DROP COLUMN calories,
DROP COLUMN fat,
DROP COLUMN protein,
DROP COLUMN carbs;

ALTER TABLE receipts.nutrition
    ADD COLUMN recipes_used INTEGER;
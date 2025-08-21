-- Junction table for favorites
CREATE TABLE favorites (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_fav_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_fav_recipe FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
    CONSTRAINT unique_fav UNIQUE (user_id, recipe_id)
);

-- Junction table for user history
CREATE TABLE user_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    viewed_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_hist_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_hist_recipe FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);

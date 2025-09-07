package alik.receiptmaker.components;

import alik.receiptmaker.model.RecipeResponse;
import alik.receiptmaker.persistence.Recipes;

public class RecipeMapper {

    public static RecipeResponse toResponse(Recipes recipe) {
        if (recipe == null) {
            return null;
        }
        Long nutritionId = null;
        if (recipe.getNutrition() != null) {
            nutritionId = recipe.getNutrition().getId();
        }

        return new RecipeResponse(
                recipe.getName(),
                recipe.getIngredients(),
                recipe.getInstructions(),
                recipe.getEstimatedTime(),
                recipe.getServings(),
                nutritionId
        );
    }
}

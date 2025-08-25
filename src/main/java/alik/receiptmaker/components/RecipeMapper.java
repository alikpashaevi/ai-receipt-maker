package alik.receiptmaker.components;

import alik.receiptmaker.model.RecipeResponse;
import alik.receiptmaker.persistence.Recipes;

public class RecipeMapper {

    public static RecipeResponse toResponse(Recipes recipe) {
        if (recipe == null) {
            return null;
        }

        return new RecipeResponse(
                recipe.getName(),
                recipe.getIngredients(),
                recipe.getInstructions(),
                recipe.getEstimatedTime(),
                recipe.getServings()
        );
    }
}

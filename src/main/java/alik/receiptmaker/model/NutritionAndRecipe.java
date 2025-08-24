package alik.receiptmaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class NutritionAndRecipe {
    private RecipeResponse recipeResponse;
    private NutritionResponse nutritionResponse;
}

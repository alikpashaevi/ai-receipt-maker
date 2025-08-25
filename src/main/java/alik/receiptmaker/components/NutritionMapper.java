package alik.receiptmaker.components;

import alik.receiptmaker.model.NutritionInfo;
import alik.receiptmaker.model.NutritionResponse;
import alik.receiptmaker.persistence.Nutrition;

public class NutritionMapper {

    public static NutritionResponse toResponse(Nutrition nutrition) {
        if (nutrition == null) {
            return null;
        }

        NutritionResponse response = new NutritionResponse();
        response.setRecipesUsed(1); // or set dynamically if needed
        response.setCalories(new NutritionInfo(nutrition.getCalories(), "kcal"));
        response.setFat(new NutritionInfo(nutrition.getFat(), "g"));
        response.setProtein(new NutritionInfo(nutrition.getProtein(), "g"));
        response.setCarbs(new NutritionInfo(nutrition.getCarbs(), "g"));

        return response;
    }
}


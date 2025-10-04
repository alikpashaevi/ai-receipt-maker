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
        response.setCalories(nutrition.getCalories());
        response.setFat(nutrition.getFat());
        response.setProtein(nutrition.getProtein());
        response.setCarbs(nutrition.getCarbs());

        return response;
    }
}


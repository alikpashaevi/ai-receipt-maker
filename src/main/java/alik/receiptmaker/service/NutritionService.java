package alik.receiptmaker.service;

import alik.receiptmaker.components.NutritionMapper;
import alik.receiptmaker.model.NutritionInfo;
import alik.receiptmaker.model.NutritionResponse;
import alik.receiptmaker.model.RecipeResponse;
import alik.receiptmaker.persistence.Nutrition;
import alik.receiptmaker.persistence.NutritionRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class NutritionService {

    private final NutritionRepo nutritionRepo;


    public NutritionResponse getNutritionInfo(RecipeResponse recipeResponse) {
        return null;
    }

    public NutritionResponse getNutritionById(long id) {
        return nutritionRepo.findById(id)
                .map(NutritionMapper::toResponse)
                .orElse(null);
    }

    public Nutrition saveNutritionInfo(NutritionResponse nutritionResponse) {
        Nutrition nutrition = new Nutrition();
        nutrition.setCalories(nutritionResponse.getCalories());
        nutrition.setProtein(nutritionResponse.getProtein());
        nutrition.setFat(nutritionResponse.getFat());
        nutrition.setCarbs(nutritionResponse.getCarbs());
        nutritionRepo.save(nutrition);
        return nutrition;
    }

}

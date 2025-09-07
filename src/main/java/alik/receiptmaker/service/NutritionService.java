package alik.receiptmaker.service;

import alik.receiptmaker.components.NutritionMapper;
import alik.receiptmaker.model.NutritionInfo;
import alik.receiptmaker.model.NutritionResponse;
import alik.receiptmaker.model.RecipeResponse;
import alik.receiptmaker.persistence.Nutrition;
import alik.receiptmaker.persistence.NutritionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class NutritionService {

    private final NutritionRepo nutritionRepo;
    private final WebClient webClient;
    private final String apiKey;

    public NutritionResponse getNutritionInfo(RecipeResponse recipeResponse) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes/guessNutrition")
                        .queryParam("title", recipeResponse.getDish_name())
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(NutritionResponse.class)
                .block();
    }

    public NutritionResponse getNutritionById(long id) {
        return nutritionRepo.findById(id)
                .map(NutritionMapper::toResponse)
                .orElse(null);

    }

    public Nutrition saveNutritionInfo(NutritionResponse nutritionResponse) {
        Nutrition nutrition = new Nutrition();
        nutrition.setCalories(nutritionResponse.getCalories().getValue());
        nutrition.setProtein(nutritionResponse.getProtein().getValue());
        nutrition.setFat(nutritionResponse.getFat().getValue());
        nutrition.setCarbs(nutritionResponse.getCarbs().getValue());
        nutritionRepo.save(nutrition);
        return nutrition;
    }

}

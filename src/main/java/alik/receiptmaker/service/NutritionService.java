package alik.receiptmaker.service;

import alik.receiptmaker.model.RecipeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class NutritionService {

    private final WebClient webClient;
    private final String apiKey;

    public String getNutritionInfo(RecipeResponse recipeResponse) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes/guessNutrition")
                        .queryParam("title", recipeResponse.getDish_name())
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}

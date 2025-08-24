package alik.receiptmaker.config;

import alik.receiptmaker.service.NutritionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class NutritionConfig {

    @Value("${spoonacular.api-key}")
    private String apiKey;

    @Bean
    public WebClient nutritionWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl("https://api.spoonacular.com").build();
    }

    @Bean
    public String spoonacularApiKey() {
        return apiKey;
    }
}

package alik.receiptmaker.service;

import alik.receiptmaker.components.GetUsername;
import alik.receiptmaker.components.IngredientsMapper;
import alik.receiptmaker.components.NutritionMapper;
import alik.receiptmaker.components.RecipeMapper;
import alik.receiptmaker.error.NotFoundException;
import alik.receiptmaker.model.NutritionAndRecipe;
import alik.receiptmaker.model.NutritionResponse;
import alik.receiptmaker.model.RecipeResponse;
import alik.receiptmaker.persistence.*;
import alik.receiptmaker.user.UserService;
import alik.receiptmaker.user.persistence.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Normalized;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final ChatModel chatModel;
    private final RecipesRepo recipesRepo;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final UserHistoryService userHistoryService;
    private final UserInfoService userInfoService;
    private final NutritionService nutritionService;
    private final NormalizedIngredientService normalizedIngredientService;

    public NutritionAndRecipe getResponse(List<String> ingredients) {
        AppUser user = userService.getUser(GetUsername.getUsernameFromToken());

        List<String> userPastFoods = user.getHistory().stream().map(
                Recipes::getName
            ).toList(
        );

        int size = userPastFoods.size();
        List<String> lastTenFoods;
        if (size > 5) {
            lastTenFoods = userPastFoods.subList(size - 10, size);
        } else {
            lastTenFoods = userPastFoods;
        }

        List<Recipes> candidateRecipes = recipesRepo.findByAllIngredients(ingredients)
                .stream()
                .filter(r -> !lastTenFoods.contains(r.getName()))
                .toList();

        if (!candidateRecipes.isEmpty()) {
            Recipes chosen = candidateRecipes.getFirst();
            NutritionAndRecipe result = new NutritionAndRecipe();
            result.setRecipeResponse(RecipeMapper.toResponse(chosen));
            if (chosen.getNutrition() != null) {
                result.setNutritionResponse(NutritionMapper.toResponse(chosen.getNutrition()));
            }
            userHistoryService.addToHistory(chosen.getName());
            return result;
        } else {
            UserInfo userInfo = userInfoService.getUserInfoByUserId(user.getId());
            if(userInfo == null) {
                throw new NotFoundException("User info not found. Please set your dietary preferences first.");
            }

            boolean vegetarian = userInfo.isVegetarian();
            boolean vegan = userInfo.isVegan();
            Set<String> allergies = userInfo.getAllergies();
            Set<String> dislikedIngredients = userInfo.getDislikedIngredients();

            String chatResponse = chatModel.call("""
                You are a helpful cooking assistant. Your task is to suggest a dish based on the user's ingredients and preferences.

                ⚠️ IMPORTANT: Return the response in **valid JSON only** with this exact structure:
                {
                  "recipeResponse": {
                    "dish_name": "string",
                    "ingredients": ["1 tbsp olive oil", "2 cloves garlic, minced", "1 tsp black pepper"],
                    "instructions": ["step 1", "step 2", "step 3"],
                    "normalized_ingredients": ["garlic", "pepper"],
                    "estimated_time_minutes": number,
                    "servings": number
                  },
                  "nutritionResponse": {
                    "calories": number,
                    "fat": number,
                    "protein": number,
                    "carbs": number
                  }
                }

                CRITICAL FORMATTING RULES:
                1. **normalized_ingredients**: This must be a simple list of the base ingredient names used in the recipe. Normalize them: remove all quantities, measurements, and preparation notes (e.g., '2 cloves of minced garlic' -> 'garlic')
                2. **ingredients**: This is the list of ingredients *for the recipe you are suggesting*, including the required quantities and preparations
                3. Each nutrition value should be for per 100 grams and should be included in the "nutrition" object
                4. Ensure that the nutritionResponse doesn't contain any additional fields or nutrition values (like fiber, sugar, etc.) beyond calories, fat, protein, and carbs.

                User ingredients: %s
                User preferences: vegetarian = %s, vegan = %s
                User disliked ingredients: %s, allergies = %s
                Try to not suggest the same dishes as last time. Last 5 dishes: %s
                """.formatted(ingredients, vegetarian, vegan, dislikedIngredients, allergies, lastTenFoods));

            int startIndex = chatResponse.indexOf('{');
            int endIndex = chatResponse.lastIndexOf('}');

            String trimmedString = chatResponse;
            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                trimmedString = chatResponse.substring(startIndex, endIndex + 1);
            }

            try {
                NutritionAndRecipe recipeAndNutrition = objectMapper.readValue(trimmedString, NutritionAndRecipe.class);
                NutritionAndRecipe nutritionAndRecipe = new NutritionAndRecipe();
                nutritionAndRecipe.setRecipeResponse(recipeAndNutrition.getRecipeResponse());
                nutritionAndRecipe.setNutritionResponse(recipeAndNutrition.getNutritionResponse());
                if (recipesRepo.existsByName(recipeAndNutrition.getRecipeResponse().getDish_name())) {
                    Recipes existingRecipe = recipesRepo.findByDishName(recipeAndNutrition.getRecipeResponse().getDish_name()).get();
                    RecipeResponse recipe = recipeAndNutrition.getRecipeResponse();
                    recipe.setRecipeId(existingRecipe.getId());
                    recipe.setNutritionId(existingRecipe.getNutrition().getId());
                    userHistoryService.addToHistory(recipe.getDish_name());

                } else {
                    saveRecipe(nutritionAndRecipe);
                    nutritionAndRecipe.getRecipeResponse().setRecipeId(recipesRepo.findByDishName(recipeAndNutrition.getRecipeResponse().getDish_name()).get().getId());
                    nutritionAndRecipe.getRecipeResponse().setNutritionId(recipesRepo.findByDishName(recipeAndNutrition.getRecipeResponse().getDish_name()).get().getNutrition().getId());
                    userHistoryService.addToHistory(recipeAndNutrition.getRecipeResponse().getDish_name());

                }
                return recipeAndNutrition;

            } catch (Exception e) {
                throw new RuntimeException("Failed to parse AI recipe response", e);
            }
        }
    }

    public void saveRecipe(NutritionAndRecipe nutritionAndRecipe) {
        Recipes recipe = new Recipes();
        recipe.setName(nutritionAndRecipe.getRecipeResponse().getDish_name());
        recipe.setIngredients(nutritionAndRecipe.getRecipeResponse().getIngredients());
        recipe.setInstructions(nutritionAndRecipe.getRecipeResponse().getInstructions());
        Set<NormalizedIngredients> normalizedIngredients =
                normalizedIngredientService.saveNormalizedIngredients(
                        nutritionAndRecipe.getRecipeResponse().getNormalized_ingredients()
                );
        recipe.setNormalizedIngredients(normalizedIngredients);
        recipe.setNormalizedIngredients(normalizedIngredients);
        recipe.setEstimatedTime(nutritionAndRecipe.getRecipeResponse().getEstimated_time_minutes());
        recipe.setServings(nutritionAndRecipe.getRecipeResponse().getServings());
        recipe.setNutrition(nutritionService.saveNutritionInfo(nutritionAndRecipe.getNutritionResponse()));

        recipesRepo.save(recipe);
    }

    public List<Recipes> getRecipes() {
        return recipesRepo.findAll();
    }

    public NutritionAndRecipe getRecipeResponseById(long id) {
        NutritionAndRecipe nutritionAndRecipe = new NutritionAndRecipe();
        RecipeResponse recipe = recipesRepo.findById(id)
                .map(RecipeMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));

        if(recipe.getNutritionId() != null) {
            NutritionResponse nutrition = nutritionService.getNutritionById(recipe.getNutritionId());
            nutritionAndRecipe.setNutritionResponse(nutrition);
        };

        nutritionAndRecipe.setRecipeResponse(recipe);
        return nutritionAndRecipe;
    }

    public Page<Recipes> getRecipesByIngredients(List<String> ingredients, int page, int pageSize) {
        return recipesRepo.findByAllIngredientsPage(ingredients, PageRequest.of(page, pageSize));
    }
}

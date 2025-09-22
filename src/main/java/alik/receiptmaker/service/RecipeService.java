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

        // get user preferences
        AppUser user = userService.getUser(GetUsername.getUsernameFromToken());

        List<String> userPastFoods = user.getHistory().stream().map(
                Recipes::getName
            ).toList(
        );

        int size = userPastFoods.size();
        List<String> lastFiveFoods;
        if (size > 5) {
            lastFiveFoods = userPastFoods.subList(size - 5, size);
        } else {
            lastFiveFoods = userPastFoods;
        }
        System.out.println("lastFiveFoods = " + lastFiveFoods);
        System.out.println("ingredients = " + ingredients);
        List<Recipes> candidateRecipes = recipesRepo.findByAllIngredients(ingredients)
                .stream()
                .filter(r -> !lastFiveFoods.contains(r.getName()))
                .toList();

        System.out.println("candidateRecipes = " + candidateRecipes);

        if (!candidateRecipes.isEmpty()) {
            Recipes chosen = candidateRecipes.getFirst();
            NutritionAndRecipe result = new NutritionAndRecipe();
            result.setRecipeResponse(RecipeMapper.toResponse(chosen));
            System.out.println("here");
            if (chosen.getNutrition() != null) {
                result.setNutritionResponse(NutritionMapper.toResponse(chosen.getNutrition()));
            }
            userHistoryService.addToHistory(chosen.getName());
            System.out.println("here 2");
            return result;
        } else {
            UserInfo userInfo = userInfoService.getUserInfoByUserId(user.getId());

            boolean vegetarian = userInfo.isVegetarian();
            boolean vegan = userInfo.isVegan();
            Set<String> allergies = userInfo.getAllergies();
            Set<String> dislikedIngredients = userInfo.getDislikedIngredients();

            String chatResponse = "{\n" +
                    "  \"dish_name\": \"Rice\",\n" +
                    "  \"ingredients\": [\n" +
                    "    \"2 boneless, skinless chicken breasts, cut into 1-inch pieces\",\n" +
                    "    \"1 large head of broccoli, cut into florets\",\n" +
                    "    \"1 tbsp sesame oil\",\n" +
                    "    \"2 cloves garlic, minced\",\n" +
                    "    \"1 tbsp grated ginger\",\n" +
                    "    \"1/4 cup soy sauce\",\n" +
                    "    \"1 tbsp honey or maple syrup\",\n" +
                    "    \"1 tsp cornstarch\",\n" +
                    "    \"Cooked rice, for serving\"\n" +
                    "  ],\n" +
                    "  \"instructions\": [\n" +
                    "    \"In a small bowl, whisk together the soy sauce, honey, and cornstarch. Set aside.\",\n" +
                    "    \"Heat sesame oil in a large skillet or wok over medium-high heat. Add the chicken and cook until browned on all sides. Remove the chicken from the skillet.\",\n" +
                    "    \"Add the broccoli florets to the same skillet and stir-fry for 3-4 minutes until they are bright green and slightly tender. Add the garlic and ginger, and cook for another minute until fragrant.\",\n" +
                    "    \"Return the cooked chicken to the skillet. Pour the soy sauce mixture over the chicken and broccoli. Cook, stirring constantly, for 1-2 minutes until the sauce thickens and coats everything.\",\n" +
                    "    \"Serve immediately over cooked rice.\"\n" +
                    "  ],\n" +
                    "  \"normalized_ingredients\": [\n" +
                    "    \"chicken breasts\",\n" +
                    "    \"broccoli\"\n" +
                    "  ],\n" +
                    "  \"estimated_time_minutes\": 25,\n" +
                    "  \"servings\": 4\n" +
                    "}";

//            String chatResponse = chatModel.call(
//                    "You are a helpful cooking assistant. Your task is to suggest a dish based on the user's ingredients and preferences. \n" +
//                            "\n" +
//                            "⚠️ IMPORTANT: Return the response in **valid JSON only** with this exact structure:\n" +
//                            "{\n" +
//                            "  \"dish_name\": \"string\",\n" +
//                            "  \"ingredients\": [\"1 tbsp olive oil\", \"2 cloves garlic, minced\", \"1 tsp black pepper\"],\n" +
//                            "  \"instructions\": [\"step 1\", \"step 2\", \"step 3\"],\n" +
//                            "  \"normalized_ingredients\": [\"garlic\", \"pepper\"],\n" +
//                            "  \"estimated_time_minutes\": number,\n" +
//                            "  \"servings\": number\n" +
//                            "}\n" +
//                            "\n" +
//                            "CRITICAL FORMATTING RULES:\n" +
//                            "1. **normalized_user_ingredients**: This must be a simple list of the base ingredient names provided by the user. Normalize them: remove all quantities, measurements, and preparation notes (e.g., '2 cloves of minced garlic' -> 'garlic').\n" +
//                            "2. **ingredients**: This is the list of ingredients *for the recipe you are suggesting*, including the required quantities and preparations.\n" +
//                            "\n" +
//                            "User ingredients: " + ingredients + "\n" +
//                            "User preferences: vegetarian = " + vegetarian + ", vegan = " + vegan + "\n" +
//                            "User disliked ingredients: " + dislikedIngredients + ", allergies =" + allergies + "\n" +
//                            "Try to not suggest the same dishes as last time. Last 5 dishes: " + lastFiveFoods + "\n"
//            );


            int startIndex = chatResponse.indexOf('{');
            int endIndex = chatResponse.lastIndexOf('}');

            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                String trimmedString = chatResponse.substring(startIndex, endIndex + 1);
                try {
                    RecipeResponse recipe = objectMapper.readValue(trimmedString, RecipeResponse.class);
                    NutritionAndRecipe nutritionAndRecipe = new NutritionAndRecipe();
                    nutritionAndRecipe.setRecipeResponse(recipe);
                    if (recipesRepo.existsByName(recipe.getDish_name())) {
                        Recipes existingRecipe = recipesRepo.findByDishName(recipe.getDish_name()).get();
                        recipe.setRecipeId(existingRecipe.getId());
                        recipe.setNutritionId(existingRecipe.getNutrition().getId());
                        userHistoryService.addToHistory(recipe.getDish_name());
                        if (recipe.getNutritionId() != null) {
                            nutritionAndRecipe.setNutritionResponse(nutritionService.getNutritionById(recipe.getNutritionId()));
                        }
                    } else {
                        if (recipe.getNutritionId() == null || nutritionService.getNutritionById(recipe.getNutritionId()) == null) {
                            NutritionResponse nutrition = nutritionService.getNutritionInfo(recipe);
                            System.out.println("nutrition = " + nutrition);

                            nutritionAndRecipe.setNutritionResponse(nutrition);
                            saveRecipe(nutritionAndRecipe);
                                // set the recipeId after saving
                            nutritionAndRecipe.getRecipeResponse().setRecipeId(recipesRepo.findByDishName(recipe.getDish_name()).get().getId());
                            nutritionAndRecipe.getRecipeResponse().setNutritionId(recipesRepo.findByDishName(recipe.getDish_name()).get().getNutrition().getId());
    //                        if (recipesRepo.findByDishName(recipe.getNu))
                            userHistoryService.addToHistory(recipe.getDish_name());
                        }
                    }
                    return nutritionAndRecipe;

                } catch (Exception e) {
                    throw new RuntimeException("Failed to parse AI recipe response", e);
                }
        }

        }


        throw new RuntimeException("AI response did not contain valid JSON");
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
        if (nutritionAndRecipe.getNutritionResponse() != null) {
            System.out.println(nutritionAndRecipe.getNutritionResponse());
            recipe.setNutrition(nutritionService.saveNutritionInfo(nutritionAndRecipe.getNutritionResponse()));
        }

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

package alik.receiptmaker.service;

import alik.receiptmaker.components.GetUsername;
import alik.receiptmaker.components.NutritionMapper;
import alik.receiptmaker.components.RecipeMapper;
import alik.receiptmaker.model.NutritionAndRecipe;
import alik.receiptmaker.model.NutritionResponse;
import alik.receiptmaker.model.RecipeResponse;
import alik.receiptmaker.persistence.Nutrition;
import alik.receiptmaker.persistence.Recipes;
import alik.receiptmaker.persistence.RecipesRepo;
import alik.receiptmaker.persistence.UserInfo;
import alik.receiptmaker.user.UserService;
import alik.receiptmaker.user.persistence.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

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

        List<Recipes> candidateRecipes = recipesRepo.findByAllIngredients(ingredients)
                .stream()
                .filter(r -> !lastFiveFoods.contains(r.getName()))
                .toList();

        if (!candidateRecipes.isEmpty()) {
            Recipes chosen = candidateRecipes.getFirst();
            NutritionAndRecipe result = new NutritionAndRecipe();


            result.setRecipeResponse(RecipeMapper.toResponse(chosen));
            result.setNutritionResponse(NutritionMapper.toResponse(chosen.getNutrition()));
            userHistoryService.addToHistory(chosen.getName());
            return result;
        } else {
            UserInfo userInfo = userInfoService.getUserInfoByUserId(user.getId());

            boolean vegetarian = userInfo.isVegetarian();
            boolean vegan = userInfo.isVegan();
            Set<String> allergies = userInfo.getAllergies();
            Set<String> dislikedIngredients = userInfo.getDislikedIngredients();


            String chatResponse = "{\n" +
                    "  \"dish_name\": \"Chicken and Broccoli\",\n" +
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
                    "  \"estimated_time_minutes\": 25,\n" +
                    "  \"servings\": 4\n" +
                    "}";

    //        String chatResponse = chatModel.call(
    //                "You are a helpful cooking assistant. \n" +
    //                "The user will provide a list of ingredients. \n" +
    //                "Suggest one dish they can cook, with step-by-step instructions. The dish name should be precise \n" +
    //                "\n" +
    //                "⚠️ IMPORTANT: Return the response in **valid JSON only** with this exact structure:\n" +
    //                "{\n" +
    //                "  \"dish_name\": \"string\",\n" +
    //                "  \"ingredients\": [\"list\", \"of\", \"ingredients\"],\n" +
    //                "  \"instructions\": [\"step 1\", \"step 2\", \"step 3\"],\n" +
    //                "  \"estimated_time_minutes\": number,\n" +
    //                "  \"servings\": number\n" +
    //                "}\n" +
    //                "\n" +
    //                "User ingredients: " + ingredients + "\n" +
    //                "User preferences: vegetarian = " + vegetarian + ", vegan = " + vegan + "\n" +
    //                "User disliked ingredients: " + dislikedIngredients + ", allergies =" + allergies + "\n" +
    //                "Try to not suggest the same dishes as last time. Last 5 dishes: " + lastFiveFoods + "\n"
    //        );



            int startIndex = chatResponse.indexOf('{');
            int endIndex = chatResponse.lastIndexOf('}');

            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                String trimmedString = chatResponse.substring(startIndex, endIndex + 1);
                try {
                    RecipeResponse recipe = objectMapper.readValue(trimmedString, RecipeResponse.class);
                    NutritionResponse nutrition = nutritionService.getNutritionInfo(recipe);
                    NutritionAndRecipe nutritionAndRecipe = new NutritionAndRecipe();
                    if (!recipesRepo.existsByName(recipe.getDish_name())) {
                        saveRecipe(recipe, nutrition);
                    }
                    // add to user history
                    // TODO: it returns a nutritionAndRecipe object even if the recipe exists in db. FIX IT!
                    // The whole method returns nutritionAndRecipe object.
                    userHistoryService.addToHistory(recipe.getDish_name());
                    nutritionAndRecipe.setRecipeResponse(recipe);
                    nutritionAndRecipe.setNutritionResponse(nutrition);
                    return nutritionAndRecipe;

                } catch (Exception e) {
                    throw new RuntimeException("Failed to parse AI recipe response", e);
                }
        }

        }


        throw new RuntimeException("AI response did not contain valid JSON");
    }

    public void saveRecipe(RecipeResponse recipeResponse, NutritionResponse nutritionResponse) {

        Recipes recipe = new Recipes();
        recipe.setName(recipeResponse.getDish_name());
        recipe.setIngredients(recipeResponse.getIngredients());
        recipe.setInstructions(recipeResponse.getInstructions());
        recipe.setEstimatedTime(recipeResponse.getEstimated_time_minutes());
        recipe.setServings(recipeResponse.getServings());
        recipe.setNutrition(nutritionService.saveNutritionInfo(nutritionResponse));

        recipesRepo.save(recipe);
    }

    public List<Recipes> getRecipes() {
        return recipesRepo.findAll();
    }

    public Optional<Recipes> findByDishName(String name) {
        return recipesRepo.findByDishName(name);
    }

//    public NutritionAndRecipe findByIngredients(List<String> ingredients, List<String> lastFiveFoods) {
//        List<Recipes> recipes = recipesRepo.findByAllIngredients(ingredients, ingredients.size());
//        for (int i = 0; i < lastFiveFoods.size(); i++) {
//            if(recipes.get(i).getName().equals(lastFiveFoods.get(i))) {
//
//            }
//        }


//    }
}

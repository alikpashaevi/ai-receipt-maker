package alik.receiptmaker.service;

import alik.receiptmaker.model.RecipeResponse;
import alik.receiptmaker.persistence.Recipes;
import alik.receiptmaker.persistence.RecipesRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final ChatModel chatModel;
    private final RecipesRepo recipesRepo;
    private final ObjectMapper objectMapper;

    public RecipeResponse getResponse(List<String> ingredients, boolean vegetarian, List<String> allergies) {

        if (allergies.isEmpty()) {
            allergies.add("none");
        }

        String chatResponse = chatModel.call(
                "You are a helpful cooking assistant. \n" +
                        "The user will provide a list of ingredients. \n" +
                        "Suggest one dish they can cook, with step-by-step instructions. \n" +
                        "\n" +
                        "⚠\uFE0F IMPORTANT: Return the response in **valid JSON only** with this exact structure:\n" +
                        "{\n" +
                        "  \"dish_name\": \"string\",\n" +
                        "  \"ingredients\": [\"list\", \"of\", \"ingredients\"],\n" +
                        "  \"instructions\": [\"step 1\", \"step 2\", \"step 3\"],\n" +
                        "  \"estimated_time_minutes\": number,\n" +
                        "  \"servings\": number\n" +
                        "}\n" +
                        "\n" +
                        "User ingredients: " + ingredients + "\n" +
                        "User preferences: vegetarian = " + vegetarian + ", allergies = " + allergies + "\n"

        );



        int startIndex = chatResponse.indexOf('{');
        int endIndex = chatResponse.lastIndexOf('}');

        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            String trimmedString = chatResponse.substring(startIndex, endIndex + 1);
            try {
                RecipeResponse recipe = objectMapper.readValue(trimmedString, RecipeResponse.class);
                saveRecipe(recipe);
                return recipe;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public void saveRecipe(RecipeResponse recipeResponse) {

        Recipes recipe = new Recipes();
        recipe.setName(recipeResponse.getDish_name());
        recipe.setIngredients(recipeResponse.getIngredients());
        recipe.setInstructions(recipeResponse.getInstructions());
        recipe.setEstimatedTime(recipeResponse.getEstimated_time_minutes());
        recipe.setServings(recipeResponse.getServings());

        recipesRepo.save(recipe);
    }

    public List<Recipes> getRecipes() {
        return recipesRepo.findAll();
    }

    public Recipes getRecipeById(long id) {
        return recipesRepo.findById(id).orElse(null);
    }
}

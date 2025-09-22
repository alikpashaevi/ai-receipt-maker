package alik.receiptmaker.controller;

import alik.receiptmaker.model.NutritionAndRecipe;
import alik.receiptmaker.model.RecipeResponse;
import alik.receiptmaker.persistence.Recipes;
import alik.receiptmaker.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static alik.receiptmaker.constants.AuthorizationConstants.USER_OR_ADMIN;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@PreAuthorize(USER_OR_ADMIN)
public class RecipeController {

    private final RecipeService recipeService;


    @GetMapping("/ask-ai")
    public NutritionAndRecipe getResponse(@RequestParam List<String> ingredients) {
        return recipeService.getResponse(ingredients);
    }

    @GetMapping
    public List<Recipes> getRecipes() {
        return recipeService.getRecipes();
    }

    @GetMapping("/{id}")
    public NutritionAndRecipe getRecipeById(@PathVariable long id) {
        return recipeService.getRecipeResponseById(id);
    }

    @GetMapping("/search")
    public Page<Recipes> searchRecipes(@RequestParam List<String> ingredients,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int pageSize) {
        return recipeService.getRecipesByIngredients(ingredients, page, pageSize);
    }
}

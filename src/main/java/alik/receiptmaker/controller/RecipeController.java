package alik.receiptmaker.controller;

import alik.receiptmaker.model.RecipeResponse;
import alik.receiptmaker.persistence.Recipes;
import alik.receiptmaker.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static alik.receiptmaker.constants.AuthorizationConstants.USER_OR_ADMIN;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@PreAuthorize(USER_OR_ADMIN)
public class RecipeController {

    private final RecipeService recipeService;


    @GetMapping("/ask-ai")
    public RecipeResponse getResponse(@RequestParam List<String> ingredients,
                                      @RequestParam boolean vegetarian,
                                      @RequestParam(required = false) List<String> allergies) {
        return recipeService.getResponse(ingredients, vegetarian, allergies);
    }

    @GetMapping
    public List<Recipes> getRecipes() {
        return recipeService.getRecipes();
    }


}

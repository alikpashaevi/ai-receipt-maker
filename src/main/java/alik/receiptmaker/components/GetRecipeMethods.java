package alik.receiptmaker.components;

import alik.receiptmaker.error.NotFoundException;
import alik.receiptmaker.persistence.Recipes;
import alik.receiptmaker.persistence.RecipesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GetRecipeMethods {

    private final RecipesRepo recipesRepo;

    public Recipes getRecipeById(long id) {
        return recipesRepo.findById(id).orElseThrow(() -> new NotFoundException("Recipe not found"));
    }

    public Recipes getRecipeByName(String name) {
        return recipesRepo.findByDishName(name).orElseThrow(() -> new NotFoundException("Recipe not found"));
    }

    public boolean existsByName(String name) {
        return recipesRepo.existsByName(name);
    }

}

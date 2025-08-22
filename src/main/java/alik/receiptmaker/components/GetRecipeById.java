package alik.receiptmaker.components;

import alik.receiptmaker.persistence.Recipes;
import alik.receiptmaker.persistence.RecipesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GetRecipeById {

    private final RecipesRepo recipesRepo;

    public Recipes getRecipeById(long id) {
        return recipesRepo.findById(id).orElse(null);
    }

}

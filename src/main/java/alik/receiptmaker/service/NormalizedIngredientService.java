package alik.receiptmaker.service;

import alik.receiptmaker.persistence.NormalizedIngredients;
import alik.receiptmaker.persistence.NormalizedIngredientsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NormalizedIngredientService {

    private final NormalizedIngredientsRepo normalizedIngredientsRepo;

    public Set<NormalizedIngredients> saveNormalizedIngredients(Set<String> ingredientNames) {
        Set<NormalizedIngredients> ingredients = new HashSet<>();

        for (String name : ingredientNames) {
            // Check if ingredient already exists (to avoid duplicates)
            NormalizedIngredients existing = normalizedIngredientsRepo.findByName(name);
            if (existing != null) {
                ingredients.add(existing);
            } else {
                NormalizedIngredients newIngredient = new NormalizedIngredients();
                newIngredient.setName(name);
                ingredients.add(normalizedIngredientsRepo.save(newIngredient));
            }
        }

        return ingredients;
    }

}

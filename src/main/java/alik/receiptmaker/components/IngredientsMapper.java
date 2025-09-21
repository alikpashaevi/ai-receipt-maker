package alik.receiptmaker.components;

import alik.receiptmaker.persistence.NormalizedIngredients;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class IngredientsMapper {
    public static Set<NormalizedIngredients> mapToNormalizedIngredients(Set<String> ingredientNames) {
        if (ingredientNames == null) return new HashSet<>();

        return ingredientNames.stream()
                .map(name -> {
                    NormalizedIngredients ingredient = new NormalizedIngredients();
                    ingredient.setName(name);
                    return ingredient;
                })
                .collect(Collectors.toSet());
    }

    public static Set<String> mapToIngredientNames(Set<NormalizedIngredients> ingredients) {
        if (ingredients == null) return new HashSet<>();

        return ingredients.stream()
                .map(NormalizedIngredients::getName)
                .collect(Collectors.toSet());
    }
}

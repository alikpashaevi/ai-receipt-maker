package alik.receiptmaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserInfoRequest {
    private String favoriteCuisine;
    private Set<String> dislikedIngredients;
    private Set<String> allergies;
    private boolean vegetarian;
    private boolean vegan;
    private boolean glutenFree;
}

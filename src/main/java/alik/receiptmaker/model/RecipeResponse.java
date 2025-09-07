package alik.receiptmaker.model;

import alik.receiptmaker.persistence.Nutrition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponse {
    private String dish_name;
    private List<String> ingredients;
    private List<String> instructions;
    private int estimated_time_minutes;
    private int servings;
    private Long nutritionId;
}

package alik.receiptmaker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NutritionResponse {
    @JsonProperty("recipesUsed")
    private int recipesUsed;
    @JsonProperty("calories")
    private NutritionInfo calories;
    @JsonProperty("fat")
    private NutritionInfo fat;
    @JsonProperty("protein")
    private NutritionInfo protein;
    @JsonProperty("carbs")
    private NutritionInfo carbs;
}

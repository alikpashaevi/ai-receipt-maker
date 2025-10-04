package alik.receiptmaker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NutritionResponse {
    @JsonProperty("calories")
    private String calories;
    @JsonProperty("fat")
    private String fat;
    @JsonProperty("protein")
    private String protein;
    @JsonProperty("carbs")
    private String carbs;
}

package alik.receiptmaker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NutritionInfo {
    @JsonProperty("value")
    private int value;
    @JsonProperty("unit")
    private String unit;
}

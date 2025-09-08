package alik.receiptmaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserFavoritesDTO {
    private long recipeId;
    private String dishName;
}

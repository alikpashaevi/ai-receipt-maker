package alik.receiptmaker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserHistoryDTO {
    private long recipeId;
    private String dishName;
    private LocalDateTime viewedAt;
}

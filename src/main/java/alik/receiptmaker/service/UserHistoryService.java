package alik.receiptmaker.service;

import alik.receiptmaker.components.GetRecipeMethods;
import alik.receiptmaker.components.GetUsername;
import alik.receiptmaker.model.UserHistoryDTO;
import alik.receiptmaker.persistence.Recipes;
import alik.receiptmaker.persistence.UserHistory;
import alik.receiptmaker.persistence.UserHistoryRepo;
import alik.receiptmaker.user.UserService;
import alik.receiptmaker.user.persistence.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHistoryService {

    private final UserHistoryRepo userHistoryRepo;
    private final UserService userService;
    private final GetRecipeMethods getRecipeMethods;

    private UserHistoryDTO mapUserHistoryToDto(UserHistory userHistory) {
        if (userHistory == null) {
            return null;
        }

        return new UserHistoryDTO(
                userHistory.getRecipe().getId(),
                userHistory.getRecipe().getName(),
                userHistory.getViewedAt()
        );
    }

    public List<UserHistoryDTO> getUserHistory() {
        AppUser user = userService.getUser(GetUsername.getUsernameFromToken());

        List<UserHistory> userHistory = userHistoryRepo.findTop5ByUserOrderByViewedAtDesc(user);
        return userHistory.stream()
                .map(this::mapUserHistoryToDto)
                .toList();
    }

    @Transactional
    public void addToHistory(String dishName) {
        if (getRecipeMethods.existsByName(dishName)) {
            AppUser user = userService.getUser(GetUsername.getUsernameFromToken());
            Recipes recipeToAdd = getRecipeMethods.getRecipeByName(dishName);

            if (userHistoryRepo.findAll().size() > 5) {
                UserHistory oldestEntry = userHistoryRepo.findTopByUserOrderByViewedAtAsc(user);
                if (oldestEntry != null) {
                    userHistoryRepo.delete(oldestEntry);
                }
            }

            if (userHistoryRepo.existsByUserAndRecipe(user, recipeToAdd)) {
                UserHistory existingEntry = userHistoryRepo.findByUserAndRecipe(user, recipeToAdd);
                if (existingEntry != null) {
                    existingEntry.setViewedAt(LocalDateTime.now());
                    userHistoryRepo.save(existingEntry);
                    return;
                }
            }

            UserHistory userHistory = new UserHistory();
            userHistory.setUser(user);
            userHistory.setRecipe(recipeToAdd);
            userHistory.setViewedAt(LocalDateTime.now());

            userHistoryRepo.save(userHistory);
        } else {
            throw new RuntimeException("Recipe with this name does not exist");
        }

    }

}

package alik.receiptmaker.persistence;

import alik.receiptmaker.user.persistence.AppUser;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHistoryRepo extends JpaRepository<UserHistory, Long> {
    List<UserHistory> findTop5ByUserOrderByViewedAtDesc(AppUser user);

    UserHistory findTopByUserOrderByViewedAtAsc(AppUser user);

    boolean existsByUserAndRecipe(AppUser user, Recipes recipeToAdd);

    UserHistory findByUserAndRecipe(AppUser user, Recipes recipeToAdd);
}

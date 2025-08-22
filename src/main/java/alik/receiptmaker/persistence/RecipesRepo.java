package alik.receiptmaker.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipesRepo extends JpaRepository<Recipes, Long> {
    @Query("SELECT r FROM Recipes r WHERE r.name = :dishName" )
    Optional<Recipes> findByDishName(String dishName);


}

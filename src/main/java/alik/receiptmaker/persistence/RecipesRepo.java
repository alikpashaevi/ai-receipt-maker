package alik.receiptmaker.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipesRepo extends JpaRepository<Recipes, Long> {
    @Query("SELECT r FROM Recipes r WHERE r.name = :dishName" )
    Optional<Recipes> findByDishName(String dishName);

    // exists by dish name
    boolean existsByName(String dishName);

    @Query("""
    SELECT r FROM Recipes r
    JOIN r.ingredients i
    WHERE i IN :ingredients
    GROUP BY r
    HAVING COUNT(DISTINCT i) = :#{#ingredients.size()}
""")
    List<Recipes> findByAllIngredients(@Param("ingredients") List<String> ingredients);



}

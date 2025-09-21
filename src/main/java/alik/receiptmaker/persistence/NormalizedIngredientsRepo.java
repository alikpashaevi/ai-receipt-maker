package alik.receiptmaker.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NormalizedIngredientsRepo extends JpaRepository<NormalizedIngredients, Long> {
    NormalizedIngredients findByName(String name);
}

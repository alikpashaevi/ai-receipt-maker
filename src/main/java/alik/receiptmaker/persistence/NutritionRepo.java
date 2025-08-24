package alik.receiptmaker.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionRepo extends JpaRepository<Nutrition, Long> {
}

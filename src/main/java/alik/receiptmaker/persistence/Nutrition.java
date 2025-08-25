package alik.receiptmaker.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "nutrition", schema = "receipts")
@SequenceGenerator(name = "nutrition_seq_gen", sequenceName = "nutrition_seq", allocationSize = 1)
@Getter
@Setter
public class Nutrition {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nutrition_seq_gen")
    private long id;

    @OneToMany(mappedBy = "nutrition", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<NutritionInfo> nutritionInfo;

}

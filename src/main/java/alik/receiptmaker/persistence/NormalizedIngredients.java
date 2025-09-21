package alik.receiptmaker.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "normalized_ingredients")
@SequenceGenerator(name = "normalized_ingredients_seq_gen", sequenceName = "normalized_ingredients_seq", allocationSize = 1)
@Getter
@Setter
public class NormalizedIngredients {

    @Id
    @GeneratedValue(generator = "normalized_ingredients_seq_gen", strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "name")
    private String name;
}

package alik.receiptmaker.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "recipes", schema = "receipts")
@SequenceGenerator(name = "recipes_seq_gen", sequenceName = "recipes_seq_gen", allocationSize = 1)
@Getter
@Setter
public class Recipes {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipes_seq_gen")
    private long id;

    @Column(name = "name")
    private String name;

  @ElementCollection
    @CollectionTable(name = "recipe_ingredients", schema = "receipts",
            joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredient")
    private List<String> ingredients;

    @ElementCollection
    @CollectionTable(name = "recipe_instructions", schema = "receipts",
            joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "instruction")
    private List<String> instructions;


    @Column(name = "estimated_time")
    private int estimatedTime;

    @Column(name = "servings")
    private int servings;

    @OneToOne
    @JoinColumn(name = "nutrition_id", referencedColumnName = "id")
    private Nutrition nutrition;

}

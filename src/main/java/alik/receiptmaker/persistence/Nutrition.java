package alik.receiptmaker.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "nutrition", schema = "receipts")
@SequenceGenerator(name = "nutrition_seq_gen", sequenceName = "nutrition_seq", allocationSize = 1)
@Getter
@Setter
public class Nutrition {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nutrition_seq_gen")
    private long id;

    @Column(name = "calories")
    private int calories;

    @Column(name = "fat")
    private int fat;

    @Column(name = "protein")
    private int protein;

    @Column(name = "carbs")
    private int carbs;

}

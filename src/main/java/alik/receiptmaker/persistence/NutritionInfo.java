package alik.receiptmaker.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "nutrition_info", schema = "receipts")
@SequenceGenerator(name = "nutrition_info_seq_gen", sequenceName = "nutrition_info_seq", allocationSize = 1)
@Getter
@Setter
public class NutritionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nutrition_info_seq_gen")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private double value;

    @Column(name = "unit")
    private String unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutrition_id")
    private Nutrition nutrition;
}

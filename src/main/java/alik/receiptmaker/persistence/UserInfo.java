package alik.receiptmaker.persistence;

import alik.receiptmaker.user.persistence.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "user_info", schema = "receipts")
@SequenceGenerator(name = "user_info_seq_gen", sequenceName = "user_info_seq", allocationSize = 1)
@Getter
@Setter
public class UserInfo {

    @Id
    @GeneratedValue(generator = "user_info_seq_gen", strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "favorite_cuisine")
    private String favoriteCuisine;

    @ElementCollection
    @CollectionTable(name = "disliked_ingredients", schema = "receipts",
            joinColumns = @JoinColumn(name = "user_info_id"))
    @Column(name = "disliked_ingredients")
    private Set<String> dislikedIngredients;

    @ElementCollection
    @CollectionTable(name = "allergies", schema = "receipts",
            joinColumns = @JoinColumn(name = "user_info_id"))
    @Column(name = "allergies")
    private Set<String> allergies;

    @Column(name = "vegetarian")
    private boolean vegetarian;

    @Column(name = "vegan")
    private boolean vegan;

    @Column(name = "gluten_free")
    private boolean glutenFree;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser appUser;

}

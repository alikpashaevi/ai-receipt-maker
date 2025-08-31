package alik.receiptmaker.user.persistence;

import alik.receiptmaker.constants.AuthProvider;
import alik.receiptmaker.persistence.Recipes;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "app_user", schema = "receipts")
@SequenceGenerator(name = "app_user_seq_gen", sequenceName = "user_seq", allocationSize = 1)
@Getter
@Setter
public class AppUser {

    @Id
    @GeneratedValue(generator = "app_user_seq_gen", strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private String username;


    @Column(name = "email")
    private String email;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private AuthProvider provider;

    @Column
    private String providerId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private Set<Recipes> favorites = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_history",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    @JsonManagedReference
    private List<Recipes> history = new ArrayList<>();


    @Override
    public String toString() {
        return "AppUser{id=" + id + ", username='" + username + "'}";
    }
}
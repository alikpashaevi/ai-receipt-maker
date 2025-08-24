package alik.receiptmaker.persistence;

import alik.receiptmaker.user.persistence.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_history", schema = "receipts")
@SequenceGenerator(name = "history_seq_gen", sequenceName = "history_seq", allocationSize = 1)
@Getter
@Setter
public class UserHistory {

    @Id
    @GeneratedValue(generator = "history_seq_gen", strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipes recipe;

    @Column(name = "viewed_at")
    private LocalDateTime viewedAt;

}

package alik.receiptmaker.user.persistence;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "role", schema = "receipts")
@Getter
@Setter
public class Role {

    @Id
    private long id;

    @Column
    private String name;
}
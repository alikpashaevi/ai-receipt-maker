package alik.receiptmaker.user.model;

import alik.receiptmaker.user.persistence.Role;
import lombok.Data;

import java.util.Set;

@Data
public class AppUserDTO {
    private long id;
    private String username;
    private String email;
    private Set<Role> roles;
}

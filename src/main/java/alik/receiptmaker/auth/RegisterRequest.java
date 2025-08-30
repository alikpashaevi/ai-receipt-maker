package alik.receiptmaker.auth;

import alik.receiptmaker.constants.AuthProvider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import liquibase.change.DatabaseChangeNote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank
    @Size(min = 2, max = 30)
    private String username;
    @Email
    @NotBlank
    @Size(min = 2, max = 50)
    private String email;
    @NotBlank
    @Size(min = 8)
    private String password;
    private AuthProvider provider;
    private Set<Long> roleIds;
}

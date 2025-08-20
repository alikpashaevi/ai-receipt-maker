package alik.receiptmaker.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import liquibase.change.DatabaseChangeNote;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    @Size(min = 2, max = 30)
    private String username;
    @NotBlank
    @Size(min = 8)
    private String password;
    private Set<Long> roleIds;
}

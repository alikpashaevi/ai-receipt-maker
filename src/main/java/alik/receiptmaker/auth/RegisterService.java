package alik.receiptmaker.auth;

import alik.receiptmaker.user.RoleService;
import alik.receiptmaker.user.persistence.AppUser;
import alik.receiptmaker.user.persistence.AppUserRepo;
import alik.receiptmaker.user.persistence.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final AppUserRepo appUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public void register(RegisterRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRoles(user.getRoles().stream()
                .map(roleId -> roleService.getRole(1L)).collect(Collectors.toSet())
        );

        if (appUserRepo.existsByUsername(user.getUsername())) {
            throw new RuntimeException("User with this username already exists");
        }

        appUserRepo.save(user);
    }

}

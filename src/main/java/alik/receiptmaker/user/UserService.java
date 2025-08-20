package alik.receiptmaker.user;

import alik.receiptmaker.user.model.UserRequest;
import alik.receiptmaker.user.persistence.AppUser;
import alik.receiptmaker.user.persistence.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepo appUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public void createUser(UserRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRoles(request.getRoleIds().stream()
                .map(roleService::getRole)
                .collect(Collectors.toSet())
        );

        if (appUserRepo.existsByUsername(user.getUsername())) {
            throw new RuntimeException("User with this username already exists");
        }

        appUserRepo.save(user);
    }

    public AppUser getUser(String username) {
        return appUserRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


}

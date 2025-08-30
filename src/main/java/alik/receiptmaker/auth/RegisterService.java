package alik.receiptmaker.auth;

import alik.receiptmaker.constants.AuthProvider;
import alik.receiptmaker.error.UserExistsException;
import alik.receiptmaker.service.EmailVerificationService;
import alik.receiptmaker.user.RoleService;
import alik.receiptmaker.user.persistence.AppUser;
import alik.receiptmaker.user.persistence.AppUserRepo;
import alik.receiptmaker.user.persistence.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
        if(appUserRepo.existsByEmail(request.getEmail())) {
            throw new UserExistsException("User with this email already exists");
        }
        user.setEmail(request.getEmail());
        user.setProvider(AuthProvider.LOCAL);
        user.setRoles(roleService.getRole(2L));

        if (appUserRepo.existsByUsername(user.getUsername())) {
            throw new UserExistsException("User with this username already exists");
        }

        appUserRepo.save(user);
    }

}

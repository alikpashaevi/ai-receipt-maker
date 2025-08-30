package alik.receiptmaker.auth;

import alik.receiptmaker.error.UsernameExistsException;
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
    private final EmailVerificationService emailVerificationService;

    public void register(RegisterRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        user.setRoles(roleService.getRole(2L));

        System.out.println(user.getRoles());

        if (appUserRepo.existsByUsername(user.getUsername())) {
            throw new UsernameExistsException("User with this username already exists");
        }

        appUserRepo.save(user);
    }

}

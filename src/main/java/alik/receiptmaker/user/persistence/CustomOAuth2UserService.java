package alik.receiptmaker.user.persistence;


import alik.receiptmaker.user.persistence.AppUser;
import alik.receiptmaker.user.persistence.AppUserRepo;
import alik.receiptmaker.user.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AppUserRepo appUserRepo;
    private final RoleService roleService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        AppUser user = appUserRepo.findByUsername(email).orElseGet(() -> {
            AppUser newUser = new AppUser();
            newUser.setUsername(email);
            newUser.setRoles(roleService.getRole(2L)); // assign USER role
            return appUserRepo.save(newUser);
        });
        return oAuth2User;
    }
}

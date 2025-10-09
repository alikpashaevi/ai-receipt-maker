package alik.receiptmaker.user;

import alik.receiptmaker.constants.AuthProvider;
import alik.receiptmaker.user.persistence.AppUser;
import alik.receiptmaker.user.persistence.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AppUserRepo userRepository;
    private final RoleService roleService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        AuthProvider provider = AuthProvider.valueOf(registrationId.toUpperCase());
        String providerId = (String) attributes.get("sub");
        String name = (String) attributes.get("name");
        String email = (String) attributes.get("email");

        AppUser user = userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> {
                    return userRepository.findByEmail(email).orElseGet(() -> {
                        AppUser newUser = new AppUser();
                        newUser.setUsername(name+providerId);
                        newUser.setEmail(email);
                        newUser.setProvider(provider);
                        newUser.setProviderId(providerId);
                        newUser.setFirstLogin(true);
                        newUser.setRoles(roleService.getRole(2L));
                        return userRepository.save(newUser);
                    });
                });

        // If user was found by email but NOT by provider, it means they are linking accounts
        if (user.getProvider() == null || !user.getProvider().equals(provider)) {
            user.setProvider(provider);
            user.setProviderId(providerId);
            user = userRepository.save(user);
        }

        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new CustomOAuth2User(user, attributes, authorities);
    }
}
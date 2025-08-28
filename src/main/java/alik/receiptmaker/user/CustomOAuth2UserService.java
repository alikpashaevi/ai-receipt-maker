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

    // Make these fields final so Lombok injects them
    private final AppUserRepo userRepository;
    private final RoleService roleService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "google"
        AuthProvider provider = AuthProvider.valueOf(registrationId.toUpperCase()); // Convert to enum safely
        String providerId = (String) attributes.get("sub");
        String email = (String) attributes.get("email");

        AppUser user = userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> {
                    // User not found by provider id, try to find by email
                    return userRepository.findByEmail(email).orElseGet(() -> {
                        // Brand new user
                        AppUser newUser = new AppUser();
                        newUser.setUsername(email); // Use email as username
                        newUser.setEmail(email);
                        newUser.setProvider(provider);
                        newUser.setProviderId(providerId);
                        newUser.setRoles(roleService.getRole(2L)); // e.g., 2L = ROLE_USER
                        return userRepository.save(newUser);
                    });
                });

        // If user was found by email but NOT by provider, it means they are linking accounts
        if (user.getProvider() == null || !user.getProvider().equals(provider)) {
            user.setProvider(provider);
            user.setProviderId(providerId);
            user = userRepository.save(user);
        }

        // Create a collection of authorities from the user's roles
        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())) // Assuming role has a getName() method that returns "ROLE_USER"
                .collect(Collectors.toList());

        // Return a custom OAuth2User object that includes the AppUser ID!
        return new CustomOAuth2User(user, attributes, authorities);
    }
}
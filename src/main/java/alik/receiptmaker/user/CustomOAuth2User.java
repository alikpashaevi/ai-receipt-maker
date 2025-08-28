package alik.receiptmaker.user;

import alik.receiptmaker.user.persistence.AppUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    // CRITICAL: Getter for your AppUser entity
    @Getter
    private final AppUser appUser;
    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomOAuth2User(AppUser appUser, Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities) {
        this.appUser = appUser;
        this.attributes = attributes;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.appUser.getUsername(); // Or this.appUser.getEmail();
    }

    // Convenience method to get the database ID for JWT generation
    public Long getId() {
        return this.appUser.getId();
    }

    public String getEmail() {
        return this.appUser.getEmail();
    }
}
package alik.receiptmaker.user;

import alik.receiptmaker.auth.JwtService;
import alik.receiptmaker.auth.LoginResponse;
import alik.receiptmaker.components.GetRecipeMethods;
import alik.receiptmaker.components.GetUsername;
import alik.receiptmaker.error.NotFoundException;
import alik.receiptmaker.error.UserExistsException;
import alik.receiptmaker.model.UserFavoritesDTO;
import alik.receiptmaker.persistence.Recipes;
import alik.receiptmaker.user.model.AppUserDTO;
import alik.receiptmaker.user.persistence.AppUser;
import alik.receiptmaker.user.persistence.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepo appUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final GetRecipeMethods getRecipeMethods;
    private final JwtService jwtService;

    private UserFavoritesDTO mapUserFavoritesDTO(Recipes recipe) {
        if (recipe == null) {
            return null;
        }

        return new UserFavoritesDTO(
                recipe.getId(),
                recipe.getName()
        );
    }

    public Page<AppUser> getUsers(int page, int pageSize) {
        return appUserRepo.findAll(PageRequest.of(page, pageSize));
    }

    public AppUser getUserForLogin(String username) {
        return appUserRepo.findByUsername(username)
                .orElse(null);
    }

    public AppUser getUser(String username) {
        return appUserRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Set<UserFavoritesDTO> getUserFavorites() {
        String username = GetUsername.getUsernameFromToken();
        AppUser user = getUser(username);
        return user.getFavorites().stream().map(this::mapUserFavoritesDTO).collect(Collectors.toSet());
    }

//    public Recipes getUserFavoriteById(Long id) {
//        String username = GetUsername.getUsernameFromToken();
//        System.out.println("Username from token: " + username);
//        AppUser user = getUser(username);
//        return user.getFavorites().stream()
//                .filter(recipe -> recipe.getId() == id)
//                .findFirst()
//                .orElseThrow(() -> new NotFoundException("Favorite recipe not found"));
//    }

    public void addToFavorites(Long receiptId) {
        String username = GetUsername.getUsernameFromToken();
        AppUser user = getUser(username);
        Recipes recipeToAdd = getRecipeMethods.getRecipeById(receiptId);
        if (recipeToAdd != null) {
            user.getFavorites().add(recipeToAdd);
        }
        appUserRepo.save(user);
    }

    public void removeFromFavorites(Long  receiptId) {
        String username = GetUsername.getUsernameFromToken();
        AppUser user = getUser(username);
        Recipes recipeToRemove = getRecipeMethods.getRecipeById(receiptId);
        if (recipeToRemove != null) {
            user.getFavorites().remove(recipeToRemove);
        }
        appUserRepo.save(user);
    }

    public void changePassword(String oldPassword, String newPassword) {
        String username = GetUsername.getUsernameFromToken();
        AppUser user = getUser(username);

        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            appUserRepo.save(user);
        } else {
            throw new RuntimeException("Old password is incorrect");
        }
    }

    public LoginResponse changeUsername(String newUsername) {
        String username = GetUsername.getUsernameFromToken();
        AppUser user = getUser(username);

        if (appUserRepo.existsByUsername(newUsername)) {
            throw new UserExistsException("User with this username already exists");
        }

        user.setUsername(newUsername);
        appUserRepo.save(user);

        return jwtService.generateLoginResponse(user);
    }

    public AppUserDTO getProfile() {
        String username = GetUsername.getUsernameFromToken();
        AppUser user = getUser(username);

        AppUserDTO userDTO = new AppUserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }

}

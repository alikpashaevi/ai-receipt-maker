package alik.receiptmaker.user;

import alik.receiptmaker.components.GetUsername;
import alik.receiptmaker.persistence.Recipes;
import alik.receiptmaker.service.RecipeService;
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
    private final RecipeService recipeService;
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

    public void addToFavorites(Long receiptId) {
        String username = GetUsername.getUsernameFromToken();
        System.out.println("Username from token: " + username);
        AppUser user = getUser(username);
        Recipes recipeToAdd = recipeService.getRecipeById(receiptId);
        if (recipeToAdd != null) {
            user.getFavorites().add(recipeToAdd);
        }
        appUserRepo.save(user);
    }

    public void removeFromFavorites(Long  receiptId) {
        String username = GetUsername.getUsernameFromToken();
        System.out.println("Username from token: " + username);
        AppUser user = getUser(username);
        Recipes recipeToRemove = recipeService.getRecipeById(receiptId);
        if (recipeToRemove != null) {
            user.getFavorites().remove(recipeToRemove);
        }
        appUserRepo.save(user);
    }

    public void addToHistory(Long receiptId) {
        String username = GetUsername.getUsernameFromToken();
        System.out.println("Username from token: " + username);
        AppUser user = getUser(username);
        Recipes recipeToAdd = recipeService.getRecipeById(receiptId);
        if (recipeToAdd != null) {
            user.getHistory().add(recipeToAdd);
        }
        appUserRepo.save(user);
    }


}

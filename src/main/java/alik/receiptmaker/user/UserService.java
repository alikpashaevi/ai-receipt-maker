package alik.receiptmaker.user;

import alik.receiptmaker.components.GetRecipeMethods;
import alik.receiptmaker.components.GetUsername;
import alik.receiptmaker.model.RecipeResponse;
import alik.receiptmaker.persistence.Recipes;
import alik.receiptmaker.user.persistence.AppUser;
import alik.receiptmaker.user.persistence.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepo appUserRepo;
    private final PasswordEncoder passwordEncoder;
//    private final RecipeService recipeService;
    private final GetRecipeMethods getRecipeMethods;
    private final RoleService roleService;

//    public void createUser(UserRequest request) {
//        AppUser user = new AppUser();
//        user.setUsername(request.getUsername());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//
//        user.setRoles(roleService.getRole(request.getRoleIds()));
//
//        if (appUserRepo.existsByUsername(user.getUsername())) {
//            throw new RuntimeException("User with this username already exists");
//        }
//
//        appUserRepo.save(user);
//    }

    public AppUser getUser(String username) {
        return appUserRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void addToFavorites(Long receiptId) {
        String username = GetUsername.getUsernameFromToken();
        System.out.println("Username from token: " + username);
        AppUser user = getUser(username);
        Recipes recipeToAdd = getRecipeMethods.getRecipeById(receiptId);
        if (recipeToAdd != null) {
            user.getFavorites().add(recipeToAdd);
        }
        appUserRepo.save(user);
    }

    public void removeFromFavorites(Long  receiptId) {
        String username = GetUsername.getUsernameFromToken();
        System.out.println("Username from token: " + username);
        AppUser user = getUser(username);
        Recipes recipeToRemove = getRecipeMethods.getRecipeById(receiptId);
        if (recipeToRemove != null) {
            user.getFavorites().remove(recipeToRemove);
        }
        appUserRepo.save(user);
    }

//    public void addToHistory(RecipeResponse recipe) {
//        String username = GetUsername.getUsernameFromToken();
//        String dishName = recipe.getDish_name();
//
//        if (getRecipeMethods.existsByName(dishName)) {
//            Recipes recipeToAdd = getRecipeMethods.getRecipeByName(dishName);
//            AppUser user = getUser(username);
//
//            if (user.getHistory().contains(recipeToAdd)) {
//                user.getHistory().remove(recipeToAdd);
//            }
//
//            user.getHistory().add(recipeToAdd);
//            appUserRepo.save(user);
//        } else {
//            System.out.println("Recipe with name " + dishName + " does not exist in the database.");
//        }
//    }


}

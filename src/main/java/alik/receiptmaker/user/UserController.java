package alik.receiptmaker.user;

import alik.receiptmaker.auth.LoginResponse;
import alik.receiptmaker.model.UserFavoritesDTO;
import alik.receiptmaker.persistence.Recipes;
import alik.receiptmaker.user.model.AppUserDTO;
import alik.receiptmaker.user.persistence.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static alik.receiptmaker.constants.AuthorizationConstants.ADMIN;
import static alik.receiptmaker.constants.AuthorizationConstants.USER_OR_ADMIN;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize(ADMIN)
    @GetMapping("/users")
    public Page<AppUser> getUsers(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int pageSize) {
        return userService.getUsers(page, pageSize);
    }

    @PreAuthorize(USER_OR_ADMIN)
    @GetMapping("/favorites")
    public ResponseEntity<Set<UserFavoritesDTO>> getUserFavorites() {
        Set<UserFavoritesDTO> favorites = userService.getUserFavorites();
        return ResponseEntity.ok(favorites);
    }

//    @PreAuthorize(USER_OR_ADMIN)
//    @GetMapping("/favorites/{id}")
//    public ResponseEntity<Recipes> getFavoriteById(@PathVariable Long id) {
//        Recipes favorite = userService.getUserFavoriteById(id);
//        return ResponseEntity.ok(favorite);
//    }

    @PreAuthorize(USER_OR_ADMIN)
    @PutMapping("/password")
    public void changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        userService.changePassword(oldPassword, newPassword);
    }

    @PreAuthorize(USER_OR_ADMIN)
    @PutMapping("/username")
    public LoginResponse changeUsername(@RequestParam String newUsername) {
        return userService.changeUsername(newUsername);
    }



    @PreAuthorize(USER_OR_ADMIN)
    @PostMapping("/favorites/{id}")
    public ResponseEntity<String> addToFavorites(@PathVariable Long id) {
        userService.addToFavorites(id);
        return ResponseEntity.ok("Recipe added to favorites");
    }

    @PreAuthorize(USER_OR_ADMIN)
    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<String> removeFromFavorites(@PathVariable Long id) {
        userService.removeFromFavorites(id);
        return ResponseEntity.ok("Recipe removed from favorites");
    }

    @PreAuthorize(USER_OR_ADMIN)
    @GetMapping("/profile")
    public AppUserDTO getUserProfile() {
        return userService.getProfile();
    }


}

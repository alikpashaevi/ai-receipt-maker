package alik.receiptmaker.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static alik.receiptmaker.constants.AuthorizationConstants.ADMIN;
import static alik.receiptmaker.constants.AuthorizationConstants.USER_OR_ADMIN;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize(ADMIN)
    @GetMapping("/users")
    public Object getUsers(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int pageSize) {
        return userService.getUsers(page, pageSize);
    }

    @PreAuthorize(USER_OR_ADMIN)
    @PutMapping("/password")
    public void changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        userService.changePassword(oldPassword, newPassword);
    }

    @PreAuthorize(USER_OR_ADMIN)
    @PutMapping("/username")
    public void changeUsername(@RequestParam String newUsername) {
        userService.changeUsername(newUsername);
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


}

package alik.receiptmaker.user;

import alik.receiptmaker.user.model.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static alik.receiptmaker.constants.AuthorizationConstants.ADMIN;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@PreAuthorize(ADMIN)
public class UserController {

    private final UserService userService;

    @PostMapping
    public void createUser(@RequestBody @Valid UserRequest request) {
        userService.createUser(request);
    }

    @PostMapping("/favorites/{id}")
    public void addToFavorites(@PathVariable Long id) {
        userService.addToFavorites(id);
    }

    @DeleteMapping("/favorites/{id}")
    public void removeFromFavorites(@PathVariable Long id) {
        userService.removeFromFavorites(id);
    }

}

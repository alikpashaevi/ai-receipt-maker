package alik.receiptmaker.user;

import alik.receiptmaker.user.model.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static alik.receiptmaker.constants.AuthorizationConstants.ADMIN;
import static alik.receiptmaker.constants.AuthorizationConstants.USER_OR_ADMIN;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    @PreAuthorize(ADMIN)
//    @PostMapping
//    public void createUser(@RequestBody @Valid UserRequest request) {
//        userService.createUser(request);
//    }

    @PreAuthorize(USER_OR_ADMIN)
    @PostMapping("/favorites/{id}")
    public void addToFavorites(@PathVariable Long id) {
        userService.addToFavorites(id);
    }

    @PreAuthorize(USER_OR_ADMIN)
    @DeleteMapping("/favorites/{id}")
    public void removeFromFavorites(@PathVariable Long id) {
        userService.removeFromFavorites(id);
    }

}

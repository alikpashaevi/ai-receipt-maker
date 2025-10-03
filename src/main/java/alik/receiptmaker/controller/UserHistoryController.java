package alik.receiptmaker.controller;

import alik.receiptmaker.model.UserHistoryDTO;
import alik.receiptmaker.persistence.UserHistory;
import alik.receiptmaker.service.UserHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static alik.receiptmaker.constants.AuthorizationConstants.USER_OR_ADMIN;

@RestController
@RequestMapping("/user-history")
@RequiredArgsConstructor
@PreAuthorize(USER_OR_ADMIN)
public class UserHistoryController {

    private final UserHistoryService userHistoryService;

    @GetMapping
    public List<UserHistoryDTO> getUserHistory() {
        return userHistoryService.getUserHistory();
    }

    @PostMapping("/{recipeId}")
    public void addToHistory(@PathVariable long recipeId) {
        userHistoryService.addToHistory(recipeId);
    }

}

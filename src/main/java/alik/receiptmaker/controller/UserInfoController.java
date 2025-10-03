package alik.receiptmaker.controller;

import alik.receiptmaker.model.UserInfoRequest;
import alik.receiptmaker.persistence.UserInfo;
import alik.receiptmaker.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static alik.receiptmaker.constants.AuthorizationConstants.USER_OR_ADMIN;

@RestController
@RequestMapping("/user-info")
@RequiredArgsConstructor
@PreAuthorize(USER_OR_ADMIN)
public class UserInfoController {

    private final UserInfoService userInfoService;

    @GetMapping
    public UserInfo getUserInfo() {
        UserInfo userInfo = userInfoService.getUserInfo();
        return userInfo;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody UserInfoRequest userInfoRequest) {
        userInfoService.addUserInfo(userInfoRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody UserInfoRequest userInfoRequest) {
        userInfoService.updateUserInfo(userInfoRequest);
        return ResponseEntity.ok().build();
    }

}

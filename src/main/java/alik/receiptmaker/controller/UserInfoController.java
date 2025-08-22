package alik.receiptmaker.controller;

import alik.receiptmaker.model.UserInfoRequest;
import alik.receiptmaker.persistence.UserInfo;
import alik.receiptmaker.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static alik.receiptmaker.constants.AuthorizationConstants.USER_OR_ADMIN;

@RestController
@RequestMapping("/user-info")
@RequiredArgsConstructor
@PreAuthorize(USER_OR_ADMIN)
public class UserInfoController {

    private final UserInfoService userInfoService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody UserInfoRequest userInfoRequest) {
        userInfoService.addUserInfo(userInfoRequest);
        return ResponseEntity.ok().build();
    }

}

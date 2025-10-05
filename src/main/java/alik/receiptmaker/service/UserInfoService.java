package alik.receiptmaker.service;

import alik.receiptmaker.components.GetUsername;
import alik.receiptmaker.model.UserInfoRequest;
import alik.receiptmaker.persistence.UserInfo;
import alik.receiptmaker.persistence.UserInfoRepo;
import alik.receiptmaker.user.UserService;
import alik.receiptmaker.user.persistence.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepo userInfoRepo;
    private final UserService userService;

    public UserInfo getUserInfoByUserId(long id) {
        return userInfoRepo.findByUserId(id);
    }

    public UserInfo getUserInfo() {
        String username = GetUsername.getUsernameFromToken();
        AppUser user = userService.getUser(username);
        return userInfoRepo.findByUserId(user.getId());
    }

    public void addUserInfo(UserInfoRequest userInfoRequest) {
        String username = GetUsername.getUsernameFromToken();
        AppUser user = userService.getUser(username);

        UserInfo userInfo = new UserInfo();
        userInfo.setFavoriteCuisine(userInfoRequest.getFavoriteCuisine());
        userInfo.setDislikedIngredients(userInfoRequest.getDislikedIngredients());
        userInfo.setAllergies(userInfoRequest.getAllergies());
        userInfo.setVegetarian(userInfoRequest.isVegetarian());
        userInfo.setVegan(userInfoRequest.isVegan());
        userInfo.setGlutenFree(userInfoRequest.isGlutenFree());
        userInfo.setAppUser(user);
        if (user.getFirstLogin()) {
            user.setFirstLogin(false);
        }

        userInfoRepo.save(userInfo);
    }

    public void updateUserInfo(UserInfoRequest userInfoRequest) {
        String username = GetUsername.getUsernameFromToken();
        AppUser user = userService.getUser(username);

        UserInfo existingInfo = userInfoRepo.findByUserId(user.getId());
        if (existingInfo != null) {
            existingInfo.setFavoriteCuisine(userInfoRequest.getFavoriteCuisine());
            existingInfo.setDislikedIngredients(userInfoRequest.getDislikedIngredients());
            existingInfo.setAllergies(userInfoRequest.getAllergies());
            existingInfo.setVegetarian(userInfoRequest.isVegetarian());
            existingInfo.setVegan(userInfoRequest.isVegan());
            existingInfo.setGlutenFree(userInfoRequest.isGlutenFree());

            userInfoRepo.save(existingInfo);
        } else {
            addUserInfo(userInfoRequest);
        }
    }

}

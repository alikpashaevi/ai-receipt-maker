package alik.receiptmaker.service;

import alik.receiptmaker.model.UserInfoRequest;
import alik.receiptmaker.persistence.UserInfo;
import alik.receiptmaker.persistence.UserInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepo userInfoRepo;

    public UserInfo getUserInfoByUserId(long id) {
        return userInfoRepo.findByUserId(id);
    }

    public void addUserInfo(UserInfoRequest userInfoRequest) {
        UserInfo userInfo = new UserInfo();
        userInfo.setFavoriteCuisine(userInfoRequest.getFavoriteCuisine());
        userInfo.setDislikedIngredients(userInfoRequest.getDislikedIngredients());
        userInfo.setAllergies(userInfoRequest.getAllergies());
        userInfo.setVegetarian(userInfoRequest.isVegetarian());
        userInfo.setVegan(userInfoRequest.isVegan());
        userInfo.setGlutenFree(userInfoRequest.isGlutenFree());

        userInfoRepo.save(userInfo);
    }

}

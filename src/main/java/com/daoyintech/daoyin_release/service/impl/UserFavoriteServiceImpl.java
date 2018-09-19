package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.user.UserFavorite;
import com.daoyintech.daoyin_release.repository.user.UserFavoriteRepository;
import com.daoyintech.daoyin_release.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFavoriteServiceImpl implements UserFavoriteService{

    @Autowired
    private UserFavoriteRepository favRepository;

    @Override
    public Boolean isFavProduct(Long userId, Long productId) {
        UserFavorite userFavorite = favRepository.findByUserIdAndProductId(userId, productId);
        if (userFavorite != null) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized void isFavChange(Long userId, Long productId){
        UserFavorite userFavorite = favRepository.findByUserIdAndProductId(userId, productId);
        if (userFavorite != null) {
            favRepository.delete(userFavorite);
        } else {
            UserFavorite favorite = new UserFavorite();
            favorite.setProductId(productId);
            favorite.setUserId(userId);
            favRepository.save(favorite);
        }
    }

}

package com.daoyintech.daoyin_release.service.user;

public interface UserFavoriteService {

    Boolean isFavProduct(Long userId, Long productId);

    void isFavChange(Long userId, Long productId);

}

package com.daoyintech.daoyin_release.repository.user;

import com.daoyintech.daoyin_release.entity.user.UserFavorite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavoriteRepository extends CrudRepository<UserFavorite,Long> {

    UserFavorite findByUserIdAndProductId(Long userId, Long productId);

}

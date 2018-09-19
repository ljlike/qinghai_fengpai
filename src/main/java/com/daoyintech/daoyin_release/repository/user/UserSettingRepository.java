package com.daoyintech.daoyin_release.repository.user;

import com.daoyintech.daoyin_release.entity.user.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/8/30.
 */
@Repository
public interface UserSettingRepository extends JpaRepository<UserSetting,Long> {

    UserSetting findByUserIdAndUserDefaultAddressId(Long userId, Long addressId);

    UserSetting findByUserId(Long id);
}

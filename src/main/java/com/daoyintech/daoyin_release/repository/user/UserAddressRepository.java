package com.daoyintech.daoyin_release.repository.user;

import com.daoyintech.daoyin_release.entity.user.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */
@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress,Long> {
    List<UserAddress> findByUserId(Long userId);
}

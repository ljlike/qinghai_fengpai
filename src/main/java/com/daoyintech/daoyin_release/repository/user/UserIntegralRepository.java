package com.daoyintech.daoyin_release.repository.user;

import com.daoyintech.daoyin_release.entity.user.integral.UserIntegral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserIntegralRepository extends JpaRepository<UserIntegral,Long> {

    /**
     * @param userId
     * @return
     */
    UserIntegral findByUserId(Long userId);

}

package com.daoyintech.daoyin_release.repository.user;

import com.daoyintech.daoyin_release.entity.user.UserProfitApplet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfitAppletRepository extends JpaRepository<UserProfitApplet,Long>{

    UserProfitApplet findByFromUserId(Long fromUserId);

    List<UserProfitApplet> findByToUserId(Long toUserId);

    Integer countByToUserId(Long userId);

    List<UserProfitApplet> findByToUserIdAndIsProfit(Long id, Boolean isProfit);

}

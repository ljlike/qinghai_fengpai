package com.daoyintech.daoyin_release.repository.user;

import com.daoyintech.daoyin_release.entity.user.UserProfit;
import com.daoyintech.daoyin_release.enums.user.UserProfitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfitRepository extends JpaRepository<UserProfit,Long> {

    List<UserProfit> findByFromUserId(Long fromUserId);

    List<UserProfit> findByToUserIdAndUserProfitType(Long toUserId, UserProfitType userProfitType);

    UserProfit findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    List<UserProfit> findByToUserId(Long fromUserId);

}

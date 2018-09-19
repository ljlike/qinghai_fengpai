package com.daoyintech.daoyin_release.repository.user;

import com.daoyintech.daoyin_release.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author pei on 2018/08/09
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    User findByUnionId(String unionId);

    User findByOpenId(String openId);

    User findByAppletOpenId(String appletOpenId);

    List<User> findByIsAgentTrueAndIdIn(Long [] userIds);
}






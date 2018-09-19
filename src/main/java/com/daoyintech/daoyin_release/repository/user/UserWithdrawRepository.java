package com.daoyintech.daoyin_release.repository.user;

import com.daoyintech.daoyin_release.entity.agent.UserWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author pei on 2018/08/13
 */
@Repository
public interface UserWithdrawRepository extends JpaRepository<UserWithdraw,Long>{

    List<UserWithdraw> findByUserId(Long id);


    List<UserWithdraw> findAllByCreatedAtAfter(Date zero);


    List<UserWithdraw> findAllByUserIdAndCreatedAtAfter(Long userId, Date zero);


}

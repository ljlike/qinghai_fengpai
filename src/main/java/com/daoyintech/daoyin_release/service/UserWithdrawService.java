package com.daoyintech.daoyin_release.service;

import com.daoyintech.daoyin_release.entity.agent.UserWithdraw;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.response.ResultResponse;

import java.util.List;

/**
 * @author pei on 2018/08/11
 */
public interface UserWithdrawService {

    ResultResponse findAllDetailAndBalance(User user);


    List<UserWithdraw> findByCreateAt(Long id);


    UserWithdraw save(UserWithdraw withdraw);


    boolean withdraw(UserWithdraw withdraw, User user);


}

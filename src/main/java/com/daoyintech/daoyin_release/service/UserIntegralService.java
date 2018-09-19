package com.daoyintech.daoyin_release.service;

import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.entity.user.integral.UserIntegral;
import com.daoyintech.daoyin_release.response.ResultResponse;

import java.math.BigDecimal;

/**
 * @author pei on 2018/08/09
 */
public interface UserIntegralService {


    UserIntegral findByUserId(Long id);


    UserIntegral findByUser(User user);


    UserIntegral save(UserIntegral userIntegral);


    UserIntegral updateUserPoint(BigDecimal bigDecimal, User joinerUser);


    void userIntegralDraw(Order saveOrder);


    ResultResponse findAllMyHelp(User user);


}

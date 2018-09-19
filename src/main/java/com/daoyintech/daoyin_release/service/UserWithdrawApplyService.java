package com.daoyintech.daoyin_release.service;

import com.daoyintech.daoyin_release.entity.user.User;

import java.math.BigDecimal; /**
 * @author pei on 2018/08/22
 */
public interface UserWithdrawApplyService {

    void createWithdraw(User user, BigDecimal point);


}

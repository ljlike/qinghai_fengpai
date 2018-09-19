package com.daoyintech.daoyin_release.service;

import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.entity.user.integral.UserIntegralDetail;
import com.daoyintech.daoyin_release.enums.user.UserIntegralFromType;
import com.daoyintech.daoyin_release.response.user.UserIntegralDetailResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author pei on 2018/08/13
 */
public interface UserIntegralDetailService {

    List<UserIntegralDetail> findByUserIdAndType(User user, int page, int type);


    List<UserIntegralDetailResponse> transformUserIntegralDetailModel(List<UserIntegralDetail> userIntegralDetails);


    UserIntegralDetail buildUserIntegralDetail(Order order, User user, BigDecimal usedIntegral, UserIntegralFromType fromType);


    UserIntegralDetail save(UserIntegralDetail detail);


    List<UserIntegralDetail> findByUserIdToNewest(User user);

}

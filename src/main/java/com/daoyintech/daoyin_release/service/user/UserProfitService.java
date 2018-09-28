package com.daoyintech.daoyin_release.service.user;

import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.enums.user.UserProfitType;
import com.daoyintech.daoyin_release.entity.user.UserProfit;
import com.daoyintech.daoyin_release.response.member.MemberResponse;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author pei on 2018/08/17
 */
public interface UserProfitService {


    void createProfitOrder(Order order);

    void createProfitUser(User user, User parent, UserProfitType userProfitType);

    void createProfitUserByGrandFather(User child, User parent,UserProfitType profitType);

    List<UserProfit> findByToUserIdAndUserProfitTypeExceptMyself(Long id, UserProfitType direct);

    List<UserProfit> directPartners(List<UserProfit> userProfitList);

    List<MemberResponse> getMembers(List<UserProfit> userProfits);


    List<UserProfit> directMembers(List<UserProfit> directProfits);


    BigDecimal calSalesAmount(User fromUser, UserProfit profit);


    BigDecimal calIntegral(User fromUser, UserProfit profit);


    List<UserProfit> findByToUserId(Long id);


    List<UserProfit> removeNotAgent(List<UserProfit> subUserProfitList);


    UserProfit findByToUserIdAndFromUserId(Long toUserId, Long fromUserId);

    void handleProfit(Order order);


}

package com.daoyintech.daoyin_release.service;

import com.daoyintech.daoyin_release.entity.user.UserProfitApplet;
import com.daoyintech.daoyin_release.enums.user.UserProfitType;
import com.daoyintech.daoyin_release.response.member.MemberResponse;

import java.util.List;

public interface UserProfitAppletService {
    UserProfitApplet findByFromUserId(Long fromUserId);

    List<UserProfitApplet> findByToUserId(Long toUserId);

    UserProfitApplet save(UserProfitApplet userProfitApplet);

    List<UserProfitApplet> findByToUserIdAndIsProfit(Long id, Boolean isProfit);

    List<MemberResponse> userProfitAppletToMemberResponse(List<UserProfitApplet> directProfits);


}

package com.daoyintech.daoyin_release.service.user;

import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.bank.BankCardInfoRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;


/**
 * @author pei on 2018/08/09
 */
public interface UserService{

    ResultResponse firstInitUserInfo(String unionId);

    ResultResponse againInitUserInfo(HashMap<String, Object> userInfoMap);

    User findByUnionId(String unionId);

    User findById(Long userId);

    User findIsAgent(Long userId);

    User UpdateOrCreateBankCardInfo(BankCardInfoRequest bankCardInfo, User user);


}

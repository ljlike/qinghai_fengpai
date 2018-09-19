package com.daoyintech.daoyin_release.service;

import com.daoyintech.daoyin_release.entity.card.UserCard;
import com.daoyintech.daoyin_release.entity.card.WxCard;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.response.card.UserCardResponse;
import me.chanjar.weixin.common.bean.WxCardApiSignature;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;

/**
 * @author pei on 2018/08/22
 */
public interface UserCardService {

    UserCard createUserCard(User user, WxCard wxCard);


    List<UserCardResponse> findUsercards(User user);


    WxCardApiSignature getH5Param(Long id) throws WxErrorException;


    UserCard createNewUserCard(User user, int type);


    List<UserCardResponse> findNewUsercards(User user);


    void userCardToNew();


    List<UserCard> findByStatus(int status);


}

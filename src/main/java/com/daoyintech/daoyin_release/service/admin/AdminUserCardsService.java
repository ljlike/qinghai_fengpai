package com.daoyintech.daoyin_release.service.admin;

import com.daoyintech.daoyin_release.response.card.UserCardResponse;

/**
 * @author pei on 2018/09/12
 */
public interface AdminUserCardsService {


    Boolean queryUserCardByCardNo(String cardCode);


    UserCardResponse getUserCardByCardNo(String cardCode);


}

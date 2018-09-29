package com.daoyintech.daoyin_release.service.user;

import com.daoyintech.daoyin_release.entity.card.UserCard;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.response.card.UserCardResponse;

import java.util.List;

/**
 * @author pei on 2018/08/22
 */
public interface UserCardService {

    UserCard createNewUserCard(User user, int type);

    List<UserCardResponse> findNewUserCards(User user);

    List<UserCard> findByStatus(int status);

}

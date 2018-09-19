package com.daoyintech.daoyin_release.repository.card;

import com.daoyintech.daoyin_release.entity.card.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author pei on 2018/7/11
 */
@Repository
public interface UserCardRepository extends JpaRepository<UserCard,Long> {

    List<UserCard> findByOpenIdAndCardIdAndStatus(String openId, String cardId, Integer status);

    UserCard findByUserCardCode(String code);

    UserCard findByUserCardCodeAndStatus(String code, Integer status);

    UserCard findByOpenIdAndCardIdAndUserCardCodeAndStatus(String openId, String cardId, String code, Integer status);

    List<UserCard> findByAppletOpenId(String openId);

    Boolean existsByUserCardCode(String cardNo);

    List<UserCard> findByOpenIdAndCardId(String openId, String cardId);

    List<UserCard> findByOpenIdOrAppletOpenId(String openId,String appletOpenId);

    List<UserCard> findByUserId(Long userId);

    List<UserCard> findByStatus(int status);

}

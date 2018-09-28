package com.daoyintech.daoyin_release.service.impl.admin;

import com.daoyintech.daoyin_release.entity.card.UserCard;
import com.daoyintech.daoyin_release.repository.card.UserCardRepository;
import com.daoyintech.daoyin_release.response.card.UserCardResponse;
import com.daoyintech.daoyin_release.service.admin.AdminUserCardsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pei on 2018/09/12
 */
@Service
public class AdminUserCardsServiceImpl implements AdminUserCardsService {

    @Autowired
    private UserCardRepository userCardRepository;

    @Override
    public Boolean queryUserCardByCardNo(String cardCode) {
        return userCardRepository.existsByUserCardCode(cardCode);
    }

    @Override
    public UserCardResponse getUserCardByCardNo(String cardCode) {
        UserCard userCard = userCardRepository.findByUserCardCode(cardCode);
        if (userCard == null){
            return null;
        }
        UserCardResponse userCardResponse = new UserCardResponse();
        BeanUtils.copyProperties(userCard, userCardResponse);
        return userCardResponse;
    }


}






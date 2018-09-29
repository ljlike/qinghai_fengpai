package com.daoyintech.daoyin_release.service.impl.user;

import com.daoyintech.daoyin_release.entity.card.UserCard;
import com.daoyintech.daoyin_release.entity.card.WxCard;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.repository.card.CardRepository;
import com.daoyintech.daoyin_release.repository.card.UserCardRepository;
import com.daoyintech.daoyin_release.response.card.UserCardResponse;
import com.daoyintech.daoyin_release.service.user.UserCardService;
import com.daoyintech.daoyin_release.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pei on 2018/08/22
 */
@Service
public class UserCardServiceImpl implements UserCardService {

    @Autowired
    private UserCardRepository userCardRepository;

    @Autowired
    private CardRepository cardRepository;

    /**
     * 新卡卷功能,线下活动券
     */
    @Override
    public UserCard createNewUserCard(User user, int type) {
        WxCard wxCard = cardRepository.findByTypeAndStatus(type, 0);
        UserCard userCard = new UserCard();
        userCard.setType(type);
        userCard.setStatus(2);//未使用
        userCard.setExpire(wxCard.getExpire());
        String strNo = String.valueOf(System.nanoTime());
        strNo = strNo.substring(strNo.length() - 4, strNo.length());
        String userCardCode = RandomUtil.generateOnlyNumber(4) + strNo + RandomUtil.toFixdLengthString(user.getId(), 4);
        userCard.setUserCardCode(userCardCode);
        userCard.setUserId(user.getId());
        if (!StringUtils.isEmpty(user.getOpenId())) {
            userCard.setOpenId(user.getOpenId());
        }
        if (!StringUtils.isEmpty(user.getAppletOpenId())) {
            userCard.setAppletOpenId(user.getAppletOpenId());
        }
        return userCardRepository.save(userCard);
    }

    @Override
    public List<UserCard> findByStatus(int status) {
        return userCardRepository.findByStatus(status);
    }

    @Override
    public List<UserCardResponse> findNewUserCards(User user) {
        List<UserCard> userCards = userCardRepository.findByUserId(user.getId());
        List<UserCardResponse> userCardResponseList = new ArrayList<>();
        if (userCards != null && userCards.size() != 0) {
            for (UserCard usercard : userCards) {
                if (usercard.getStatus() == 4) {
                    continue;
                }
                UserCardResponse userCardResponse = new UserCardResponse();
                userCardResponse.setId(usercard.getId());
                userCardResponse.setStatus(usercard.getStatus());
                userCardResponse.setUserCardCode(usercard.getUserCardCode());
                userCardResponse.setExpire(usercard.getExpire());
                userCardResponse.setType(usercard.getType());
                if (usercard.getType() == 1) {
                    userCardResponse.setTitle("解惑人生重塑机缘");
                } else {
                    userCardResponse.setTitle("让幸福和性福有技可施");
                }
                userCardResponseList.add(userCardResponse);
            }
        }
        userCardResponseList = userCardResponseList.stream().filter(c -> c.getExpire() > Calendar.getInstance().getTimeInMillis()).collect(Collectors.toList());
        return userCardResponseList;
    }

}

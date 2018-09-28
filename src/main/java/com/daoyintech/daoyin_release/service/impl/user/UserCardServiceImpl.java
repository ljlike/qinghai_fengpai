package com.daoyintech.daoyin_release.service.impl.user;

import com.daoyintech.daoyin_release.entity.card.UserCard;
import com.daoyintech.daoyin_release.entity.card.WxCard;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.repository.card.CardRepository;
import com.daoyintech.daoyin_release.repository.card.UserCardRepository;
import com.daoyintech.daoyin_release.repository.user.UserRepository;
import com.daoyintech.daoyin_release.response.card.UserCardResponse;
import com.daoyintech.daoyin_release.service.user.UserCardService;
import com.daoyintech.daoyin_release.utils.DateUtils;
import com.daoyintech.daoyin_release.utils.RandomUtil;
import me.chanjar.weixin.common.bean.WxCardApiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author pei on 2018/08/22
 */
@Service
public class UserCardServiceImpl implements UserCardService {

    @Autowired
    private UserCardRepository userCardRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private UserRepository userRepository;


    /**
     * 新卡卷功能,线下活动券
     * */
    @Override
    public UserCard createNewUserCard(User user,int type){
        UserCard userCard = new UserCard();
        userCard.setType(type);
        if (type == 1){
            String dateType = "2018-09-09 14:00:00";
            userCard.setExpire(DateUtils.dateStringTypeToLong(dateType));
        }else if (type == 2){
            String dateType = "2018-09-16 14:00:00";
            userCard.setExpire(DateUtils.dateStringTypeToLong(dateType));
        }
        userCard.setStatus(2);
        String strNo = String.valueOf(System.nanoTime());
        strNo = strNo.substring(strNo.length()-4,strNo.length());
        String userCardCode = RandomUtil.generateOnlyNumber(4) + strNo + RandomUtil.toFixdLengthString(user.getId(), 4);
        userCard.setUserCardCode(userCardCode);
        userCard.setUserId(user.getId());
        if (!StringUtils.isEmpty(user.getOpenId())){
            userCard.setOpenId(user.getOpenId());
        }
        if (!StringUtils.isEmpty(user.getAppletOpenId())){
            userCard.setAppletOpenId(user.getAppletOpenId());
        }
        UserCard card = userCardRepository.save(userCard);
        return card;
    }


    @Override
    public UserCard createUserCard(User user, WxCard wxCard) {
        UserCard userCard = new UserCard();
        userCard.setCardId(wxCard.getCardId());
        String strNo = String.valueOf(System.nanoTime());
        strNo = strNo.substring(strNo.length()-4,strNo.length());
        String userCardCode = RandomUtil.generateOnlyNumber(4) + strNo + RandomUtil.toFixdLengthString(user.getId(), 4);
        userCard.setUserCardCode(userCardCode);
        userCard.setStatus(2);
//        userCard.setStatus(1);
        userCard.setExpire(DateUtils.dateLongToLong(new Date(), 30));
        userCard.setAppletOpenId(user.getAppletOpenId());
        userCard.setOpenId(user.getOpenId());
        UserCard card = userCardRepository.save(userCard);
        //TODO 发送客服消息领取卡卷
        return card;
    }

    @Override
    public List<UserCardResponse> findUsercards(User user) {
        List<UserCardResponse> userCardResponseList = new ArrayList<>();
       /* List<UserCard> cardList = null;
        if (!StringUtils.isEmpty(user.getOpenId())){
            cardList = userCardRepository.findByOpenIdOrAppletOpenId(user.getOpenId(), user.getAppletOpenId());
        }else{
            cardList = userCardRepository.findByAppletOpenId(user.getAppletOpenId());
        }
        List<WxCard> wxCardList = cardRepository.findAll();
        if (cardList != null && cardList.size() != 0){
            for (UserCard usercard : cardList) {
                Optional<WxCard> first = wxCardList.stream().filter(c -> c.getCardId().equals(usercard.getCardId())).findFirst();
                WxCard wxCard = null;
                if (first.isPresent()) {
                    wxCard = first.get();
                    UserCardResponse userCardResponse = new UserCardResponse();
                    userCardResponse.setId(usercard.getId());
                    userCardResponse.setStatus(usercard.getStatus());
                    userCardResponse.setUserCardCode(usercard.getUserCardCode());
                    userCardResponse.setExpire(usercard.getExpire());
                    userCardResponse.setTitle(wxCard.getTitle());
                    if (userCardResponse.getTitle().contains("法")) {
                        userCardResponse.setType(1);
                    } else if (userCardResponse.getTitle().contains("心")) {
                        userCardResponse.setType(2);
                    } else {
                        userCardResponse.setType(3);
                    }
                    userCardResponseList.add(userCardResponse);
                }
            }
            userCardResponseList = userCardResponseList.stream().sorted((c1, c2) -> c1.getStatus().compareTo(c2.getStatus())).collect(Collectors.toList());
            return userCardResponseList;
        }*/
        return userCardResponseList;
    }

    @Override
    public List<UserCardResponse> findNewUsercards(User user) {
        List<UserCard> userCards = userCardRepository.findByUserId(user.getId());
        List<UserCardResponse> userCardResponseList = new ArrayList<>();
        if (userCards != null && userCards.size() != 0){
            for (UserCard usercard : userCards) {
                UserCardResponse userCardResponse = new UserCardResponse();
                userCardResponse.setId(usercard.getId());
                userCardResponse.setStatus(usercard.getStatus());
                userCardResponse.setUserCardCode(usercard.getUserCardCode());
                userCardResponse.setExpire(usercard.getExpire());
                userCardResponse.setType(usercard.getType());
                userCardResponseList.add(userCardResponse);
                if (usercard.getType() == 1){
                    userCardResponse.setTitle("解惑人生重塑机缘");
                }else{
                    userCardResponse.setTitle("让幸福和性福有技可施");
                }
            }
        }
        return userCardResponseList;
    }


    @Override
    public WxCardApiSignature getH5Param(Long id) throws WxErrorException {
        UserCard userCard = userCardRepository.getOne(id);
        WxCardApiSignature signature = wxMpService.getCardService().createCardApiSignature(userCard.getCardId(), userCard.getAppletOpenId());
        signature.setCardId(userCard.getCardId());
        signature.setOpenId(userCard.getAppletOpenId());
        return signature;
    }


    @Override
    public void userCardToNew() {
        List<UserCard> cardList = userCardRepository.findAll();
        for (UserCard userCard : cardList) {
            User user = null;
            if (!StringUtils.isEmpty(userCard.getAppletOpenId())){
                user = userRepository.findByAppletOpenId(userCard.getAppletOpenId());
            }else {
                user = userRepository.findByOpenId(userCard.getOpenId());
            }
            userCard.setAppletOpenId(user.getAppletOpenId());
            userCard.setOpenId(user.getOpenId());
            if (userCard.getStatus() == 1){
                userCard.setStatus(2);
            }
            userCard.setUserId(user.getId());
            if (userCard.getCardId().equals("p2e54wk76GIcvR2lL9M_0r09zldg")){
                userCard.setType(2);
                String dateType = "2018-09-16 14:00:00";
                userCard.setExpire(DateUtils.dateStringTypeToLong(dateType));
            }else{
                String dateType = "2018-09-09 14:00:00";
                userCard.setExpire(DateUtils.dateStringTypeToLong(dateType));
                userCard.setType(1);
            }
            if (StringUtils.isEmpty(userCard.getUserCardCode())){
                String strNo = String.valueOf(System.nanoTime());
                strNo = strNo.substring(strNo.length()-4,strNo.length());
                userCard.setUserCardCode(RandomUtil.generateOnlyNumber(4)+ strNo +RandomUtil.toFixdLengthString(user.getId(),4));
            }
            userCardRepository.save(userCard);
        }



    }

    @Override
    public List<UserCard> findByStatus(int status) {
        return userCardRepository.findByStatus(status);
    }


}

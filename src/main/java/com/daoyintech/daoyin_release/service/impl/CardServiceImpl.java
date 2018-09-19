package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainJoiner;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.enums.bargain.BargainHelpType;
import com.daoyintech.daoyin_release.repository.order.BargainJoinerRepository;
import com.daoyintech.daoyin_release.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pei on 2018/08/17
 */
@Slf4j
@Service
public class CardServiceImpl implements CardService{

    @Autowired
    private UserService userService;

    @Autowired
    private BargainJoinerRepository bargainJoinerRepository;

    @Autowired
    private BargainOrderService bargainOrderService;

    @Autowired
    private UserCardService userCardService;

    /*@Override
    public void buildUserCard(Order order) {
        BargainOrder bargainOrder = bargainOrderService.findByOrderId(order.getId());
        List<BargainJoiner> bargainJoiners = bargainJoinerService.getJoinersByBargainOrder(bargainOrder);
        User user = userService.findById(order.getUserId());
        List<WxCard> wxCards = cardRepository.findAll();
        for (BargainJoiner bargainJoiner: bargainJoiners) {
            if (bargainJoiner.getType().ordinal() == BargainHelpType.法务券.ordinal()){
                WxCard wxCard = wxCards.stream().filter(card -> card.getTitle().contains("法务")).findAny().orElseGet(null);
                userCardService.createUserCard(user,wxCard);
            }
            if (bargainJoiner.getType().ordinal() == BargainHelpType.心理券.ordinal()){
                WxCard wxCard = wxCards.stream().filter(card -> card.getTitle().contains("心理")).findAny().orElseGet(null);
                userCardService.createUserCard(user,wxCard);
            }
        }
    }*/


    /**
     * 新卡卷功能,线下活动券
     * */
    @Async
    @Override
    public synchronized void buildNewUserCard(Order order){
        BargainOrder bargainOrder = bargainOrderService.findByOrderId(order.getId());
        List<BargainJoiner> bargainJoiners = bargainJoinerRepository.findByBargainOrderId(bargainOrder.getId());
        User user = userService.findById(order.getUserId());
        for (BargainJoiner bargainJoiner: bargainJoiners) {
            if (bargainJoiner.getType().ordinal() == BargainHelpType.法务券.ordinal()){
                userCardService.createNewUserCard(user,2);
                //让幸福和性福有技可施
            }
            if (bargainJoiner.getType().ordinal() == BargainHelpType.心理券.ordinal()){
                userCardService.createNewUserCard(user,1);
                //解惑人生重塑机缘
            }
            if (bargainJoiner.getType().ordinal() == BargainHelpType.线下活动卷.ordinal()){
                userCardService.createNewUserCard(user,2);
            }
        }
    }







}

package com.daoyintech.daoyin_release.service.admin;

import com.daoyintech.daoyin_release.entity.card.WxCard;

import java.util.List;

/**
 * @author pei on 2018/09/28
 */
public interface AdminWxCardService {


    String addWxCard(String title, String date, Integer type);


    List<WxCard> wxCardList();


    List<WxCard> upOrDownCard(Long id);


}

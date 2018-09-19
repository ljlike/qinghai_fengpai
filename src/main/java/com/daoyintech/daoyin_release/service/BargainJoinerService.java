package com.daoyintech.daoyin_release.service;

import com.daoyintech.daoyin_release.entity.order.bargain.BargainJoiner;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.entity.user.User;

import java.util.List;

/**
 * @author pei on 2018/08/17
 */
public interface BargainJoinerService {

    List<BargainJoiner> getJoinersByBargainOrder(BargainOrder bargainOrder);


    Boolean isUserJoin(BargainOrder bargainOrder, User user);


}

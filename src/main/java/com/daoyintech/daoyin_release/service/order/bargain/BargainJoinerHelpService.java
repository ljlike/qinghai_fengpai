package com.daoyintech.daoyin_release.service.order.bargain;

import com.daoyintech.daoyin_release.entity.order.bargain.BargainJoiner;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.entity.user.User;

import java.util.List;

/**
 * @author pei on 2018/08/20
 */
public interface BargainJoinerHelpService {

    Boolean isBargainCanHelp(BargainOrder bargainOrder, User user);

    List<BargainJoiner> getJoinersByDate(BargainOrder bargainOrder);


    boolean isSelf(BargainOrder bargainOrder, User user);


    boolean isMaxHelpCount(BargainOrder bargainOrder);


    int checkisCanCatch(BargainOrder bargainOrder, User byUnionId);


//    BargainJoiner bargainHelp(BargainOrder bargainOrder, User user);


    BargainJoiner bargainDrawHelp(BargainOrder bargainOrder, User user);


}

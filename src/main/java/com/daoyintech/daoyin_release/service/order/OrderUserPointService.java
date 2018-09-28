package com.daoyintech.daoyin_release.service.order;

import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.entity.user.User;

import java.math.BigDecimal;
import java.util.List; /**
 * @author pei on 2018/08/14
 */
public interface OrderUserPointService {

    /**
     * @param id
     * @param user
     * @param items
     * @return
     */
    BigDecimal calPayPrice(Long id, User user, List<LineItem> items);




}

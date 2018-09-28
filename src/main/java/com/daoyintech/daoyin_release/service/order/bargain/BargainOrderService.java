package com.daoyintech.daoyin_release.service.order.bargain;

import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.entity.user.User;

import java.math.BigDecimal;

/**
 * @author pei on 2018/08/17
 */
public interface BargainOrderService {

    BargainOrder findByOrderId(Long id);


    BargainOrder create(Long productId, User user, Long formatId, Long colorId);


    void orderPayedExpired(BargainOrder bargainOrder, Order order);


    BargainOrder findOrderByOrderNo(String bargainOrderNo);


    BigDecimal calCutPrice(BargainOrder bargainOrder);


    Order finishBargainOrder(String bargainOrderNo);

    BargainOrder save(BargainOrder bargainOrder);


    BargainOrder findOne(Long bargainOrderId);


}

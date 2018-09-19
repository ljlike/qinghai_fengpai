package com.daoyintech.daoyin_release.messageQueue.receiver;

import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.enums.order.OrderStatus;
import com.daoyintech.daoyin_release.service.BargainOrderService;
import com.daoyintech.daoyin_release.service.OrderService;
import com.daoyintech.daoyin_release.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderHandlerReceiver {

    @Autowired
    private OrderService handlerService;

    @Autowired
    private BargainOrderService bargainOrderService;

    /**
     * 检查砍价订单是否过期
     *
     * @param orderNo
     */
    public void checkOrderExpired(String orderNo) {
        log.info("{}:检查砍价订单是否过期,orderNo={}", DateUtils.getStringDate(),orderNo);
        Order order = handlerService.findOrderByOrderNo(orderNo);
        if(order != null){
            BargainOrder bargainOrder = bargainOrderService.findByOrderId(order.getId());
            if (order.getStatus().ordinal() == OrderStatus.砍价中.ordinal()){
                bargainOrderService.orderPayedExpired(bargainOrder,order);
            }
        }

    }
}

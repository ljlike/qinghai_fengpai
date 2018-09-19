package com.daoyintech.daoyin_release.messageQueue.receiver;

import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.enums.order.OrderStatus;
import com.daoyintech.daoyin_release.service.OrderService;
import com.daoyintech.daoyin_release.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExpireAndDeleteOrderHandlerReceiver {

    @Autowired
    private OrderService orderService;

    /**
     * 检查订单是否过期，需要取消
     *
     * @param orderNo
     */
    public void checkOrderExpiredAndDelete(String orderNo) {
        Order order = orderService.findOrderByOrderNo(orderNo);
        if(order != null && order.getStatus().equals(OrderStatus.等待支付)){
            orderService.loseOrder(orderNo);
            log.info("{}:检查订单是否过期,取消成功:orderNo = {}", DateUtils.getStringDate(),orderNo);
        }
    }
}

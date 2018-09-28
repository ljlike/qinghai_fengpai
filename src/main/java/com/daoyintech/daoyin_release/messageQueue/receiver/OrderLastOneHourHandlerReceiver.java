package com.daoyintech.daoyin_release.messageQueue.receiver;

import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.service.order.bargain.BargainOrderService;
import com.daoyintech.daoyin_release.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderLastOneHourHandlerReceiver {

    @Autowired
    private OrderService handlerService;

    @Autowired
    private BargainOrderService bargainOrderService;

    //@Autowired
    //private WxBargainTemplateService wxBargainTemplateService;

    public void checkOrderLastOneHourExpired(String orderNo){
        Order order = handlerService.findOrderByOrderNo(orderNo);
        if(order != null){
            BargainOrder bargainOrder = bargainOrderService.findByOrderId(order.getId());
            //if (order.getStatus().ordinal() == OrderStatus.砍价中.ordinal()){
                //TODO 消息发送通知
                //wxBargainTemplateService.sendBargainOrderlastOneHour(bargainOrder);
            //}
        }
    }
}

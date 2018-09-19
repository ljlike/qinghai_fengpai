package com.daoyintech.daoyin_release.service;

import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.response.order.BargainOrderRequest;
import com.daoyintech.daoyin_release.response.order.OrderNewRequest;
import com.daoyintech.daoyin_release.response.order.OrderResponse;

import java.util.List;
import java.util.SortedMap;

/**
 * @author pei on 2018/08/14
 */
public interface OrderService {

    Order createOrder(OrderNewRequest orderNewRequest, String unionId);

    Order findById(Long orderId);

    Order save(Order order);

    /**
     * @param order
     * @param unionId
     * @param ip
     */
    SortedMap<Object, Object> getPayInfo(Order order, String unionId, String ip);


    Boolean deleteOrder(String orderNo);


    List<OrderResponse> findByOrderStatus(Integer status, User user);


    Order findOrderByOrderNo(String orderNo);


    void loseOrder(String orderNo);


    Order updateOrderStatusDelivery(String out_trade_no);


    boolean isFreightFee(Order order);


    OrderResponse findByOrderNo(String orderNo);


    Order saveOrderByOrderNo(BargainOrderRequest bargainOrderRequest);


    void updateOrderStatus(String orderNo);


    Order createBargainOrder(LineItem item, Long userId);


}







package com.daoyintech.daoyin_release.service.impl.order;

import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.entity.user.integral.UserIntegral;
import com.daoyintech.daoyin_release.enums.order.OrderPayType;
import com.daoyintech.daoyin_release.enums.order.OrderType;
import com.daoyintech.daoyin_release.repository.order.LineItemRepository;
import com.daoyintech.daoyin_release.service.order.OrderService;
import com.daoyintech.daoyin_release.service.order.OrderUserPointService;
import com.daoyintech.daoyin_release.service.user.UserIntegralDetailService;
import com.daoyintech.daoyin_release.service.user.UserIntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author pei on 2018/08/14
 */
@Service
public class OrderUserPointServiceImpl implements OrderUserPointService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private LineItemRepository lineItemRepository;


    @Autowired
    private UserIntegralService userIntegralService;

    @Autowired
    private UserIntegralDetailService userIntegralDetailService;

    /**
     * 1. 电商创建订单controller
     * 1.1 接口参数(支付方式，购买的商品,是否使用积分)
     * 1.2 创建订单
     * 1.3 计算订单支付金额(涉及更新订单信息)
     * 1.4 生成微信支付参数cal
     * 1.5 调用统一下单
     * 1.6 返回微信支付参数
     * @param orderId
     * @param user
     * @return
     */
    @Override
    public BigDecimal calPayPrice(Long orderId, User user, List<LineItem> items) {
        Order order = orderService.findById(orderId);
        order.setOrderPayType(OrderPayType.小程序);
        order = orderService.save(order);
        if (order.getOrderType().equals(OrderType.砍价订单)){
            return order.getPayMoney();
        }
        BigDecimal payMoney = new BigDecimal(totalPrice(items)).add(order.getFreightPrice());
        if (order.getIsMyselfPick() == Boolean.TRUE || order.getIsFreightFree() == Boolean.TRUE) {
            payMoney = new BigDecimal(totalPrice(items));
        }
        order.setPayMoney(payMoney);
        orderService.save(order);
        return payMoney;



       /* items.forEach(item ->{
            item.setOrderId(orderId);
            lineItemRepository.save(item);
        });*/
       /* if (!order.getIsUseIntegral()) {
            order.setOrderPayType(OrderPayType.小程序);
            order = orderService.save(order);
            if (order.getOrderType().equals(OrderType.砍价订单)){
                return order.getPayMoney();
            }
            BigDecimal payMoney = new BigDecimal(totalPrice(items)).add(order.getFreightPrice());
            if (order.getIsMyselfPick() == true || order.getIsFreightFree() == true) {
                payMoney = new BigDecimal(totalPrice(items));
            }
            order.setPayMoney(payMoney);
            order = orderService.save(order);
            return payMoney;
        }
        //1,积分足以支付
        //2,积分不足以支付
        order.setOrderPayType(OrderPayType.积分和微信);
        UserIntegral userIntegral = userIntegralService.findByUser(user);
        if (order.getFreightPrice().compareTo(userIntegral.getIntegral()) <= 0) {
            order.setUsedIntegral(order.getFreightPrice());
            BigDecimal payMoney = new BigDecimal(totalPrice(items));
            order.setPayMoney(payMoney);
            order.setIsFreightFree(true);
            order = orderService.save(order);
            UserIntegral UserIntegralUpdateAfter = updateUserIntegral(order, user);
            if (order.getUsedIntegral().doubleValue() != 0) {
                userIntegralDetailService.buildUserIntegralDetail(order, user, order.getUsedIntegral(), UserIntegralFromType.购物消费);
            }
            return payMoney;
        }
        BigDecimal payMoney = new BigDecimal(totalPrice(items)).add(order.getFreightPrice());
        order.setPayMoney(payMoney);
        orderService.save(order);
        return payMoney;*/
    }

    public UserIntegral updateUserIntegral(Order order, User user){
        UserIntegral userIntegral = userIntegralService.findByUser(user);
        userIntegral.setIntegral(userIntegral.getIntegral().subtract(order.getUsedIntegral()));
        userIntegralService.save(userIntegral);
        return userIntegral;
    }

    public Double totalPrice(List<LineItem> items){
        Double sum = 0.0;
        for(int i = 0;i < items.size(); i++){
            LineItem lineItem = items.get(i);
            sum += lineItem.totalPrice();
        }
        return sum;
    }

}


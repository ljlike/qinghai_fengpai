package com.daoyintech.daoyin_release.service.impl.order;

import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainJoiner;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.entity.product.Product;
import com.daoyintech.daoyin_release.entity.product.SubProductType;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.enums.bargain.BargainHelpType;
import com.daoyintech.daoyin_release.enums.order.OrderStatus;
import com.daoyintech.daoyin_release.enums.order.OrderType;
import com.daoyintech.daoyin_release.enums.user.UserIntegralFromType;
import com.daoyintech.daoyin_release.repository.user.UserRepository;
import com.daoyintech.daoyin_release.repository.product.ProductRepository;
import com.daoyintech.daoyin_release.repository.order.OrderRepository;
import com.daoyintech.daoyin_release.repository.product.ProductTypeRepository;
import com.daoyintech.daoyin_release.service.*;
import com.daoyintech.daoyin_release.service.order.OrderNoticeService;
import com.daoyintech.daoyin_release.service.order.OrderService;
import com.daoyintech.daoyin_release.service.order.bargain.BargainJoinerService;
import com.daoyintech.daoyin_release.service.order.bargain.BargainOrderService;
import com.daoyintech.daoyin_release.service.order.cart.LineItemService;
import com.daoyintech.daoyin_release.service.product.SubProductTypeService;
import com.daoyintech.daoyin_release.service.user.UserIntegralDetailService;
import com.daoyintech.daoyin_release.service.user.UserIntegralService;
import com.daoyintech.daoyin_release.service.user.UserProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.SortedMap;

/**
 * @author pei on 2018/08/16
 */
@Service
public class OrderNoticeServiceImpl implements OrderNoticeService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private LineItemService lineItemService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SubProductTypeService subProductTypeService;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BargainOrderService bargainOrderService;

    @Autowired
    private BargainJoinerService bargainJoinerService;

    @Autowired
    private UserIntegralService userIntegralService;

    @Autowired
    private UserIntegralDetailService userIntegralDetailService;

    @Autowired
    private UserProfitService profitOrderService;

    @Autowired
    private CardService cardService;

    @Autowired
    private UserProfitService userProfitService;

    @Value("${integral.receiveIntegralForAllJoiners}")
    private String receiveIntegralForAllJoiners;

    @Transactional
    @Override
    public void notice(SortedMap<Object, Object> packageParams) {
        if (packageParams.get("result_code").equals("SUCCESS")){
            Order order = orderRepository.findByOrderNo((String) packageParams.get("out_trade_no"));
            if (checkPay(order)) {
                Order outTradeNoOrder = orderService.updateOrderStatusDelivery((String) packageParams.get("out_trade_no"));
                List<LineItem> lineItems = lineItemService.findByOrderId(outTradeNoOrder.getId());
                outTradeNoOrder.setStatus(OrderStatus.待发货);
                Order saveOrder = orderRepository.save(outTradeNoOrder);
                Long subProductTypeId = productRepository.getOne(lineItems.get(0).getProductId()).getSubProductTypeId();
                SubProductType subProductType = subProductTypeService.getOne(subProductTypeId);
                Long id = productTypeRepository.getOne(subProductType.getProductTypeId()).getId();
                if (id.equals(8L)){
                    User user = userRepository.getOne(saveOrder.getUserId());
                    user.setIsPayGiftBag(true);
                    userRepository.save(user);
                    //发送消息
                    //wxOrderTemplateService.sendGiftBagOrderPayAfter(order);
                }
                handleProduction(lineItems); //处理库存和销售量
                //reapUserIntegral(saveOrder);//积分
                userIntegralService.userIntegralDraw(saveOrder);
//                handleProfitOrder(saveOrder);//分润
                userProfitService.handleProfit(saveOrder);//分润
                if (saveOrder.getOrderType().equals(OrderType.砍价订单)){
                    cardService.buildNewUserCard(saveOrder);
                }
            }
        }
    }



    /**
     * 订单分润
     * @param order
     */
    private void handleProfitOrder(Order order) {
        if (order.getStatus().equals(OrderStatus.待发货)) {
            profitOrderService.createProfitOrder(order);
        }
    }



    //积分和积分详情
    private void reapUserIntegral(Order order){
        if (order.getOrderType().equals(OrderType.砍价订单)) {
            BargainOrder bargainOrder = bargainOrderService.findByOrderId(order.getId());
            List<BargainJoiner> joiners = bargainJoinerService.getJoinersByBargainOrder(bargainOrder);
            if (joiners.size() != 0){
                for (BargainJoiner joiner:joiners) {
                    User joinerUser = userRepository.getOne(joiner.getJoinerId());
                    User user = userRepository.getOne(order.getUserId());
                    //每个参与者获得5积分
                    userIntegralService.updateUserPoint(new BigDecimal(receiveIntegralForAllJoiners),joinerUser);
                    userIntegralDetailService.buildUserIntegralDetail(order, joinerUser,new BigDecimal(receiveIntegralForAllJoiners), UserIntegralFromType.帮助朋友);
                    //发送模板消息
                    //wxBargainTemplateService.sendBargainJoinInfoAfterPay(bargainOrder, joiner);
                    //积分券获得的10积分
                    if (joiner.getType().equals(BargainHelpType.getType(3))){
                        userIntegralService.updateUserPoint(new BigDecimal(10),user);
                        userIntegralDetailService.buildUserIntegralDetail(order, user,new BigDecimal(10), UserIntegralFromType.帮助朋友);
                        //发送模板消息
                        //wxBargainTemplateService.sendBargainJoinInfoAfterPayByPointTicket(bargainOrder, joiner);
                    }
                }
            }
            //发送模板消息
            //wxBargainTemplateService.sendBargainOrderPayAfter(bargainOrder);
        }
    }


    private void handleProduction(List<LineItem> lineItems) {
        lineItems.forEach(lineItem -> {
            Product production = productRepository.getOne(lineItem.getProductId());
            production.setInitSellCount(production.getInitSellCount() + lineItem.getQuantity());
            production.setShowSellCount(production.getShowSellCount() + lineItem.getQuantity());
            productRepository.save(production);
        });
    }

    private boolean checkPay(Order order) {
        if (!order.getStatus().equals(OrderStatus.等待支付)) {
            return false;
        }
        return true;
    }
}






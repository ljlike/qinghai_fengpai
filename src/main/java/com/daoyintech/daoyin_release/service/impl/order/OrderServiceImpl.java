package com.daoyintech.daoyin_release.service.impl.order;

import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.entity.product.Product;
import com.daoyintech.daoyin_release.entity.product.ProductColor;
import com.daoyintech.daoyin_release.entity.product.ProductFormat;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.entity.user.UserAddress;
import com.daoyintech.daoyin_release.entity.user.integral.UserIntegral;
import com.daoyintech.daoyin_release.enums.ResultEnum;
import com.daoyintech.daoyin_release.enums.bargain.BargainOrderStatus;
import com.daoyintech.daoyin_release.enums.order.OrderStatus;
import com.daoyintech.daoyin_release.enums.order.OrderType;
import com.daoyintech.daoyin_release.enums.user.UserIntegralFromType;
import com.daoyintech.daoyin_release.exception.DaoyinException;
import com.daoyintech.daoyin_release.messageQueue.sender.ExpireAndDeleteOrderSender;
import com.daoyintech.daoyin_release.repository.order.LineItemRepository;
import com.daoyintech.daoyin_release.repository.product.ProductRepository;
import com.daoyintech.daoyin_release.repository.user.UserAddressRepository;
import com.daoyintech.daoyin_release.repository.user.UserRepository;
import com.daoyintech.daoyin_release.repository.order.BargainOrderRepository;
import com.daoyintech.daoyin_release.repository.order.OrderRepository;
import com.daoyintech.daoyin_release.repository.product.ProductColorRepository;
import com.daoyintech.daoyin_release.repository.product.ProductFormatRepository;
import com.daoyintech.daoyin_release.response.order.*;
import com.daoyintech.daoyin_release.response.product.ProductFormatResponse;
import com.daoyintech.daoyin_release.response.product.ProductResponse;
import com.daoyintech.daoyin_release.service.order.OrderService;
import com.daoyintech.daoyin_release.service.order.OrderUserPointService;
import com.daoyintech.daoyin_release.service.order.cart.LineItemService;
import com.daoyintech.daoyin_release.service.user.UserAddressService;
import com.daoyintech.daoyin_release.service.user.UserIntegralDetailService;
import com.daoyintech.daoyin_release.service.user.UserIntegralService;
import com.daoyintech.daoyin_release.service.user.UserService;
import com.daoyintech.daoyin_release.utils.DateUtils;
import com.daoyintech.daoyin_release.utils.WebUtil;
import com.daoyintech.daoyin_release.utils.pay.PayCommonUtil;
import com.daoyintech.daoyin_release.utils.pay.WxPayUtil;
import com.daoyintech.daoyin_release.utils.pay.XMLUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author pei on 2018/08/14
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private LineItemService lineItemService;

    @Autowired
    private OrderUserPointService orderUserPointService;

    @Autowired
    private BargainOrderRepository bargainOrderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserIntegralService userIntegralService;

    @Autowired
    private UserIntegralDetailService userIntegralDetailService;

    @Autowired
    private ProductFormatRepository productFormatRepository;

    @Autowired
    private ProductColorRepository productColorRepository;

    @Autowired
    private ExpireAndDeleteOrderSender expireAndDeleteOrderSender;

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private LineItemRepository lineItemRepository;

    @Value("${pay.freight_price}")
    private BigDecimal freightPrice;
    @Value("${wxPay.appId}")
    private String appId;
    @Value("${wxPay.body}")
    private String body;
    @Value("${wxPay.mchId}")
    private String mchId;
    @Value("${wxPay.notifyUrl}")
    private String notifyUrl;
    @Value("${wxPay.tradeType}")
    private String tradeType;
    @Value("${wxPay.mchKey}")
    private String mchKey;
    @Value("${wxPay.payUrl}")
    private String payUrl;

    @Transactional
    @Override
    public Order createOrder(OrderNewRequest orderNewRequest, String unionId) {
        User user = userRepository.findByUnionId(unionId);
        Order order = new Order();
        //收货地址
        UserAddress address = userAddressRepository.getOne(orderNewRequest.getAddressId());
        log.info("买家下单收货地址:{}:{}:unionId = {}",address,orderNewRequest,unionId);
        order.setUserId(user.getId());
        order.setIsUseIntegral(orderNewRequest.getIsUseIntegral());
        order.setIsMyselfPick(orderNewRequest.getIsMyselfPick());
        order.setProvince(address.getProvince());
        order.setCity(address.getCity());
        order.setDistrict(address.getDistrict());
        order.setStreet(address.getStreet());
        order.setPhone(address.getPhone());
        order.setName(address.getName());
        order.setOrderNo(WebUtil.noceStr());
        order.setRefundNo(WebUtil.noceStr());
        order.setOrderType(OrderType.普通订单);
        order.setFreightPrice(freightPrice);
        Order save = orderRepository.save(order);
        List<LineItem> items = lineItemService.transformFromModel(orderNewRequest.getItems(), save);
        BigDecimal payMoney = orderUserPointService.calPayPrice(save.getId(),user,items);
        save.setPayMoney(payMoney);
        Order saveOrder = orderRepository.save(save);
        //rabbit 消息队列
        expireAndDeleteOrderSender.sendCheckOrderExpiredAndDelete(saveOrder.getOrderNo());
        return saveOrder;
    }

    @Override
    public Order findById(Long orderId) {
        return orderRepository.getOne(orderId);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    /**
     * 微信小程序支付
     *
     * @param order
     * @param unionId
     * @return
     * @throws IOException
     */
    @Transactional
    @Override
    public SortedMap<Object, Object> getPayInfo(Order order, String unionId,String ip) {
        User user = userService.findByUnionId(unionId);
        String openId;
        if (StringUtils.isEmpty(user.getAppletOpenId())){
            openId = user.getOpenId();
        }else{
            openId = user.getAppletOpenId();
        }
        SortedMap<Object, Object> sortedMap = buildPayRequest(order,openId, ip);
        log.info("{}:请求微信支付数据:sortedMap={}", DateUtils.getStringDate(),sortedMap);
        String requestXML = PayCommonUtil.getRequestXml(sortedMap);
        String result =WxPayUtil.httpRequest(payUrl, "POST", requestXML);
        Map map = null;
        try {
            map = XMLUtil.doXMLParse(result);
        } catch (Exception e) {
            log.error("【微信小程序支付异步通知】xml转换失败{}",DateUtils.getStringDate());
            throw new RuntimeException("【微信小程序支付异步通知】xml转换失败");
        }
        log.info("{}:微信支付请求返回数据:map={}", DateUtils.getStringDate(),map);
        String returnCode = (String) map.get("return_code");
        if (!returnCode.equals("SUCCESS")){
            log.error("{}:{}",DateUtils.getStringDate(), ResultEnum.ORDER_PREPAY_ERROR.getMessage());
            throw new DaoyinException(ResultEnum.ORDER_PREPAY_ERROR);
        }
        SortedMap<Object, Object> packageP = jsSignPayInfo(map);
        return packageP;
    }


    public SortedMap<Object, Object> jsSignPayInfo(Map map) {
        //得到prepay_id
        String prepay_id = (String) map.get("prepay_id");
        SortedMap<Object, Object> packageP = new TreeMap<Object, Object>();
        //！！！注意，这里是appId,上面是appid
        packageP.put("appId", appId);
        //时间戳
        packageP.put("nonceStr", System.currentTimeMillis()+"");
        //必须把package写成 "prepay_id="+prepay_id这种形式
        packageP.put("package", "prepay_id=" + prepay_id);
        //paySign加密
        packageP.put("signType", "MD5");
        packageP.put("timeStamp", (System.currentTimeMillis() / 1000) + "");
        //得到paySign
        String paySign = PayCommonUtil.createSign("UTF-8", packageP, mchKey);
        packageP.put("paySign", paySign);
        log.info("{}:微信支付返回前端数据:packageP = {}", DateUtils.getStringDate(),packageP);
        return packageP;
    }


    @Transactional
    @Override
    public Boolean deleteOrder(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo);
        order.setCanceledAt(new Date());
        order.setStatus(OrderStatus.已取消);
        order = orderRepository.save(order);
        if (order.getOrderType().equals(OrderType.砍价订单)){
            BargainOrder bargainOrder = bargainOrderRepository.findByOrderId(order.getId());
            bargainOrder.setStatus(BargainOrderStatus.取消);
            bargainOrderRepository.save(bargainOrder);
        }
        //商品库存回退
        productInventoryBack(order);
        //积分回退
        //integralBack(order);
        return true;
    }

    @Override
    public List<OrderResponse> findByOrderStatus(Integer status, User user) {
        List<Order> orders = new ArrayList<>();
        OrderStatus orderStatus = OrderStatus.getStatus(status);
        if (status == 11){
            //所有订单
            orders = orderRepository.findByUserId(user.getId());
        } else if (status == 3) {
            //orders = orderRepository.queryBargainOrder(user.getId(), OrderType.砍价订单, BargainOrderStatus.正在进行);
            orders = orderRepository.findByUserIdAndOrderTypeAndStatus(user.getId(),OrderType.砍价订单,OrderStatus.砍价中);
            log.info("{}:正在进行:砍价订单:{}", DateUtils.getStringDate(),orders);
            //checkExpiredAndSave(orders);
            //orders = orderRepository.queryBargainOrder(user.getId(), OrderType.砍价订单, BargainOrderStatus.正在进行);
            orders = orderRepository.findByUserIdAndOrderTypeAndStatus(user.getId(),OrderType.砍价订单,OrderStatus.砍价中);
            log.info("{}:正在进行:砍价订单:{}", DateUtils.getStringDate(),orders);
        } else {
            orders = orderRepository.findByUserIdAndStatusOrderByUpdatedAtDesc(user.getId(), orderStatus);
        }


        //数据封装
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders){
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setCreatedAt(order.getCreatedAt());
            orderResponse.setOrderNo(order.getOrderNo());

            Double initPrice = 0.0;
            Double price = 0.0;
            List<LineItem> lineItems = lineItemService.findByOrderId(order.getId());
            List<LineItemDetailResponse> lineItemDetailResponses = new ArrayList<>();
            for (LineItem lineItem : lineItems){
                Product product = null;
                if (!StringUtils.isEmpty(lineItem.getProductId())){
                    product = productRepository.findById(lineItem.getProductId()).orElse(null);
                }
                ProductFormat format = null;
                if (!StringUtils.isEmpty(lineItem.getFormatId())){
                    format = productFormatRepository.findById(lineItem.getFormatId()).orElse(null);
                }
                ProductColor color = null;
                if (!StringUtils.isEmpty(lineItem.getColorId())){
                    color = productColorRepository.findById(lineItem.getColorId()).orElse(null);
                }
                LineItemDetailResponse lineItemDetailResponse = new LineItemDetailResponse();
                lineItemDetailResponse.setId(lineItem.getId());
                lineItemDetailResponse.setQuantity(lineItem.getQuantity());
                lineItemDetailResponse.setSinglePrice(lineItem.getPrice());
                ProductResponse productResponse = new ProductResponse();
                if (!StringUtils.isEmpty(product)){
                    productResponse.setId(product.getId());
                    productResponse.setName(product.getName());
                    productResponse.setPointPrecent(product.getPointPercent());
                    productResponse.setThumb(product.url());
                    productResponse.setIsLuxury(product.getIsLuxury());
                    productResponse.setPointPrecent(product.getPointPercent());
                }
                if (!StringUtils.isEmpty(color)){
                    productResponse.setColor(color);
                }
                if (!StringUtils.isEmpty(format)){
                    productResponse.setFormat(ProductFormatResponse.getProductFormatModel(format));
                }
                lineItemDetailResponse.setProduct(productResponse);

                lineItemDetailResponses.add(lineItemDetailResponse);
                //计算价格
                initPrice += lineItem.initTotalPrice(format.getSellPrice());
                price += lineItem.totalPrice();
            }
            orderResponse.setLineItems(lineItemDetailResponses);
            orderResponse.setInitPrice(BigDecimal.valueOf(initPrice).setScale(2,BigDecimal.ROUND_HALF_DOWN));
            orderResponse.setPrice(BigDecimal.valueOf(price).setScale(2,BigDecimal.ROUND_HALF_DOWN));

            orderResponse.setStatus(order.getStatus().ordinal());
            orderResponse.setIsMyselfPick(order.getIsMyselfPick());
            if (order.getOrderType().ordinal() == 1){
                orderResponse.setIsBargain(true);
            }else {
                orderResponse.setIsBargain(false);
            }
            orderResponse.setLineItems(lineItemDetailResponses);

            orderResponse.setDelivery(DeliveryResponse.deliveryTransformDeliveryResponse(order));
            orderResponse.setExpress(ExpressCompanyResponse.expressCompanyTransformExpressCompanyResponse(order));
            orderResponse.setOrderTime(OrderTimeResponse.orderTimeTransformOrderTimeResponse(order));

            BargainOrder bargainOrder = bargainOrderRepository.findByOrderId(order.getId());
            if (bargainOrder != null){
                LineItem item = lineItems.get(0);
                orderResponse.setBargain(BargainResponse.bargainOrderTransformBargainResponse(bargainOrder,order,item));
            }
            if (order.getUsedIntegral() != null){
                orderResponse.setUsedIntegral(order.getUsedIntegral().doubleValue());
            }
            orderResponses.add(orderResponse);
        }

        return orderResponses;
        /*return Lists.transform(orders, order ->
                OrderResponse.bargainTransformOrderResponse(order,
                        bargainOrderRepository.findByOrderId(order.getId()),
                        lineItemService.findByOrderId(order.getId()),
                        productRepository,
                        productFormatRepository,
                        productColorRepository)
        );*/
    }

    @Override
    public Order findOrderByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }

    @Transactional
    @Override
    public void loseOrder(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo);
        order.setLoseAt(new Date());
        order.setStatus(OrderStatus.已失效);
        orderRepository.save(order);
        productInventoryBack(order);
        integralBack(order);
        if (order.getOrderType().equals(OrderType.砍价订单)){
            BargainOrder bargainOrder = bargainOrderRepository.findByOrderId(order.getId());
            bargainOrder.setStatus(BargainOrderStatus.超时);
            bargainOrderRepository.save(bargainOrder);
            //TODO 消息发送
            //wxBargainTemplateService.sendBargainOrderCancel(bargainOrder);
        }else {
            //wxBargainTemplateService.sendOrderCancel(order);
        }
    }

    public Order updateOrderStatusDelivery(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo);
        order.setStatus(OrderStatus.待发货);
        order.setPayAt(new Date());
        return orderRepository.save(order);
    }

    @Override
    public boolean isFreightFee(Order order) {
        if(order.getIsFreightFree() || order.getIsMyselfPick()){
            return true;
        }
        return false;
    }

    @Override
    public OrderResponse findByOrderNo(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo);
        if (order.getOrderType().ordinal() == 1) {
            BargainOrder bargainOrder = bargainOrderRepository.findByOrderId(order.getId());
            return OrderResponse.bargainTransformOrderResponse(order,
                                                                bargainOrder,
                                    lineItemService.findByOrderId(order.getId()),
                                                                productRepository,
                                                                productFormatRepository,
                                                                productColorRepository);
        }
        return OrderResponse.transformOrderResponse(order,
                lineItemService.findByOrderId(order.getId()),
                productRepository,
                productFormatRepository,
                productColorRepository);
    }

    @Override
    public Order saveOrderByOrderNo(BargainOrderRequest bargainOrderRequest) {
        Order order = orderRepository.findByOrderNo(bargainOrderRequest.getOrderNo());
        UserAddress address = userAddressService.findById(bargainOrderRequest.getAddressId());
        log.info("分享购买家下单收货地址:{}:{}",address,bargainOrderRequest);
        order.setIsUseIntegral(bargainOrderRequest.getIsUseIntegral());
        order.setProvince(address.getProvince());
        order.setCity(address.getCity());
        order.setDistrict(address.getDistrict());
        order.setStreet(address.getStreet());
        order.setPhone(address.getPhone());
        order.setName(address.getName());
        order.setIsMyselfPick(bargainOrderRequest.getIsMyselfPick());
        Order save = orderRepository.save(order);
        if (order.getIsFreightFree() == Boolean.FALSE && order.getIsMyselfPick() == Boolean.TRUE){
            save.setIsFreightFree(true);
            save.setPayMoney(order.getPayMoney().subtract(order.getFreightPrice()));
            save = orderRepository.save(save);
        }
        return save;
    }

    @Override
    public void updateOrderStatus(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo);
        order.setStatus(OrderStatus.已完成);
        order.setFinishedAt(new Date());
        orderRepository.save(order);
    }

    @Override
    public Order createBargainOrder(LineItem item, Long userId) {
        Order order = new Order();
        order.setOrderType(OrderType.砍价订单);
        order.setStatus(OrderStatus.砍价中);
        order.setOrderNo(WebUtil.noceStr());
        order.setRefundNo(WebUtil.noceStr());
        order.setUserId(userId);
        order.setFreightPrice(freightPrice);
        order.setPayMoney(BigDecimal.valueOf(item.totalPrice()).add(freightPrice));
        Order save = orderRepository.save(order);
        item.setOrderId(save.getId());
        lineItemRepository.save(item);
        return save;
    }


    /**
     * 积分回退
     *
     * @param order
     */
    public void integralBack(Order order) {
        User user = userService.findById(order.getUserId());
        UserIntegral userIntegral = userIntegralService.findByUser(user);
        userIntegral.setIntegral(userIntegral.getIntegral().add(order.getUsedIntegral()));
        userIntegralService.save(userIntegral);
        if (order.getUsedIntegral().doubleValue() != 0) {
            userIntegralDetailService.buildUserIntegralDetail(order, user, order.getUsedIntegral(), UserIntegralFromType.取消订单);
        }
    }


    public SortedMap<Object, Object> buildPayRequest(Order order, String openId,String ip) {
        SortedMap<Object, Object> sortedMap = new TreeMap<Object, Object>();
        sortedMap.put("appid", appId);
        sortedMap.put("body",  body);
        sortedMap.put("mch_id", mchId);
        sortedMap.put("nonce_str", WxPayUtil.getNonceStr());
        sortedMap.put("notify_url",notifyUrl);
        sortedMap.put("openid", openId);
        sortedMap.put("out_trade_no", order.getOrderNo());
        sortedMap.put("spbill_create_ip", ip);
        sortedMap.put("total_fee", String.valueOf(order.getPayMoney().setScale(2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue()));
        sortedMap.put("trade_type",tradeType);
        log.info("{}:请求微信支付数据,加密前:sortedMap={}", DateUtils.getStringDate(),sortedMap);
        String sign = PayCommonUtil.createSign("UTF-8", sortedMap, mchKey);
        sortedMap.put("sign", sign);
        return sortedMap;
    }


    private void productInventoryBack( Order order){
        List<LineItem> items = lineItemService.findByOrderId(order.getId());
        for (LineItem item : items) {
            Product product = productRepository.getOne(item.getProductId());
            product.setInventory(product.getInventory() + item.getQuantity());
            productRepository.save(product);
        }
    }

    private void checkExpiredAndSave(List<Order> orders) {
        if (orders.size() != 0) {
            for (Order order : orders) {
                BargainOrder bargainOrder = bargainOrderRepository.findByOrderId(order.getId());
                if (new Date(bargainOrder.getExpiredAt().getTime() + 1000 * 60 * 60 * 24).before(new Date())) {
                    deleteOrder(order.getOrderNo());
                    bargainOrder.setStatus(BargainOrderStatus.超时);
                    bargainOrderRepository.save(bargainOrder);
                    order.setStatus(OrderStatus.已失效);
                    orderRepository.save(order);
                    productInventoryBack(order);
                    integralBack(order);
                }
            }
        }
    }

}

package com.daoyintech.daoyin_release.service.impl.order.bargain;

import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainJoiner;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.entity.product.Product;
import com.daoyintech.daoyin_release.entity.product.ProductColor;
import com.daoyintech.daoyin_release.entity.product.ProductFormat;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.enums.bargain.BargainHelpType;
import com.daoyintech.daoyin_release.enums.bargain.BargainOrderStatus;
import com.daoyintech.daoyin_release.enums.order.OrderStatus;
import com.daoyintech.daoyin_release.messageQueue.sender.ExpireAndDeleteOrderSender;
import com.daoyintech.daoyin_release.messageQueue.sender.OrderLastOneHourHandlerSender;
import com.daoyintech.daoyin_release.messageQueue.sender.OrderSender;
import com.daoyintech.daoyin_release.repository.order.BargainOrderRepository;
import com.daoyintech.daoyin_release.repository.order.OrderRepository;
import com.daoyintech.daoyin_release.repository.product.ProductColorRepository;
import com.daoyintech.daoyin_release.repository.product.ProductFormatRepository;
import com.daoyintech.daoyin_release.repository.product.ProductRepository;
import com.daoyintech.daoyin_release.service.order.bargain.BargainJoinerService;
import com.daoyintech.daoyin_release.service.order.bargain.BargainOrderService;
import com.daoyintech.daoyin_release.service.order.cart.LineItemService;
import com.daoyintech.daoyin_release.service.order.OrderService;
import com.daoyintech.daoyin_release.utils.WebUtil;
import com.daoyintech.daoyin_release.utils.qiniu.QiniuUploadTool;
import com.qiniu.storage.model.DefaultPutRet;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author pei on 2018/08/17
 */
@Service
public class BargainOrderServiceImpl implements BargainOrderService{

    @Autowired
    private BargainOrderRepository bargainOrderRepository;

    /**
     * 创建帮助订单
     */
    @Autowired
    private ProductFormatRepository formatRepository;

    @Autowired
    private ProductColorRepository colorRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LineItemService lineItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private QiniuUploadTool qiniuUploadTool;

    @Autowired
    private OrderLastOneHourHandlerSender orderLastOneHourHandlerSender;

    @Autowired
    private OrderSender orderSender;

    @Autowired
    private ExpireAndDeleteOrderSender expireAndDeleteOrderSender;

    @Autowired
    private BargainJoinerService bargainJoinerService;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public BargainOrder findByOrderId(Long orderId) {
        return bargainOrderRepository.findByOrderId(orderId);
    }

    @Transactional
    @Override
    public BargainOrder create(Long productId, User user, Long formatId, Long colorId) {
        ProductFormat format = formatRepository.getOne(formatId);
        ProductColor color = null;
        if (colorId != null) {
            color = colorRepository.getOne(colorId);
        }
        Product product = productRepository.getOne(productId);
        BargainOrder bo = bargainOrderRepository.findByProductIdAndUserIdAndStatus(productId,user.getId(), BargainOrderStatus.正在进行);
        if (bo != null){
            if (bo.getExpiredAt().after(new Date())){
                return bo;
            }
        }
        LineItem item = lineItemService.createLineItemByProductAndProductFormat(product, format, color);
        if (product.getInventory() - item.getQuantity() < 0) {
            return null;
        }
        product.setInventory(product.getInventory() - item.getQuantity());
        productRepository.save(product);

        Order order = orderService.createBargainOrder(item, user.getId());
        BargainOrder bargainOrder = new BargainOrder();
        bargainOrder.setOrderNo(WebUtil.noceStr());
        Long longExpiredAt  = System.currentTimeMillis()+ 1000 * 60 * 60;
        bargainOrder.setExpiredAt(new Date(longExpiredAt));
        bargainOrder.setOrderId(order.getId());
        bargainOrder.setQrCodeUrl(user.getQrCodeUrl());
        bargainOrder.setPrice(format.getSellPrice());
        bargainOrder.setProductId(productId);
        bargainOrder.setFormatId(formatId);
        bargainOrder.setColorId(colorId);
        bargainOrder.setUserId(user.getId());
        //TODO 属性含义
        //String qrCodeUrl = generateBargainOrderQrCodeUrl(bargainOrder);
        //bargainOrder.setQrCodeUrl(qrCodeUrl);
        BargainOrder saveBargainOrder = bargainOrderRepository.save(bargainOrder);
        //TODO 消息发送通知
        //orderLastOneHourHandlerSender.sendCheckOrderLastOneHourExpired(order.getOrderNo());
        orderSender.sendCheckOrderExpired(order.getOrderNo());
        return saveBargainOrder;
    }

    @Override
    public void orderPayedExpired(BargainOrder bargain, Order order) {
        bargain.setStatus(BargainOrderStatus.超时);
        bargainOrderRepository.save(bargain);
        order.setStatus(OrderStatus.等待支付);
        Order saveOrder = orderService.save(order);
        expireAndDeleteOrderSender.sendCheckOrderExpiredAndDelete(saveOrder.getOrderNo());
        //TODO 发送消息通知
        //wxBargainTemplateService.sendBargainOrderTimeEnd(bargain);
    }

    @Override
    public BargainOrder findOrderByOrderNo(String orderNo) {
        return bargainOrderRepository.findByOrderNo(orderNo);
    }

    @Override
    public BigDecimal calCutPrice(BargainOrder bargainOrder) {
        List<BargainJoiner> joiners = bargainJoinerService.getJoinersByBargainOrder(bargainOrder);
        if (joiners.size() == 0){
            return new BigDecimal(0);
        }
        for (BargainJoiner joiner: joiners) {
            if (BargainHelpType.getType(1).equals(joiner.getType())){
                return joiner.getBargainPrice();
            }
        }
        return new BigDecimal(0);
    }

    @Override
    public Order finishBargainOrder(String orderNo) {
        BargainOrder bargainOrder = findOrderByOrderNo(orderNo);
        Order order = orderService.findById(bargainOrder.getOrderId());
        if(bargainOrder.getStatus() == BargainOrderStatus.正在进行) {
            bargainOrder.setStatus(BargainOrderStatus.完成);
            bargainOrderRepository.save(bargainOrder);

            BigDecimal payMoney = lineItemService.findByOrderId(order.getId()).get(0).getPrice();
            if (order.getIsFreightFree() == false ){
                payMoney = payMoney.add(order.getFreightPrice());
            }
            order.setPayMoney(payMoney);
            order.setStatus(OrderStatus.等待支付);
            order = orderRepository.save(order);
            //rabbit 发送消息
            expireAndDeleteOrderSender.sendCheckOrderExpiredAndDelete(order.getOrderNo());
        }
        return order;
    }

    @Override
    public BargainOrder save(BargainOrder bargainOrder) {
        return bargainOrderRepository.save(bargainOrder);
    }

    @Override
    public BargainOrder findOne(Long bargainOrderId) {
        return bargainOrderRepository.findById(bargainOrderId).orElse(null);
    }


    public String generateBargainOrderQrCodeUrl(BargainOrder bargainOrder) {
        try {
            WxMpQrCodeTicket ticket =  wxMpService.getQrcodeService().qrCodeCreateTmpTicket("bargain" + "_" +bargainOrder.getOrderNo(), 24 * 60 * 60);
            File file = wxMpService.getQrcodeService().qrCodePicture(ticket);
            DefaultPutRet ret = qiniuUploadTool.upload(FileUtils.readFileToByteArray(file), "bargain/orders/" + bargainOrder.getOrderNo());
            return ret.key;
        } catch (WxErrorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
















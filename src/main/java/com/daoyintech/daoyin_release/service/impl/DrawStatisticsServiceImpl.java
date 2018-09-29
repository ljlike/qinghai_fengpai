package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.draw.Prize;
import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.repository.prize.PrizeRepository;
import com.daoyintech.daoyin_release.service.DrawStatisticsService;
import com.daoyintech.daoyin_release.service.order.cart.LineItemService;
import com.daoyintech.daoyin_release.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author pei on 2018/09/25
 */
@Slf4j
@Service
public class DrawStatisticsServiceImpl implements DrawStatisticsService {
    //售出总数量
    private static final String productSoldQuantity = "PRODUCT_SOLD_QUANTITY_";
    //售出总金额
    private static final String productSoldMoney = "PRODUCT_SOLD_MONEY_";

    @Autowired
    private LineItemService lineItemService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private PrizeRepository prizeRepository;

    @Autowired
    private WxMpService wxMpService;

    @Async
    @Override
    public synchronized void productStatistics(Order order) {
        //redis
        String redisKeyDate = DateUtils.getRedisKeyDate(new Date());
        //单个订单售出总数量,金额
        List<LineItem> itemList = lineItemService.findByOrderId(order.getId());
        Integer quantity = 0;
        for (LineItem lineItem : itemList) {
            quantity = quantity + lineItem.getQuantity();
        }
        //售出数量
        String quantityKey = productSoldQuantity + redisKeyDate;
        String keyData = redisTemplate.opsForValue().get(quantityKey);
        if (!StringUtils.isEmpty(keyData)){
            Integer soldQuantity = Integer.valueOf(keyData);
            quantity = quantity + soldQuantity;
        }
        redisTemplate.opsForValue().set(quantityKey,quantity.toString(),7, TimeUnit.DAYS);
        //售出金额
        BigDecimal orderPayMoney = order.getPayMoney();
        String moneyKey = productSoldMoney + redisKeyDate;
        if (!StringUtils.isEmpty(moneyKey)){
            BigDecimal money = new BigDecimal(moneyKey);
            orderPayMoney = orderPayMoney.add(money);
        }
        redisTemplate.opsForValue().set(moneyKey,orderPayMoney.toString(),7, TimeUnit.DAYS);
    }

    /**
     * 查询统计抽奖数据
     * */
    @Override
    public void drawStatistics(){
        String redisKeyDate = DateUtils.getRedisKeyDate(new Date());
        String quantityKey = productSoldQuantity + redisKeyDate;
        String moneyKey = productSoldMoney + redisKeyDate;
        String quantity = redisTemplate.opsForValue().get(quantityKey);
        String money = redisTemplate.opsForValue().get(moneyKey);
        if (StringUtils.isEmpty(quantity)){
            quantity = "0";
        }
        if (StringUtils.isEmpty(money)){
            money = "0";
        }
        StringBuffer bufferMsg = new StringBuffer(redisKeyDate).append("卖出商品(数量/件):").append(quantity).append("\r\n")
                .append("营业额(元):").append(money).append("\r\n");
        bufferMsg.append("奖池区间").append("\r\n");
        List<Prize> prizeList = prizeRepository.findAllByOrderByMaxPrizePersonsCountDesc();
        //奖池总积分  奖池剩余积分
        msgHandle(prizeList,bufferMsg);
        WxMpKefuMessage wxMpKefuMessage = new WxMpKefuMessage();
        wxMpKefuMessage.setToUser("o2e54wnx_WfeGc4Eskjiobfe88BI");
        wxMpKefuMessage.setMsgType(WxConsts.KefuMsgType.TEXT);
        wxMpKefuMessage.setContent(bufferMsg.toString());
        try {
            wxMpService.getKefuService().sendKefuMessage(wxMpKefuMessage);
        } catch (WxErrorException e) {
            log.error("发送抽奖统计积分数据失败:{}",e.getMessage());
        }
    }

    private void msgHandle(List<Prize> prizes, StringBuffer bufferMsg){
        Integer totalIntegral = 0;
        Integer totalResidueIntegral = 0;
        Integer totalDrawIntegral = 0;
        for (Prize prize : prizes) {
            totalIntegral = totalIntegral + prize.getTotalPrizePoint();
            int prizePoint = prize.getTotalPrizePoint() - prize.getTodayPrizePoint();
            totalResidueIntegral = totalResidueIntegral + prizePoint;
            totalDrawIntegral = totalDrawIntegral + prize.getTodayPrizePoint();
            bufferMsg.append(prize.getMin()).append("-").append(prize.getMax()).append("\r\n");
            bufferMsg.append("允许抽中的最大人数:").append(prize.getMaxPrizePersonsCount()).append("\r\n已经抽中的人数:").append(prize.getTodayPrizePersonsCount()).append("\r\n");
            bufferMsg.append("允许抽中的总积分:").append(prize.getTotalPrizePoint()).append("\r\n已经抽中的积分:").append(prize.getTodayPrizePoint()).append("\r\n");
        }
        bufferMsg.append("\r\n奖池总积分:").append(totalIntegral).append("\r\n已经抽走的总积分:").append(totalDrawIntegral).append("\r\n剩余的总积分:").append(totalResidueIntegral).append("\r\n");
    }





}









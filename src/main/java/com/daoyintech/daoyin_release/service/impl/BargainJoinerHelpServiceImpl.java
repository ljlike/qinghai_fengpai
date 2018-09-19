package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainJoiner;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.enums.bargain.BargainHelpType;
import com.daoyintech.daoyin_release.repository.order.BargainJoinerRepository;
import com.daoyintech.daoyin_release.service.BargainJoinerHelpService;
import com.daoyintech.daoyin_release.service.BargainOrderService;
import com.daoyintech.daoyin_release.service.LineItemService;
import com.daoyintech.daoyin_release.service.OrderService;
import com.daoyintech.daoyin_release.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author pei on 2018/08/20
 */
@Slf4j
@Service
public class BargainJoinerHelpServiceImpl implements BargainJoinerHelpService {

    @Autowired
    private BargainJoinerRepository joinerRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private LineItemService lineItemService;

    @Autowired
    private BargainOrderService bargainOrderService;

    @Value("${bargain_help.max_helper_count}")
    private int max_help_count; //最高可以参与砍价的人数

    /**
     * 当前砍价订单是否能够砍价
     * @param order 对应砍价订单
     * @return
     *  false为不能砍价 对应砍价人数已超过上限,自己不能参与自己的砍价,yi
     *  true 为可以砍价
     */
    @Override
    public Boolean isBargainCanHelp(BargainOrder order, User user) {
        if (user ==null){
            return false;
        }
        if(joinerRepository.existsByJoinerIdAndBargainOrderId(user.getId(),order.getId())){
            return false;
        }
        if(joinerRepository.countByBargainOrderId(order.getId()) >= max_help_count) {
            return false;
        }
        return true;
    }

    @Override
    public List<BargainJoiner> getJoinersByDate(BargainOrder order) {
        List<BargainJoiner> result = Arrays.asList(new BargainJoiner[12]);
        List<BargainJoiner> joiners = joinerRepository.findByBargainOrderIdOrderByCreatedAtAsc(order.getId());
        for(int i = 0; i < result.size(); i++) {
            BargainJoiner joiner = null;
            try {
                joiner = joiners.get(i);
            } catch (Exception e) {
            }finally {
                result.set(i, joiner);
            }
        }
        return result;
    }

    /**
     * 砍价的是否是订单发起人
     * @param order
     * @return
     */
    @Override
    public boolean isSelf(BargainOrder order, User user) {
        log.info("{}:砍价的是否是订单发起人:userId={}:sessionUserId={}", DateUtils.getStringDate(),order.getUserId(),user.getId());
        if (order.getUserId().equals(user.getId())) {
            return true;
        }
        return false;
    }

    /**
     * 是否达到最大砍价人数
     * @param order
     * @return
     */
    @Override
    public boolean isMaxHelpCount(BargainOrder order) {
        if(joinerRepository.countByBargainOrderId(order.getId()) >= max_help_count) {
            return true;
        }
        return false;
    }

    @Override
    public int checkisCanCatch(BargainOrder bargainOrder, User user) {
        if (user == null){
            return 1;
        }
        if (isMaxHelpCount(bargainOrder)){
            return 2;
        }
        if (isCatched(bargainOrder,user)){
            return 3;
        }
 /*       if (!user.getIsSubscribe()){
            return 4;
        }*/
        return 0;
    }

      /**
     * 参与砍价
     * @param order 对应砍价订单
     * @param user 对应参与用户
     */
   /* @Override
    public BargainJoiner bargainHelp(BargainOrder order, User user) {
        if(!isBargainCanHelp(order, user)) {
            return null;
        }
        List<BargainHelpType> types = calSurplusTypes(order);
        int typesIndex = Math.abs(new Random().nextInt() % types.size());
        BargainJoiner joiner = createJoiner(order,user,types.get(typesIndex));
        if(joiner != null){
            Long bargainUserId = orderService.findById(order.getOrderId()).getUserId();
            //TODO 消息通知
            // wxBargainTemplateService.sendBargainJoinInfoBeforePay(order, joiner);
            if (!bargainUserId.toString().equals(user.getId().toString())) {
                //TODO 消息通知
                //wxBargainTemplateService.sendBargainUserInfo(order, joiner);
            }
            if (joinerRepository.countByBargainOrderId(order.getId()) >= max_help_count) {
                bargainOrderService.finishBargainOrder(order.getOrderNo());
                //TODO 消息通知
                //wxBargainTemplateService.sendBargainOrderFinish(order);
            }
            return joiner;
        }
        return null;
    }*/

    /**
     * 参与砍价 小程序
     * @param bargainOrder 对应砍价订单
     * @param user 对应参与用户
     */
    @Override
    public synchronized BargainJoiner bargainDrawHelp(BargainOrder bargainOrder, User user) {
        if(!isBargainCanHelp(bargainOrder,user)) {
            return null;
        }
        List<BargainHelpType> types = drawCalSurplusTypes(bargainOrder);
        //随机一个索引
        int typesIndex = Math.abs(new Random().nextInt() % types.size());
        log.info("参与砍价:抽中的奖品类型:typesIndex = {}",types.get(typesIndex));
        BargainJoiner joiner = createJoiner(bargainOrder,user,types.get(typesIndex));
        if(joiner != null){
            if (joinerRepository.countByBargainOrderId(bargainOrder.getId()) >= max_help_count) {
                bargainOrderService.finishBargainOrder(bargainOrder.getOrderNo());
            }
        }
        return joiner;
    }

    /**
     * 奖品种类封装
     * */
    private List<BargainHelpType> drawCalSurplusTypes(BargainOrder bargainOrder) {
        List<BargainJoiner> joiners = joinerRepository.findByBargainOrderId(bargainOrder.getId());
        if (joiners == null || joiners.size() == 0){
            List<BargainHelpType> bargainHelpTypes = new ArrayList<>();
            bargainHelpTypes.add(BargainHelpType.getType(3));
            return bargainHelpTypes;
        }
        List<BargainHelpType> types = new ArrayList<>();
        for (BargainJoiner joiner : joiners) {
            types.add(joiner.getType());
        }
        List<BargainHelpType> bargainHelpTypes = new ArrayList<>();
//        bargainHelpTypes.add(BargainHelpType.getType(0));
        bargainHelpTypes.add(BargainHelpType.getType(3));
        bargainHelpTypes.add(BargainHelpType.getType(4));
        bargainHelpTypes.add(BargainHelpType.getType(5));
        bargainHelpTypes.removeAll(types);
        return bargainHelpTypes;
    }


/*    private List<BargainHelpType> calSurplusTypes(BargainOrder order){
        List<BargainJoiner> joiners = joinerRepository.findByBargainOrderId(order.getId());
        List<BargainHelpType> types = new ArrayList<>();
        for (BargainJoiner joiner:joiners) {
            types.add(joiner.getType());
        }
        List<BargainHelpType> bargainHelpTypes = new ArrayList<>();
        bargainHelpTypes.add(BargainHelpType.getType(0));
        bargainHelpTypes.add(BargainHelpType.getType(2));
        bargainHelpTypes.add(BargainHelpType.getType(3));
        bargainHelpTypes.add(BargainHelpType.getType(4));
        bargainHelpTypes.add(BargainHelpType.getType(5));
        bargainHelpTypes.removeAll(types);
        return bargainHelpTypes;
    }*/



    /**
     * 娃娃机创建参与者/抽奖参与者
     * @param bargainOrder
     * @param type
     * @param user
     */
    private BargainJoiner createJoiner(BargainOrder bargainOrder, User user,BargainHelpType type) {
        BargainJoiner joiner = new BargainJoiner();
        joiner.setAvatar(user.getAvatar());
        joiner.setBargainOrderId(bargainOrder.getId());
        joiner.setJoinerId(user.getId());
        joiner.setNickName(user.getNickName());
        joiner.setType(type);
        //todo 设置砍价订单的lineItem的price
        Order order = orderService.findById(bargainOrder.getOrderId());
        List<LineItem> items = lineItemService.findByOrderId(order.getId());
        if (BargainHelpType.getType(0).equals(type)) {
            bargainOrder.setHasColorEgg(true);
            bargainOrderService.save(bargainOrder);
            String joinerDescription = "好友的这单有神秘彩蛋哟！" ;
            joiner.setDescription(joinerDescription);
        }
        if (BargainHelpType.getType(1).equals(type)) {
            BigDecimal price = items.get(0).getPrice().multiply(new BigDecimal(0.05)).setScale(2,BigDecimal.ROUND_HALF_UP);
            joiner.setBargainPrice(price);
            lineItemService.updatePriceByBargainOrder(items.get(0).getId(),items.get(0).getPrice().subtract(price));
            String joinerDescription = "减减减！替好友节约￥ " + price ;
            joiner.setDescription(joinerDescription);
        }
        if (BargainHelpType.getType(2).equals(type)){
            order.setIsFreightFree(true);
            orderService.save(order);
            String joinerDescription = "好友的这单运费免啦!";
            joiner.setDescription(joinerDescription);
        }
        if (BargainHelpType.getType(3).equals(type)){
            String joinerDescription = "支付后您的好友将获得随机积分!";
            joiner.setDescription(joinerDescription);
        }
        if (BargainHelpType.getType(4).equals(type)){
            String joinerDescription = "好友可以找客服使用此券啦！";
            joiner.setDescription(joinerDescription);
        }
        if (BargainHelpType.getType(5).equals(type)){
            String joinerDescription = "好友可以找客服使用此券啦！";
            joiner.setDescription(joinerDescription);
        }
        BargainJoiner bargainJoiner = joinerRepository.save(joiner);
        log.info("创建参与者抽奖记录:BargainJoiner = {}:type = {}",joiner,type);
        return bargainJoiner;
    }


    /**
     * 是否已经抓过
     */
    public boolean isCatched(BargainOrder bargainOrder, User user){
        if (joinerRepository.existsByJoinerIdAndBargainOrderId(user.getId(),bargainOrder.getId())){
            return true;
        }
        return false;
    }


}

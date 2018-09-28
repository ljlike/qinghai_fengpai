package com.daoyintech.daoyin_release.service.impl.user;

import com.daoyintech.daoyin_release.entity.agent.AgentBean;
import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.user.*;
import com.daoyintech.daoyin_release.entity.user.integral.UserIntegral;
import com.daoyintech.daoyin_release.entity.user.integral.UserIntegralDetail;
import com.daoyintech.daoyin_release.enums.order.OrderStatus;
import com.daoyintech.daoyin_release.enums.user.UserIntegralFromType;
import com.daoyintech.daoyin_release.enums.user.UserProfitType;
import com.daoyintech.daoyin_release.repository.user.UserRelationshipRepository;
import com.daoyintech.daoyin_release.repository.user.UserRepository;
import com.daoyintech.daoyin_release.repository.agent.AgentRepository;
import com.daoyintech.daoyin_release.repository.user.UserProfitOrderRepository;
import com.daoyintech.daoyin_release.repository.user.UserProfitRepository;
import com.daoyintech.daoyin_release.response.member.MemberResponse;
import com.daoyintech.daoyin_release.service.order.OrderService;
import com.daoyintech.daoyin_release.service.order.cart.LineItemService;
import com.daoyintech.daoyin_release.service.user.*;
import com.daoyintech.daoyin_release.utils.DateUtil;
import com.daoyintech.daoyin_release.utils.DateUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pei on 2018/08/17
 */
@Slf4j
@Service
public class UserProfitServiceImpl implements UserProfitService {

    @Autowired
    private UserProfitRepository profitRepository;

    @Autowired
    private LineItemService lineItemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfitOrderRepository userProfitOrderRepository;

    @Autowired
    private UserRelationshipRepository relationshipRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserIntegralService userIntegralService;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserIntegralDetailService userIntegralDetailService;

    @Autowired
    private UserProfitAppletService userProfitAppletService;

    @Value("${integral.direct_profit_percent}")
    private String directProfitPercent;

    @Value("${integral.indirect_profit_percent}")
    private String indirectProfitPercent;

    @Value("${integral.integral_money_percent}")
    private BigDecimal integralMoneyPercent;

    @Value("${integral.team_person}")
    private int teamPerson;

    @Value("${integral.teamProfitPercent}")
    private String teamProfitPercent;

    @Value("${profit_percent}")
    private BigDecimal profitPercent;
    @Override
    @Async
    public void createProfitOrder(Order order) {
        List<UserProfit> profitList = profitRepository.findByFromUserId(order.getUserId());
        if (profitList.size() == 0 ){
            return;
        }
        List<LineItem> items = lineItemService.findByOrderId(order.getId());
        BigDecimal price = new BigDecimal(totalPrice(items));
        BigDecimal tempMoney = orderService.isFreightFee(order)?order.getPayMoney():order.getPayMoney().subtract(order.getFreightPrice());
        profitList.forEach((UserProfit item) -> {
            BigDecimal profitPercent = sclUserProfitPercent(item);
            BigDecimal profitMoney = (tempMoney).setScale(2,BigDecimal.ROUND_FLOOR).multiply(profitPercent).setScale(2,BigDecimal.ROUND_FLOOR);
            if (!isZeroMoney(profitMoney)) {
                UserProfitOrder profitOrder = new UserProfitOrder();
                profitOrder.setOrderMoney(price);
                profitOrder.setProfitMoney(profitMoney);
                profitOrder.setOrderNo(order.getOrderNo());
                profitOrder.setUserProfitId(item.getId());
                profitOrder.setType(UserProfitOrderType.小程序);
                UserProfitOrder userProfitOrder = userProfitOrderRepository.save(profitOrder);
                updateUserAgentPrice(item.getToUserId(), profitMoney);
                item.setTotalPrice(item.getTotalPrice().add(profitMoney));
                profitRepository.save(item);
                UserIntegral userIntegral = updateUserIntegralByProfit(item, profitMoney);
                createIntegralDetail(item, profitMoney, userProfitOrder,order);
                //模板消息通知
                //String time = DateUtil.DateToStr(new Date(), DateUtil.simple);
                //BigDecimal consumptionPrice = orderService.isFreightFee(order)? order.getPayMoney(): order.getPayMoney().subtract(order.getFreightPrice()).setScale(2,BigDecimal.ROUND_HALF_DOWN);
                //PerformanceTemplate performanceTemplate = new PerformanceTemplate(consumptionPrice.toString(), profitMoney.toString(), time);
                //User user = userRepository.getOne(item.getToUserId());
                //模板消息通知
                //performanceTemplate.sendMessage(wxMpService, user);
            }
        });
    }

    //从下级分润detail（公众号）
    private void createIntegralDetail(UserProfit userProfit,BigDecimal profitMoney,UserProfitOrder userProfitOrder,Order order){
        User  toUser = userRepository.getOne(userProfit.getToUserId());
        newUserIntegralDetail(toUser.getId(),profitMoney,order,userProfitOrder);
    }

    //从粉丝分润detail（小程序）
    private void createIntegralDetail(UserProfitApplet userProfitApplet,BigDecimal profitMoney,UserProfitOrder userProfitOrder,Order order){
        User  toUser = userRepository.getOne(userProfitApplet.getToUserId());
        newUserIntegralDetail(toUser.getId(),profitMoney,order,userProfitOrder);
    }

    private void newUserIntegralDetail(Long toUserId,BigDecimal profitMoney,Order order,UserProfitOrder userProfitOrder){
        UserIntegralDetail detail = new UserIntegralDetail();
        detail.setUserIntegralFromType(UserIntegralFromType.订单分润);
        detail.setUserId(toUserId);
        detail.setIntegral(profitMoney.divide(integralMoneyPercent,2,BigDecimal.ROUND_HALF_UP));
        detail.setOrderId(order.getId());
        detail.setUserProfitOrderNo(userProfitOrder.getOrderNo());
        userIntegralDetailService.save(detail);
    }


    private UserIntegral updateUserIntegralByProfit(UserProfit userProfit,BigDecimal profitMoney){
        User  toUser = userRepository.getOne(userProfit.getToUserId());
        log.info("{}:代理商用户积分变化", DateUtils.getStringDate());
        UserIntegral integral = userIntegralService.updateUserPoint(profitMoney.divide(integralMoneyPercent, 2, BigDecimal.ROUND_HALF_UP), toUser);
        return integral;
    }

    private UserIntegral updateUserIntegralByProfit(UserProfitApplet userProfitApplet,BigDecimal profitMoney){
        User  toUser = userRepository.getOne(userProfitApplet.getToUserId());
        log.info("{}:代理商用户积分变化", DateUtils.getStringDate());
        UserIntegral integral = userIntegralService.updateUserPoint(profitMoney.divide(integralMoneyPercent, 2, BigDecimal.ROUND_HALF_UP), toUser);
        return integral;
    }

    private void updateUserAgentPrice(Long userId, BigDecimal price) {
        AgentBean agentBean = agentRepository.findByUserId(userId);
        if (agentBean != null) {
            log.info("{}:分润计算前:userId = {} performanceMoney = ",userId,agentBean.getPerformanceMoney()+" 变动金额 = "+price.doubleValue());
            agentBean.setPerformanceMoney(agentBean.getPerformanceMoney() + price.doubleValue());
            log.info("{}:分润计算后:userId = {} performanceMoney = ",userId,agentBean.getPerformanceMoney());
            agentRepository.save(agentBean);
        }
    }


    private boolean isZeroMoney(BigDecimal profitMoney){
        if (profitMoney.equals(new BigDecimal(0))){
            return true;
        }
        return false;
    }

    private BigDecimal sclUserProfitPercent(UserProfit userProfit){
        List<UserProfit> userProfitChildsForParent = findByToUserIdAndUserProfitTypeExceptMyself(userProfit.getToUserId(),UserProfitType.direct);
        Integer count = directPartners(userProfitChildsForParent).size();
        if ("direct".equals(userProfit.getUserProfitType().toString())){
            if (count >= teamPerson){
                return new BigDecimal(directProfitPercent).add(new BigDecimal(teamProfitPercent));
            }
            return new BigDecimal(directProfitPercent);
        }
        if("indirect".equals(userProfit.getUserProfitType().toString())){
            if (count >= teamPerson){
                return new BigDecimal(directProfitPercent).add(new BigDecimal(teamProfitPercent));
            }
            return new BigDecimal(directProfitPercent);
        }
        if("indirectThree".equals(userProfit.getUserProfitType().toString())){
            return new BigDecimal(teamProfitPercent);
        }
        return new BigDecimal(0);
    }

    @Async
    @Override
    public void createProfitUser(User user, User parent, UserProfitType userProfitType) {
        UserProfit profit = profitRepository.findByFromUserIdAndToUserId(user.getId(), parent.getId());
        if (profit == null) {
            profit = new UserProfit();
            profit.setFromUserId(user.getId());
            profit.setToUserId(parent.getId());
            profit.setUserProfitType(userProfitType);
            profitRepository.save(profit);
        }
    }

    @Override
    public void createProfitUserByGrandFather(User child, User parent,UserProfitType profitType) {
        UserRelationship ship = relationshipRepository.findByChildUserId(parent.getId());
        if (ship != null) {
            User user = userRepository.getOne(ship.getParentId());
            if (user != null && user.getIsAgent()) {
                createProfitUser(child, user,profitType);
                UserRelationship userRelationship = relationshipRepository.findByChildUserId(user.getId());
                User parentParent = userRepository.getOne(userRelationship.getParentId());

                List<UserProfit> userProfitList = findByToUserIdAndUserProfitTypeExceptMyself(parentParent.getId(),UserProfitType.direct);
                List<UserProfit> userProfits = directPartners(userProfitList);
                if (parent != null && parent.getIsAgent() && userProfits.size() >= teamPerson) {
                    createProfitUser(child, parentParent, UserProfitType.indirectThree);
                }
            }
        }

    }

    /**
     * 获取直招的合伙人
     * @param directProfits
     * @return
     */
    public List<UserProfit> directPartners(List<UserProfit> directProfits){
        List<UserProfit> partners = new ArrayList<>();
        for (int i = 0; i <directProfits.size() ; i++) {
            User user = userRepository.getOne(directProfits.get(i).getFromUserId());
            if (user.getIsAgent()) {
                partners.add(directProfits.get(i));
            }
        }
        return partners;
    }

    @Override
    public List<MemberResponse> getMembers(List<UserProfit> userProfits) {
        return Lists.transform(userProfits, userProfit -> {
            MemberResponse response = new MemberResponse();
            User fromUser = userRepository.getOne(userProfit.getFromUserId());
            response.setId(userProfit.getId());
            response.setAvatar(fromUser.getAvatar());
           /* String nickname = null;
            try {
                nickname = URLDecoder.decode(fromUser.getNickName(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                log.error("{}:nickname编码错误:{}",DateUtils.getStringDate(),e.getMessage());
            }*/
            response.setName(fromUser.getNickName());
            BigDecimal salesAmount = calSalesAmount(fromUser,userProfit);
            BigDecimal integral = calIntegral(fromUser,userProfit);
            response.setSalesAmount(salesAmount);
            if(integral.doubleValue() == 0.00){
                response.setIntegral(new BigDecimal(0));
            }else {
                response.setIntegral(integral);
            }
            List<UserProfit> directProfits = findByToUserIdAndUserProfitTypeExceptMyself(fromUser.getId(), UserProfitType.direct);
            List<UserProfit> directPartnersProfits = directPartners(directProfits);
            response.setSubPartnersCount(directPartnersProfits.size());
            return response;
        });
    }

    /**
     * 获取直招的普通会员
     * @param directProfits
     * @return
     */
    @Override
    public List<UserProfit> directMembers(List<UserProfit> directProfits) {
        List<UserProfit> members = new ArrayList<>();
        for (int i = 0; i < directProfits.size() ; i++) {
            User user = userRepository.getOne(directProfits.get(i).getFromUserId());
            if (!user.getIsAgent()) {
                members.add(directProfits.get(i));
            }
        }
        return members;
    }

    @Override
    public BigDecimal calIntegral(User fromUser,UserProfit userProfit){
        BigDecimal totalProfitMoney = totalIntegralInMonth(userProfit);
        if (fromUser.getIsAgent()){
            List<UserProfit> userProfitsToUser = findByToUser(userProfit.getFromUserId());
            for (int i = 0; i <userProfitsToUser.size() ; i++) {
                totalProfitMoney = totalProfitMoney.add(totalIntegralInMonth(userProfitsToUser.get(i)));
            }
        }
        return totalProfitMoney.divide(integralMoneyPercent,2,BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public List<UserProfit> findByToUserId(Long fromUserId) {
        List<UserProfit> userProfits = profitRepository.findByToUserId(fromUserId);
        return userProfits;
    }

    @Override
    public List<UserProfit> removeNotAgent(List<UserProfit> userProfits) {
        for (int i = 0; i < userProfits.size(); i++) {
            boolean isAgent  = userService.findById(userProfits.get(i).getFromUserId()).getIsAgent();
            if (!isAgent) {
                userProfits.remove(userProfits.get(i));
                i--;
            }
        }
        return userProfits;
    }

    @Override
    public UserProfit findByToUserIdAndFromUserId(Long toUserId, Long fromUserId) {
        return profitRepository.findByFromUserIdAndToUserId(toUserId,fromUserId);
    }

    public BigDecimal totalIntegralInMonth(UserProfit userProfit){
        List<UserProfitOrder> orders = userProfitOrderRepository.findByUserProfitIdAndCreatedAtAfterAndStatus(userProfit.getId(),DateUtil.getFirstDayByMonth(),0);
        BigDecimal totalProfitMoney = new BigDecimal(0);
        for (UserProfitOrder order:orders) {
            totalProfitMoney = totalProfitMoney.add(order.getProfitMoney());
        }
        return totalProfitMoney;
    }

    @Override
    public BigDecimal calSalesAmount(User fromUser,UserProfit userProfit){
        BigDecimal salesAmount = totalSalesAmountInMonth(userProfit);
        if (fromUser.getIsAgent()){
            List<UserProfit> userProfitsToUser = findByToUser(userProfit.getFromUserId());
            for (int i = 0; i <userProfitsToUser.size() ; i++) {
                salesAmount = salesAmount.add(totalSalesAmountInMonth(userProfitsToUser.get(i)));
            }
        }
        return salesAmount;
    }
    public BigDecimal totalSalesAmountInMonth(UserProfit userProfit){
        List<UserProfitOrder> orders = userProfitOrderRepository.findByUserProfitIdAndCreatedAtAfterAndStatus(userProfit.getId(), DateUtil.getFirstDayByMonth(),0);
        BigDecimal totalMoney = new BigDecimal(0);
        for (UserProfitOrder order:orders) {
            totalMoney = totalMoney.add(order.getOrderMoney());
        }
        return totalMoney;
    }
    public List<UserProfit> findByToUser(Long fromUserId){
        List<UserProfit> userProfits = profitRepository.findByToUserId(fromUserId);
        return userProfits;
    }




    public List<UserProfit> findByToUserIdAndUserProfitTypeExceptMyself(Long  toUserId,UserProfitType type){
        List<UserProfit> userProfits = profitRepository.findByToUserIdAndUserProfitType(toUserId,type);
        for (int i = 0; i < userProfits.size(); i++) {
            if (userProfits.get(i).getFromUserId().equals(toUserId)){
                userProfits.remove(userProfits.get(i));
                i--;
            }
        }
        return userProfits;
    }

    private Double totalPrice(List<LineItem> items){
        Double sum = 0.0;
        for(int i = 0;i < items.size(); i++){
            LineItem lineItem = items.get(i);
            sum += lineItem.totalPrice();
        }
        return sum;
    }

    /**
     * 订单分润
     * @param order
     */
    @Async
    public void handleProfit(Order order){
        if (order.getStatus().equals(OrderStatus.待发货)) {
            createProfitOrderWithApplet(order);
        }
    }

    public void  createProfitOrderWithApplet(Order order){
        UserProfitApplet userProfitApplet = userProfitAppletService.findByFromUserId(order.getUserId());
        if (userProfitApplet == null || !userProfitApplet.getIsProfit()){
            return;
        }
        List<LineItem> items = lineItemService.findByOrderId(order.getId());
        BigDecimal price = new BigDecimal(totalPrice(items));
        BigDecimal tempMoney = orderService.isFreightFee(order)?order.getPayMoney():order.getPayMoney().subtract(order.getFreightPrice());

        BigDecimal profitMoney = tempMoney.multiply(profitPercent).setScale(2,BigDecimal.ROUND_FLOOR);
        if (profitMoney.doubleValue() == 0D){
            return;
        }
        UserProfitOrder userProfitOrder = new UserProfitOrder();
        userProfitOrder.setOrderMoney(price);
        userProfitOrder.setProfitMoney(profitMoney);
        userProfitOrder.setOrderNo(order.getOrderNo());
        userProfitOrder.setUserProfitAppletId(userProfitApplet.getId());
        userProfitOrder.setType(UserProfitOrderType.小程序);
        userProfitOrderRepository.save(userProfitOrder);

        updateUserAgentPrice(userProfitApplet.getToUserId(), profitMoney);  //修改代理商业绩
        userProfitApplet.setTotalPrice(userProfitApplet.getTotalPrice().add(profitMoney)); //修改为我带来的积分
        userProfitAppletService.save(userProfitApplet);
        updateUserIntegralByProfit(userProfitApplet, profitMoney);//修改用户积分

        createIntegralDetail(userProfitApplet, profitMoney, userProfitOrder,order); //创建积分记录
    }


}

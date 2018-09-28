package com.daoyintech.daoyin_release.service.impl.user;

import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.entity.user.UserProfitApplet;
import com.daoyintech.daoyin_release.entity.user.UserProfitOrder;
import com.daoyintech.daoyin_release.repository.user.UserProfitAppletRepository;
import com.daoyintech.daoyin_release.repository.user.UserProfitOrderRepository;
import com.daoyintech.daoyin_release.repository.user.UserRepository;
import com.daoyintech.daoyin_release.response.member.MemberResponse;
import com.daoyintech.daoyin_release.service.user.UserProfitAppletService;
import com.daoyintech.daoyin_release.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserProfitAppletServiceImpl implements UserProfitAppletService {

    @Autowired
    private UserProfitAppletRepository userProfitAppletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfitOrderRepository userProfitOrderRepository;

    @Value("${integral.integral_money_percent}")
    private BigDecimal integralMoneyPercent;

    @Override
    public UserProfitApplet findByFromUserId(Long fromUserId) {
        return userProfitAppletRepository.findByFromUserId(fromUserId);
    }

    @Override
    public List<UserProfitApplet> findByToUserId(Long toUserId) {
        return userProfitAppletRepository.findByToUserId(toUserId);
    }

    @Override
    public UserProfitApplet save(UserProfitApplet userProfitApplet) {
         userProfitAppletRepository.save(userProfitApplet);
         return userProfitApplet;
    }

    @Override
    public List<UserProfitApplet> findByToUserIdAndIsProfit(Long id, Boolean isProfit) {
        return userProfitAppletRepository.findByToUserIdAndIsProfit(id,isProfit);
    }

    @Override
    public List<MemberResponse> userProfitAppletToMemberResponse(List<UserProfitApplet> directProfits) {
        List<MemberResponse> memberResponses = new ArrayList<>();
        for (UserProfitApplet userProfitApplet : directProfits) {
            User fromUser = userRepository.getOne(userProfitApplet.getFromUserId());
            MemberResponse memberResponse = new MemberResponse();
            memberResponse.setId(userProfitApplet.getId());
            memberResponse.setAvatar(fromUser.getAvatar());
            memberResponse.setName(fromUser.getNickName());
            BigDecimal salesAmount = calSalesAmount(fromUser,userProfitApplet);
            BigDecimal integral = calIntegral(fromUser,userProfitApplet);
            memberResponse.setSalesAmount(salesAmount);
            if(integral.doubleValue() == 0.00){
                memberResponse.setIntegral(new BigDecimal(0));
            }else {
                memberResponse.setIntegral(integral);
            }
            List<UserProfitApplet> userProfitApplets = findByToUserIdAndIsProfit(fromUser.getId(), Boolean.TRUE);
            memberResponse.setSubPartnersCount(userProfitApplets.size());
            memberResponses.add(memberResponse);
        }
        return memberResponses;

    }
    public BigDecimal calSalesAmount(User fromUser,UserProfitApplet userProfit){
        BigDecimal salesAmount = totalSalesAmountInMonth(userProfit);
        if (fromUser.getIsAgent()){
            List<UserProfitApplet> userProfitsToUser = findByToUser(userProfit.getFromUserId());
            for (int i = 0; i <userProfitsToUser.size() ; i++) {
                salesAmount = salesAmount.add(totalSalesAmountInMonth(userProfitsToUser.get(i)));
            }
        }
        return salesAmount;
    }
    public BigDecimal totalSalesAmountInMonth(UserProfitApplet userProfit){
        List<UserProfitOrder> orders = userProfitOrderRepository.findByUserProfitIdAndCreatedAtAfterAndStatus(userProfit.getId(), DateUtil.getFirstDayByMonth(),0);
        BigDecimal totalMoney = new BigDecimal(0);
        for (UserProfitOrder order:orders) {
            totalMoney = totalMoney.add(order.getOrderMoney());
        }
        return totalMoney;
    }



    public BigDecimal calIntegral(User fromUser, UserProfitApplet userProfitApplet){
        BigDecimal totalProfitMoney = totalIntegralInMonth(userProfitApplet);
        if (fromUser.getIsAgent()){
            List<UserProfitApplet> userProfitsToUser = findByToUser(userProfitApplet.getFromUserId());
            for (int i = 0; i <userProfitsToUser.size() ; i++) {
                totalProfitMoney = totalProfitMoney.add(totalIntegralInMonth(userProfitsToUser.get(i)));
            }
        }
        return totalProfitMoney.divide(integralMoneyPercent,2,BigDecimal.ROUND_HALF_UP);
    }
    public List<UserProfitApplet> findByToUser(Long fromUserId){
        List<UserProfitApplet> userProfits = userProfitAppletRepository.findByToUserId(fromUserId);
        return userProfits;
    }

    public BigDecimal totalIntegralInMonth(UserProfitApplet userProfitApplet){
        List<UserProfitOrder> orders = userProfitOrderRepository.findByUserProfitAppletIdAndCreatedAtAfterAndStatus(userProfitApplet.getId(), DateUtil.getFirstDayByMonth(),0);
        BigDecimal totalProfitMoney = new BigDecimal(0);
        for (UserProfitOrder order:orders) {
            totalProfitMoney = totalProfitMoney.add(order.getProfitMoney());
        }
        return totalProfitMoney;
    }



}

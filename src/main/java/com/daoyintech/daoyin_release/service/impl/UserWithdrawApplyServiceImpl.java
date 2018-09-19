package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.agent.UserWithdraw;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.enums.agent.WithdrawStatus;
import com.daoyintech.daoyin_release.enums.agent.WithdrawType;
import com.daoyintech.daoyin_release.repository.user.UserRepository;
import com.daoyintech.daoyin_release.repository.user.UserWithdrawRepository;
import com.daoyintech.daoyin_release.service.UserIntegralService;
import com.daoyintech.daoyin_release.service.UserWithdrawApplyService;
import com.daoyintech.daoyin_release.service.UserWithdrawService;
import com.daoyintech.daoyin_release.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author pei on 2018/08/22
 */
@Service
public class UserWithdrawApplyServiceImpl implements UserWithdrawApplyService {

    @Value("${integral.integral_money_percent}")
    private BigDecimal integralMoneyPercent;

    @Value("${integral.autoWithdrawIntegralLine}")
    private Double autoWithdrawIntegralLine;

    @Autowired
    private UserWithdrawRepository userWithdrawRepository;

    @Autowired
    private UserIntegralService userIntegralService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserWithdrawService userWithdrawService;

    //创建提现申请
    @Transactional
    @Override
    public void createWithdraw(User user, BigDecimal point) {
        UserWithdraw userWithdraw = new UserWithdraw();
        userWithdraw.setUserId(user.getId());
        userWithdraw.setIntegral(point.doubleValue());
        userWithdraw.setMoney(transformPointByAgentLevel(point, user.getAgentLevelId()));
        String tradeNo = "UW" + DateUtil.dateStr() + user.getId();
        userWithdraw.setTradeNo(tradeNo);
        UserWithdraw withdraw = userWithdrawRepository.save(userWithdraw);
        userIntegralService.updateUserPoint(new BigDecimal(withdraw.getIntegral()).multiply(new BigDecimal(-1)),user);
        if (withdraw.getIntegral() > autoWithdrawIntegralLine) {
            withdraw.setStatus(WithdrawStatus.已受理);
            userWithdrawService.save(withdraw);
        }else {
            boolean isSuccess =  userWithdrawService.withdraw(withdraw,user);
            handleWithdrawAfter(isSuccess,withdraw);
        }
    }


    public void handleWithdrawAfter(Boolean isSuccess,UserWithdraw userWithdraw){
        if (!isSuccess) {
            userWithdraw.setStatus(WithdrawStatus.提现失败);
            userWithdraw.setType(WithdrawType.零钱);
            userWithdraw.setContent("提现失败");
            userWithdrawService.save(userWithdraw);
            //TODO 提现消息通知
            //wxTemplateService.sendWithdrawMessage(userWithdraw, userWithdraw.getUser());
        }else {
            userWithdraw.setStatus(WithdrawStatus.到账成功);
            userWithdraw.setType(WithdrawType.零钱);
            userWithdraw.setSuccessTime(new Date());
            userWithdrawService.save(userWithdraw);
            //TODO 提现消息通知
            //wxTemplateService.sendWithdrawMessage(userWithdraw, userWithdraw.getUser());
        }
    }


    private Double transformPointByAgentLevel(BigDecimal point, Long agentLevelId) {
        Double withdrawMoney = point.multiply(integralMoneyPercent).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
        //银行手续费
        Double BankFee = calBankFee(withdrawMoney);
        return withdrawMoney;
    }

    private Double calBankFee(Double withdrawMoney){
        Double commonFee =  withdrawMoney * 0.001;
        if (commonFee < 1 ){
            commonFee = 1D;
        }
        if (commonFee > 25) {
            commonFee = 25D;
        }
        return commonFee;
    }



}

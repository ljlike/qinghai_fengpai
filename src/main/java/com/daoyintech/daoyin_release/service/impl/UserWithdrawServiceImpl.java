package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.agent.AgentBean;
import com.daoyintech.daoyin_release.entity.agent.UserWithdraw;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.entity.user.integral.UserIntegral;
import com.daoyintech.daoyin_release.enums.agent.ApplyStatus;
import com.daoyintech.daoyin_release.repository.user.UserIntegralRepository;
import com.daoyintech.daoyin_release.repository.user.UserWithdrawRepository;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.agent.UserWithdrawRecordResponse;
import com.daoyintech.daoyin_release.response.agent.UserWithdrawResponse;
import com.daoyintech.daoyin_release.service.AgentService;
import com.daoyintech.daoyin_release.service.UserIntegralService;
import com.daoyintech.daoyin_release.service.UserWithdrawService;
import com.daoyintech.daoyin_release.utils.DateUtils;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author pei on 2018/08/11
 */
@Slf4j
@Service
public class UserWithdrawServiceImpl implements UserWithdrawService {

    @Autowired
    private UserIntegralRepository userIntegralRepository;

    @Autowired
    private UserWithdrawRepository userWithdrawRepository;

    @Autowired
    private UserIntegralService userIntegralService;

    @Value("${integral.integral_money_percent}")
    private BigDecimal integralMoneyPercent;

    @Value("${wx.appId}")
    private String appId;

    @Value("${wxPay.mchId}")
    private String mchId;

    @Value("${wx.spbill_create_ip}")
    private String spbillCreateIp;



    @Autowired
    private AgentService agentService;

    @Autowired
    private WxPayService wxPayService;

    @Override
    public ResultResponse findAllDetailAndBalance(User user) {
        UserIntegral userIntegral = userIntegralRepository.findByUserId(user.getId());
        List<UserWithdraw> userWithdraws = userWithdrawRepository.findByUserId(user.getId());
        List<UserWithdrawRecordResponse> userWithdrawResponseList = new ArrayList<>();
        if (userWithdraws.size() != 0){
            userWithdraws.forEach(uw ->{
                UserWithdrawRecordResponse userWithdrawRecordResponse = UserWithdrawRecordResponse.buildWithdrawResponse(uw);
                userWithdrawResponseList.add(userWithdrawRecordResponse);
            });
        }
        UserWithdrawResponse userWithdrawResponse = new UserWithdrawResponse();
        userWithdrawResponse.setBalance(userIntegral.getIntegral());
        userWithdrawResponse.setPercent(integralMoneyPercent);
        userWithdrawResponse.setRecords(userWithdrawResponseList);
        return ResultResponseUtil.success(userWithdrawResponse);
    }

    @Override
    public List<UserWithdraw> findByCreateAt(Long userId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        return userWithdrawRepository.findAllByUserIdAndCreatedAtAfter(userId,zero);
    }

    @Override
    public UserWithdraw save(UserWithdraw withdraw) {
        return userWithdrawRepository.save(withdraw);
    }

    /**
     * 提现到微信
     *
     * @param withdraw
     * @param user
     * @return
     */
    @Async
    @Synchronized
    @Override
    public boolean withdraw(UserWithdraw withdraw, User user) {
        EntPayRequest entPayRequest = new EntPayRequest();
        entPayRequest.setAppid(appId);
        entPayRequest.setMchId(mchId);
        entPayRequest.setOpenid(user.getAppletOpenId());
        entPayRequest.setPartnerTradeNo(withdraw.getTradeNo());
        String price = BigDecimal.valueOf(withdraw.getMoney()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        entPayRequest.setAmount(EntPayRequest.yuanToFen(price));
        entPayRequest.setCheckName("NO_CHECK");
        AgentBean agentBean = agentService.findByUserIdAndIsSuccess(user, ApplyStatus.准合伙人);
        if (agentBean != null && ApplyStatus.准合伙人.equals(agentBean.getIsSuccess())) {
            entPayRequest.setReUserName(agentBean.getName());
        }
        entPayRequest.setDescription("转账" + withdraw.getMoney().toString() + "元");
        entPayRequest.setSpbillCreateIp(spbillCreateIp);
        log.info("{}:entPayRequest:{}", DateUtils.getStringDate(),entPayRequest);
        try {
            EntPayResult entPayResult = wxPayService.getEntPayService().entPay(entPayRequest);
            return entPayResult.getResultCode().equals("SUCCESS") ? true : false;
        } catch (Exception e) {
           log.error("{}:提现错误：{}",DateUtils.getStringDate(),e.getMessage());
           userIntegralService.updateUserPoint(new BigDecimal(withdraw.getIntegral()),user);
        }
        return false;
    }

}














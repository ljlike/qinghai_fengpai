package com.daoyintech.daoyin_release.controller.customer.agent;

import com.daoyintech.daoyin_release.controller.customer.user.BaseUserController;
import com.daoyintech.daoyin_release.entity.agent.UserWithdraw;
import com.daoyintech.daoyin_release.entity.bank.Bank;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.enums.ResultEnum;
import com.daoyintech.daoyin_release.enums.agent.WithdrawStatus;
import com.daoyintech.daoyin_release.enums.setting.OverallSettingStatus;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.bank.BankCardInfoRequest;
import com.daoyintech.daoyin_release.service.*;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/agentWithdraw")
@RestController
public class AgentWithdrawController extends BaseUserController{


    @Autowired
    private UserService userService;

    @Autowired
    private UserWithdrawService userWithdrawService;

    @Autowired
    private BankService bankService;
    @Autowired
    private OverallSettingService overallSettingService;

    @Autowired
    private UserIntegralService userIntegralService;

    @Autowired
    private UserWithdrawApplyService userWithdrawApplyService;

    @Value("${integral.min_withdraw_money}")
    private Double minWithdrawMoney;

    @Value("${integral.maxWithdrawOneDay}")
    private Double maxWithdrawOneDay;

    @Value("${integral.integral_money_percent}")
    private BigDecimal integralMoneyPercent;

    /**
     * 查询提现日志与可提现余额
     * @return
     */
    @ApiOperation("查询提现日志与可提现余额")
    @GetMapping("/findAllDetailAndBalance")
    public ResultResponse findAll(){
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.PARAM_ERROR.getCode(),"登录失效,请重新登录!");
        }
        User user = userService.findByUnionId(unionId);
        return userWithdrawService.findAllDetailAndBalance(user);
    }


    @ApiOperation("申请提现")
    @PostMapping("{integral}/withdraw")
    public ResultResponse withDraw(@PathVariable(name = "integral")String integral){
        BigDecimal point;
        try {
            point = new BigDecimal(integral);
        } catch (Exception e) {
            return ResultResponseUtil.error(ResultEnum.PARAM_ERROR.getCode(),"输入的积分不符合格式!");
        }
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        BigDecimal currentPoint = userIntegralService.findByUserId(user.getId()).getIntegral();
        /*if(OverallSettingStatus.off.equals(overallSettingService.findByName("提现").getStatus())) {
            return ResultResponseUtil.error(ResultEnum.USER_ERROR.getCode(),"当前尚未开启提现");
        }*/
        if(currentPoint.doubleValue() < point.doubleValue()) {
            return ResultResponseUtil.error(ResultEnum.USER_ERROR.getCode(),"积分余额不足");
        }
        if (currentPoint.doubleValue() < minWithdrawMoney){
            return ResultResponseUtil.error(ResultEnum.USER_ERROR.getCode(),"至少提现" + minWithdrawMoney + "积分");
        }
        /*if (user.getBankCardNum() == null || user.getOpenBank() == null){
            return ResultResponseUtil.error(ResultEnum.USER_ERROR.getCode(),"请先绑定银行卡");
        }*/
        if (point.doubleValue() > 50000D){
            return ResultResponseUtil.error(ResultEnum.USER_ERROR.getCode(),"每单提现限额5万元");
        }
        List<UserWithdraw> userWithdraws = userWithdrawService.findByCreateAt(user.getId());
        Double sum = 0.00;
        for (UserWithdraw userWithdraw : userWithdraws) {
            if (userWithdraw.getStatus().equals(WithdrawStatus.到账成功)){
                sum += userWithdraw.getMoney();
            }
        }
        if (sum + point.multiply(integralMoneyPercent).doubleValue()> maxWithdrawOneDay){
            return ResultResponseUtil.error(ResultEnum.USER_ERROR.getCode(),"每天提现限额"+maxWithdrawOneDay+"元,你今天还可以提现"+(maxWithdrawOneDay-sum)+"元");
        }
        userWithdrawApplyService.createWithdraw(user, point);
        return ResultResponseUtil.success("您的申请已提交,请耐心等待");
    }




    /*    @ApiOperation("查询所有银行")
    @GetMapping("/bank")
    public ResultResponse findAllBank(){
        List<Bank> banks = bankService.findAll();
        return ResultResponseUtil.success(banks);
    }*/


/*
    @ApiOperation("修改提现信息")
    @PostMapping("/bankCardInfo")
    public ResultResponse updateOrCreate(@RequestBody BankCardInfoRequest bankCardInfo){
        if (StringUtils.isEmpty(bankCardInfo.getBankCardNum()) || StringUtils.isEmpty(bankCardInfo.getHoldCardName()) || StringUtils.isEmpty(bankCardInfo.getOpenBank())){
            return ResultResponseUtil.error(ResultEnum.PARAM_ERROR.getCode(),"请正确填写银行卡信息!");
        }
        User user = userService.findByUnionId(getCurrentUnionId());
        userService.UpdateOrCreateBankCardInfo(bankCardInfo,user);
        return ResultResponseUtil.success("绑定银行卡信息成功！");
    }
*/



}




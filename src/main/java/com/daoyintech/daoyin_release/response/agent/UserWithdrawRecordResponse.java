package com.daoyintech.daoyin_release.response.agent;

import com.daoyintech.daoyin_release.entity.agent.UserWithdraw;
import lombok.Data;

import java.util.Date;

/**
 * @author pei on 2018/08/13
 */
@Data
public class UserWithdrawRecordResponse {

    private Long createdAt;

    private Double changeIntegral; //提现积分

    private Double changeMoney; //提现金额

    private int status;

    private String content;//拒绝理由

    public static UserWithdrawRecordResponse buildWithdrawResponse(UserWithdraw userWithdraw){
        UserWithdrawRecordResponse userWithdrawRecordResponse = new UserWithdrawRecordResponse();
        userWithdrawRecordResponse.setChangeIntegral(userWithdraw.getIntegral());
        userWithdrawRecordResponse.setContent(userWithdraw.getContent());
        userWithdrawRecordResponse.setStatus(userWithdraw.getStatus().ordinal());
        userWithdrawRecordResponse.setCreatedAt(userWithdraw.getCreatedAt().getTime());
        userWithdrawRecordResponse.setChangeMoney(userWithdraw.getMoney());
        return userWithdrawRecordResponse;
    }





}

package com.daoyintech.daoyin_release.response.agent;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author pei on 2018/08/13
 */
@Data
public class UserWithdrawResponse {

    private BigDecimal balance;

    private BigDecimal percent;

    private List<UserWithdrawRecordResponse> records;

}










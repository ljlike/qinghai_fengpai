package com.daoyintech.daoyin_release.response.draw;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author pei on 2018/09/10
 */
@Data
public class DrawRecordResponse {

    private String userName;

    private String userHeadUrl;

    private String productName;

    private BigDecimal payMoney;

    private String productUrl;

    private BigDecimal cardIntegral;

    private List<JoinerUserResponse> joinerUserResponseList;

}

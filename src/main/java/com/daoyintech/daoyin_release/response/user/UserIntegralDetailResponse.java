package com.daoyintech.daoyin_release.response.user;
import com.daoyintech.daoyin_release.enums.user.UserIntegralFromType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author pei on 2018/08/13
 */
@Data
public class UserIntegralDetailResponse {

    private Long createdAt;

    private Long orderId;

    private Long userId;

    private BigDecimal Integral = new BigDecimal(0);

    private String userIntegralFromType = UserIntegralFromType.帮助朋友.toString();

}






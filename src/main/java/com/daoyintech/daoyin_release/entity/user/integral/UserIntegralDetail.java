package com.daoyintech.daoyin_release.entity.user.integral;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.enums.user.UserIntegralFromType;
import com.daoyintech.daoyin_release.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;

@Slf4j
@Data
@Entity
@Table(name = "user_integral_detail")
@EqualsAndHashCode(callSuper = true)
public class UserIntegralDetail extends AbstractEntity {

    @Column
    private Long  orderId;

    @Column
    private Long userId;

    @Column
    private BigDecimal Integral = new BigDecimal(0);

    @Column
    @Enumerated(EnumType.STRING)
    private UserIntegralFromType userIntegralFromType = UserIntegralFromType.帮助朋友;

    @Column
    private String userProfitOrderNo;


    public String intergralFromLabel(User user) {
        /*String nickname = null;
        try {
            nickname = URLDecoder.decode(user.getNickName(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("{}:nickname编码错误:{}", DateUtils.getStringDate(),e.getMessage());
        }*/
        if (this.userIntegralFromType == UserIntegralFromType.帮助朋友) {
            return "帮助"+user.getNickName();
        } else if (this.userIntegralFromType == UserIntegralFromType.订单分润) {
            return "从" + user.getNickName() + "的订单中分的";
        } else {
            return this.userIntegralFromType.name();
        }
    }
    public String intergralFromLabel() {
        return this.userIntegralFromType.name();

    }


}

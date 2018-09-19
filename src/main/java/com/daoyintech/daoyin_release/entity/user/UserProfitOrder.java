package com.daoyintech.daoyin_release.entity.user;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class UserProfitOrder extends AbstractEntity {

    @Column
    private String orderNo;

    @Column
    private Long userProfitId;

    @Column
    private Long userProfitAppletId;

    @Column
    private BigDecimal profitMoney = new BigDecimal(0);//分得利润是钱，不是积分

    @Column
    private BigDecimal orderMoney = new BigDecimal(0);

    /**
     * 0 有效
     * 1 失效
     * */
    @Column
    private int status = 0;

    @Column
    @Enumerated(EnumType.STRING)
    private UserProfitOrderType type = UserProfitOrderType.公众号;

}

package com.daoyintech.daoyin_release.entity.order;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.daoyintech.daoyin_release.enums.order.OrderPayType;
import com.daoyintech.daoyin_release.enums.order.OrderStatus;
import com.daoyintech.daoyin_release.enums.order.OrderType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@EqualsAndHashCode(callSuper = true)
public class Order extends AbstractEntity {

    @Column
    private Long userId;

    @Column
    private String province;

    @Column
    private String city;

    @Column
    private String district;

    @Column
    private String street;

    @Column
    private String phone;

    @Column
    private String name;



    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.等待支付;


    @Column
    private String expressNo;

    @Column
    private String expressName;

    @Column
    private BigDecimal expressMoney;

    @Column
    private String sendType;


    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendAt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedAt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date canceledAt; //取消时间

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date payAt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date refundAt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date loseAt;//失效时间

    @Column
    private String orderNo;

    @Column
    private String refundNo;//退款单号

    @Column
    private String reason;//退款理由

    @Column
    private String content; //拒绝退款原因

    @Column
    @Enumerated(EnumType.STRING)
    private OrderType orderType = OrderType.普通订单;

    @Column
    private BigDecimal freightPrice = new BigDecimal(0);

    @Column
    private Boolean isFreightFree = false;

    @Column
    private BigDecimal payMoney = new BigDecimal(0);

    @Column
    private BigDecimal discount = new BigDecimal(0);

    @Column
    private Boolean isUseIntegral = false;

    @Column
    private BigDecimal realPayMoney = new BigDecimal(0);

    @Column
    private BigDecimal usedIntegral = new BigDecimal("0");

    @Column
    private Boolean isMyselfPick = false; //是否自提（上门取件，免运费）

    @Column
    @Enumerated(EnumType.STRING)
    private OrderPayType orderPayType = OrderPayType.微信;


    public Double totalPrice(List<LineItem> items){
        Double sum = 0.0;
        for(int i = 0;i < items.size(); i++){
            LineItem lineItem = items.get(i);
            sum += lineItem.totalPrice();
        }
        return sum;
    }


}

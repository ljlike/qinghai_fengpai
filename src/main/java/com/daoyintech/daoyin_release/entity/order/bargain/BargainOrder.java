package com.daoyintech.daoyin_release.entity.order.bargain;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.daoyintech.daoyin_release.enums.bargain.BargainOrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "bargain_orders")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class BargainOrder extends AbstractEntity {

    @Column
    private String orderNo;

    @Column
    private Long orderId;

    @Column
    private Long userId;

    @Column
    private Long productId;

    @Column
    private String qrCodeUrl;

    @Column
    private BigDecimal price = new BigDecimal(0);

    @Column
    @Enumerated(EnumType.STRING)
    private BargainOrderStatus status = BargainOrderStatus.正在进行;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredAt;

    @Column
    private Long formatId;

    @Column
    private Long colorId;

    @Column
    private Boolean hasColorEgg = false;


}

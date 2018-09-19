package com.daoyintech.daoyin_release.entity.user;


import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * 粉丝分润（小程序）
 */
@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class UserProfitApplet extends AbstractEntity {

    @Column
    private Long fromUserId;

    @Column
    private Long toUserId;

    @Column
    private BigDecimal totalPrice = new BigDecimal(0);

    @Column
    private Boolean isProfit= false;
}

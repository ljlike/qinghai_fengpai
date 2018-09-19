package com.daoyintech.daoyin_release.entity.user;


import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.daoyintech.daoyin_release.enums.user.UserProfitType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class UserProfit extends AbstractEntity {

    @Column
    private Long fromUserId;

    @Column
    private Long toUserId;

    @Column
    @Enumerated(EnumType.STRING)
    private UserProfitType userProfitType = UserProfitType.direct;

    @Column
    private BigDecimal totalPrice = new BigDecimal(0);

}

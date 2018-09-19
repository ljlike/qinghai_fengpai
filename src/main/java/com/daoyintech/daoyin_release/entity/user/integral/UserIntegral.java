package com.daoyintech.daoyin_release.entity.user.integral;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.daoyintech.daoyin_release.entity.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "user_integral")
@EqualsAndHashCode(callSuper = true)
public class UserIntegral extends AbstractEntity {

    @Column
    private Long userId;

    @Column
    private BigDecimal integral = new BigDecimal("0");


}

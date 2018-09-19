package com.daoyintech.daoyin_release.entity.order.bargain;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.daoyintech.daoyin_release.enums.bargain.BargainHelpType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "bargain_joiners")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class BargainJoiner extends AbstractEntity {

    @Column
    private Long bargainOrderId;

    @Column
    private Long joinerId;

    @Column
    private BigDecimal bargainPrice = new BigDecimal(0);

    @Column
    private String avatar;

    @Column
    private String nickName;

    @Column
    @JsonIgnore
    private BigDecimal scale = new BigDecimal(1);

    @Column
    @Enumerated(EnumType.ORDINAL)
    private BargainHelpType type = BargainHelpType.getType(0);

    @Column
    private Integer position = 0;

    @Column
    private String description ="";
}

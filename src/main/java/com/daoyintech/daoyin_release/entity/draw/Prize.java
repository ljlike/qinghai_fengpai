package com.daoyintech.daoyin_release.entity.draw;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "prizes")
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Prize extends AbstractEntity {

    /**
     * 积分最小
     * */
    @Column
    private int min;

    /**
     * 积分最大
     * */
    @Column
    private int max;

    /**
     * 此区间允许的总中奖人数
     * */
    @Column
    private int maxPrizePersonsCount = 0;

    /**
     * 今天/这月/此次放奖 中已中奖的人数
     * */
    @Column
    private Integer todayPrizePersonsCount = 0;

    /**
     * 此区间允许的总中奖积分
     * */
    @Column
    private int totalPrizePoint = 0;

    /**
     * 今天/这月/此次放奖 中已中奖的积分
     * */
    @Column
    private int todayPrizePoint = 0;


}

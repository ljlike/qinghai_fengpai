package com.daoyintech.daoyin_release.entity.card;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author pei on 2018/7/20
 */
@Entity
@Table(name = "wx_card")
@Data
@EqualsAndHashCode(callSuper=false)
public class WxCard extends AbstractEntity {

    @Column
    private String title;

    @Column
    private String cardId;

    @Column
    private Long fixedTime;


}








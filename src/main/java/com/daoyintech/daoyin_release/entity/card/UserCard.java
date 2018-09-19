package com.daoyintech.daoyin_release.entity.card;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author pei on 2018/7/11
 */
@Entity
@Table(name = "user_card")
@Data
@EqualsAndHashCode(callSuper=false)
public class UserCard extends AbstractEntity {

    @Column
    private String openId;

    @Column
    private String appletOpenId;

    @Column
    private String cardId;

    @Column
    private String userCardCode;

    @Column
    private Integer status;

    @Column
    private Long expire;

    @Column
    private Long userId;

    /**
     * 法务  2
     * 心理  1
     * */
    @Column
    private int type;

}

package com.daoyintech.daoyin_release.entity.user;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class User extends AbstractEntity {

    @Column
    private String nickName;

    @Column
    private String avatar;

    @Column
    private String openId;

    @Column
    private String appletQrCode;

    @Column
    private String unionId;

    @Column
    private String qrCodeUrl;

    @Column
    private Boolean isSubscribe = false;

    @Column
    private Boolean isAgent= false;

    @Column
    private String bankCardNum;

    @Column
    private Long openBank;

    @Column
    private String holdCardName;

    @Column
    @JsonIgnore
    private String bankPassword;

    @Column
    private String appletOpenId;

    @Column
    private Boolean isPayGiftBag;

    @Column
    private String avatarQiniu;

    // 对应用户当前的合伙人等级
    @Column
    private Long agentLevelId;

    @Column
    private Integer fansCount;

}

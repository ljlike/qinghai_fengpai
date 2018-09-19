package com.daoyintech.daoyin_release.entity.user;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_settings")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserSetting extends AbstractEntity {

    @Column
    private Long userId;

    @Column
    private Long userDefaultAddressId;


}

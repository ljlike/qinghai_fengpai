package com.daoyintech.daoyin_release.entity.setting;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.daoyintech.daoyin_release.enums.setting.OverallSettingStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "overall_setting")
@EqualsAndHashCode(callSuper = true)
@Data
public class OverallSetting extends AbstractEntity {

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private OverallSettingStatus status =  OverallSettingStatus.off;
}

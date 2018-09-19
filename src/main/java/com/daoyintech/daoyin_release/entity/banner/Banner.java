package com.daoyintech.daoyin_release.entity.banner;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
@Table(name = "banners")
@Data
@EqualsAndHashCode(callSuper = true)
public class Banner extends AbstractEntity {

    @Column
    private String iconKey;

    @Column
    private String link;

    @Autowired
    private Boolean isSell = true;
}

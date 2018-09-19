package com.daoyintech.daoyin_release.entity.user;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_addresses")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserAddress extends AbstractEntity {

    @Column
    private Long userId;

    @Column
    private String province;

    @Column
    private String city;

    @Column
    private String district;

    @Column
    private String street;

    @Column
    private String phone;

    @Column
    private String name;

}

package com.daoyintech.daoyin_release.entity.bank;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "bank")
@EqualsAndHashCode(callSuper = true)
public class Bank extends AbstractEntity {

    @Column
    private String name;

    @Column
    private int bankCode;
}

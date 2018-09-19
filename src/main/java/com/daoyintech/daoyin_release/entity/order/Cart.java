package com.daoyintech.daoyin_release.entity.order;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "carts")
@Data
@EqualsAndHashCode(callSuper = true)
public class Cart extends AbstractEntity {

    @Column
    private Long userId;

}

package com.daoyintech.daoyin_release.entity.product;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "product_colors")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductColor extends AbstractEntity {

    @Column
    private String name;

    @Column
    private Long productId;

}

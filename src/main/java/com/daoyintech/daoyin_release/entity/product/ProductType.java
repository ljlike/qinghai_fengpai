package com.daoyintech.daoyin_release.entity.product;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.daoyintech.daoyin_release.enums.ProductTypeStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "product_types")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductType extends AbstractEntity {

    @Column
    private String name;

    @Column
    private String iconKey; //图片路径

    @Column
    @Enumerated(EnumType.STRING)
    private ProductTypeStatus status = ProductTypeStatus.上架;


}

package com.daoyintech.daoyin_release.entity.product;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "sub_product_types")
@Data
@EqualsAndHashCode(callSuper = true)
public class SubProductType extends AbstractEntity {

    @Column
    private Long productTypeId;

}

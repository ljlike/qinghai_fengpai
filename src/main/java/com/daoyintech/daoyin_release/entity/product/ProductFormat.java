package com.daoyintech.daoyin_release.entity.product;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.daoyintech.daoyin_release.enums.product.FormatStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_formats")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductFormat extends AbstractEntity {

    @Column
    private String name;

    @Column
    private Long productId;


    @Column
    private BigDecimal sellPrice = new BigDecimal(0);

    @Column
    @Enumerated(EnumType.STRING)
    private FormatStatus status = FormatStatus.上架;

}

package com.daoyintech.daoyin_release.entity.product;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.daoyintech.daoyin_release.enums.ImageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

@Entity
@Table(name = "product_images")
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductImage extends AbstractEntity {

    @Column
    private Long productDetailId; //商品id,同productId

    @Column
    private String attachmentKey; //图片地址

    @Column
    @Enumerated(EnumType.STRING)
    private ImageType imageType; //图片的类型

}

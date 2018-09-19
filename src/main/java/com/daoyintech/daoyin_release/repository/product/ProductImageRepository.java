package com.daoyintech.daoyin_release.repository.product;

import com.daoyintech.daoyin_release.entity.product.ProductImage;
import com.daoyintech.daoyin_release.enums.ImageType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends CrudRepository<ProductImage, Long> {

    List<ProductImage> findByProductDetailIdAndImageType(Long productId, ImageType imageType);

}

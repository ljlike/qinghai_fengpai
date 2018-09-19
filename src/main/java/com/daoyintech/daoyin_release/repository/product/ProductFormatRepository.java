package com.daoyintech.daoyin_release.repository.product;

import com.daoyintech.daoyin_release.entity.product.ProductFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductFormatRepository extends JpaRepository<ProductFormat,Long> {

    List<ProductFormat> findByProductId(Long productId);

}

package com.daoyintech.daoyin_release.repository.product;

import com.daoyintech.daoyin_release.entity.product.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductColorRepository extends JpaRepository<ProductColor,Long> {

    List<ProductColor> findByProductId(Long productId);

}

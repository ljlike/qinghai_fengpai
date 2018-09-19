package com.daoyintech.daoyin_release.repository.product;

import com.daoyintech.daoyin_release.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findTop3ByIsSellIsTrueAndIsRecommendationIsTrueOrderByHotSortDesc();

    @Modifying
    @Query(value = "SELECT * FROM products p JOIN sub_product_types spt ON p.sub_product_type_id = spt.id WHERE p.is_sell = 1 AND spt.product_type_id=?1 ORDER BY p.hot_sort DESC", nativeQuery = true)
    List<Product> findProductsByProductTypeId(Long productTypeId);

    List<Product> findByNameContaining(String name);

}

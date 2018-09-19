package com.daoyintech.daoyin_release.repository.product;

import com.daoyintech.daoyin_release.entity.product.ProductType;
import com.daoyintech.daoyin_release.enums.ProductTypeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType,Long>{

    List<ProductType> findByStatus(ProductTypeStatus status);

}

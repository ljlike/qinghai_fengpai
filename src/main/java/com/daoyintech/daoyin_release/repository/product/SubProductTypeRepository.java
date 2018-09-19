package com.daoyintech.daoyin_release.repository.product;

import com.daoyintech.daoyin_release.entity.product.ProductType;
import com.daoyintech.daoyin_release.entity.product.SubProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
@Repository
public interface SubProductTypeRepository extends JpaRepository<SubProductType,Long> {

}

package com.daoyintech.daoyin_release.mappers;

import com.daoyintech.daoyin_release.response.result.ProductFavoriteResult;
import com.daoyintech.daoyin_release.response.result.ProductResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {

    List<ProductResult> seekProductLikeProductName(String productName);

    List<ProductFavoriteResult> selectProductAndFavorite(@Param("productTypeId") Long productTypeId, @Param("userId") Long userId);

    List<ProductFavoriteResult> selectProductAndFavoriteForHomePage(Long userId);

    List<ProductResult> selectFavoriteProductsByUseId(Long userId);
}
package com.daoyintech.daoyin_release.service.product;

import com.daoyintech.daoyin_release.entity.product.Product;
import com.daoyintech.daoyin_release.entity.product.ProductFormat;
import com.daoyintech.daoyin_release.response.product.ProductDetailResponse;
import com.daoyintech.daoyin_release.response.product.ProductFavoriteResponse;
import com.daoyintech.daoyin_release.response.product.ProductResponse;
import com.daoyintech.daoyin_release.response.result.ProductResult;

import java.util.List;


public interface ProductService {

    ProductResponse findByProductId(Long productId);

    Product findProductByProductId(Long productId);

    ProductResponse findProductDetailByProductId(Long userId, Long productId);

    ProductDetailResponse findProductColorsAndFormatsByProductId(Long productId);

    List<ProductResult> seekProductLikeProductName(String productName);

    List<ProductFavoriteResponse> findProductsAndFavoriteByProductTypeIdAndUserId(Long productTypeId, Long userId);

    List<ProductResult> selectFavoriteProductsByUseId(Long userId);

    ProductFormat findProductFormatById(Long formatId);

    String findBannerUrlById(Long bannerId);

}

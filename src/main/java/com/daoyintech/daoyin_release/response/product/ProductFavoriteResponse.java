package com.daoyintech.daoyin_release.response.product;

import com.daoyintech.daoyin_release.response.DefinitionResponse;
import com.daoyintech.daoyin_release.response.result.ProductFavoriteResult;
import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductFavoriteResponse {

    private Long productId;

    private String productName;

    private BigDecimal showPrice;

    private BigDecimal sellPrice;

    private String imgUrl;

    private Boolean isFavorite;

    public static List<ProductFavoriteResponse> productFavoriteResponsesBuild(List<ProductFavoriteResult> results){
        return Lists.transform(results,input -> {
            ProductFavoriteResponse response = new ProductFavoriteResponse();
            response.setProductId(input.getProductId());
            response.setProductName(input.getProductName());
            response.setSellPrice(input.getSellPrice());
            response.setShowPrice(input.getShowPrice());
            response.setImgUrl(DefinitionResponse.getImgUrl(input.getImgUrl()));
            response.setIsFavorite(userIdTransFormIsFavorite(input.getUserId()));
            return response;
        });
    }

    private static Boolean userIdTransFormIsFavorite(Long userId){
        return userId!=null ? true : false;
    }

}

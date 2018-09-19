package com.daoyintech.daoyin_release.response.result;

import com.daoyintech.daoyin_release.response.DefinitionResponse;
import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductResult {

    private Long productId;

    private String productName;

    private String imgUrl;

    private String productTypeName;

    private BigDecimal sellPrice;

    public static List<ProductResult> productResultsBuild(List<ProductResult> productResults){
        return Lists.transform(productResults,input -> {
            ProductResult result = new ProductResult();
            result.setProductId(input.getProductId());
            result.setProductName(input.getProductName());
            result.setImgUrl(DefinitionResponse.getImgUrl(input.getImgUrl()));
            result.setSellPrice(input.getSellPrice());
            result.setProductTypeName(input.getProductTypeName());
            return result;
        });
    }

}

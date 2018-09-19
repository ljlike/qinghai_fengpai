package com.daoyintech.daoyin_release.response.product;

import com.daoyintech.daoyin_release.entity.product.ProductType;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class ProductTypeResponse {

    private Long id;

    private String name;

    public static List<ProductTypeResponse> productTypeResponseBuild(List<ProductType> productTypes){
        return Lists.transform(productTypes,input -> {
            ProductTypeResponse productTypeResponse = new ProductTypeResponse();
            productTypeResponse.setId(input.getId());
            productTypeResponse.setName(input.getName());
            return productTypeResponse;
        });
    }
}

package com.daoyintech.daoyin_release.response.product;

import com.daoyintech.daoyin_release.entity.product.ProductFormat;
import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductFormatResponse {

    private Long id;

    private String name;

    private BigDecimal price = new BigDecimal(0);

    public static List<ProductFormatResponse> getProductFormatModel(List<ProductFormat> productFormats){
        return Lists.transform(productFormats, input -> {
            ProductFormatResponse productFormatResponse = getProductFormatModel(input);
            return productFormatResponse;
        });
    }

    public static ProductFormatResponse getProductFormatModel(ProductFormat productFormat){
        ProductFormatResponse productFormatResponse = new ProductFormatResponse();
        productFormatResponse.setId(productFormat.getId());
        productFormatResponse.setName(productFormat.getName());
        productFormatResponse.setPrice(productFormat.getSellPrice());
        return productFormatResponse;

    }
}

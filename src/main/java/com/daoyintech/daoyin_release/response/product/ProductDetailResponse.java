package com.daoyintech.daoyin_release.response.product;

import com.daoyintech.daoyin_release.entity.product.Product;
import com.daoyintech.daoyin_release.entity.product.ProductColor;
import com.daoyintech.daoyin_release.entity.product.ProductFormat;
import com.daoyintech.daoyin_release.response.DefinitionResponse;
import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDetailResponse {

    private Long productId;

    private String name;

    private Integer inventory;

    private String url;

    private List<ProductColorResponse> colors;

    private List<ProductFormatResponse> format;

    public static ProductDetailResponse productDetailResponseBuild(Product product,List<ProductColor> colors,List<ProductFormat> formats){
        ProductDetailResponse response = new ProductDetailResponse();
        response.setProductId(product.getId());
        response.setName(product.getName());
        response.setUrl(DefinitionResponse.getImgUrl(product.getIconKey()));
        response.setInventory(product.getInventory());
        response.setColors(getColors(colors));
        response.setFormat(getFormat(formats));
        return response;
    }

    private static List<ProductColorResponse> getColors(List<ProductColor> colors) {
        return Lists.transform(colors, input -> {
            ProductColorResponse response = new ProductColorResponse();
            response.setProductColorId(input.getId());
            response.setName(input.getName());
            return response;
        });
    }

    private static List<ProductFormatResponse> getFormat(List<ProductFormat> formats) {
        return Lists.transform(formats, input -> {
            ProductFormatResponse response = new ProductFormatResponse();
            response.setProductFormatId(input.getId());
            response.setSellPrice(input.getSellPrice());
            response.setName(input.getName());
            return response;
        });
    }

    @Data
    static class ProductColorResponse{
        private Long productColorId;
        private String name;
    }

    @Data
    static class ProductFormatResponse{
        private Long productFormatId;
        private BigDecimal sellPrice;
        private String name;
    }
}

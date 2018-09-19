package com.daoyintech.daoyin_release.response.product;

import com.daoyintech.daoyin_release.entity.product.Product;
import com.daoyintech.daoyin_release.entity.product.ProductColor;
import com.daoyintech.daoyin_release.entity.product.ProductFormat;
import com.daoyintech.daoyin_release.entity.product.ProductImage;
import com.daoyintech.daoyin_release.response.DefinitionResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductResponse {

    private Long id;

    private Long productId;

    private String name;

    private String description;

    private BigDecimal showPrice;

    private BigDecimal sellPrice;

    private List<String> bannerImgUrls;

    private List<String> detailImgUrls;

    private String url;

    private String thumb;

    private Integer showSellCount = 0;

    private Boolean isLuxury;

    private BigDecimal pointPrecent = new BigDecimal(0.5);

    private Integer inventory = 0;

    private Boolean isFavorite;

    @JsonIgnore
    private ProductColor color;

    private ProductFormatResponse format;

    public static List<ProductResponse> productResponseBuild(List<Product> products) {
        return Lists.transform(products, input -> {
            ProductResponse response = new ProductResponse();
            response.setProductId(input.getId());
            response.setName(input.getName());
            response.setShowPrice(input.getShowPrice());
            response.setSellPrice(input.getSellPrice());
            response.setUrl(DefinitionResponse.getImgUrl(input.getIconKey()));
            return response;
        });
    }

    public static ProductResponse productResponseBuild(Product product, List<ProductImage> images) {
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setShowPrice(product.getShowPrice());
        response.setSellPrice(product.getSellPrice());
        response.setUrl(DefinitionResponse.getImgUrl(product.getIconKey()));
        response.setBannerImgUrls(getImgUrls(images));
        return response;
    }

    public static ProductResponse productResponseBuild(Product product, List<ProductImage> bannerImages, List<ProductImage> detailImages, Boolean isFavorite){
        ProductResponse response = new ProductResponse();
        response.setBannerImgUrls(getImgUrls(bannerImages));
        response.setProductId(product.getId());
        response.setName(product.getName());
        response.setInventory(product.getInventory());
        response.setSellPrice(product.getSellPrice());
        response.setDetailImgUrls(getImgUrls(detailImages));
        response.setIsFavorite(isFavorite);
        return response;
    }

    private static List<String> getImgUrls(List<ProductImage> images) {
        return Lists.transform(images, input -> DefinitionResponse.getImgUrl(input.getAttachmentKey()));
    }

    public static ProductResponse productTransformProductResponse(Product product,
                                                                  ProductFormat format,
                                                                  ProductColor color) {
        ProductResponse productResponse = new ProductResponse();
        if (!StringUtils.isEmpty(product)){
            productResponse.setName(product.getName());
            productResponse.setId(product.getId());
            productResponse.setThumb(product.url());
            productResponse.setPointPrecent(product.getPointPercent());
            productResponse.setIsLuxury(product.getIsLuxury());
            productResponse.setPointPrecent(product.getPointPercent());
        }
        if (!StringUtils.isEmpty(color)){
            productResponse.setColor(color);
        }
        if (!StringUtils.isEmpty(format)){
            productResponse.setFormat(ProductFormatResponse.getProductFormatModel(format));
        }
        return productResponse;
    }
}

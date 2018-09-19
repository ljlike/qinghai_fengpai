package com.daoyintech.daoyin_release.response;

import com.daoyintech.daoyin_release.entity.banner.Banner;
import com.daoyintech.daoyin_release.entity.product.ProductType;
import com.daoyintech.daoyin_release.response.product.ProductFavoriteResponse;
import com.daoyintech.daoyin_release.response.product.ProductTypeResponse;
import com.daoyintech.daoyin_release.response.result.ProductFavoriteResult;
import lombok.Data;

import java.util.List;

@Data
public class WelcomeResponse {

    private List<ProductTypeResponse> productTypes;

    private List<BannerResponse> banners;

    //private List<ProductResponse> products;

    private List<ProductFavoriteResponse> products;

    /*public static WelcomeResponse welcomeResponseBuild(List<ProductType> productTypes, List<Banner> banners, List<Product> products) {
        WelcomeResponse response = new WelcomeResponse();
        response.setProductTypes(ProductTypeResponse.productTypeResponseBuild(productTypes));
        response.setBanners(BannerResponse.bannerResponseBuild(banners));
        response.setProducts(ProductResponse.productResponseBuild(products));
        return response;
    }*/

    public static WelcomeResponse welcomeResponseBuild(List<ProductType> productTypes, List<Banner> banners, List<ProductFavoriteResult> products) {
        WelcomeResponse response = new WelcomeResponse();
        response.setProductTypes(ProductTypeResponse.productTypeResponseBuild(productTypes));
        response.setBanners(BannerResponse.bannerResponseBuild(banners));
        response.setProducts(ProductFavoriteResponse.productFavoriteResponsesBuild(products));
        return response;
    }
}

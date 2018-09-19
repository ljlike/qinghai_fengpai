package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.banner.Banner;
import com.daoyintech.daoyin_release.entity.product.Product;
import com.daoyintech.daoyin_release.entity.product.ProductColor;
import com.daoyintech.daoyin_release.entity.product.ProductFormat;
import com.daoyintech.daoyin_release.entity.product.ProductImage;
import com.daoyintech.daoyin_release.enums.ImageType;
import com.daoyintech.daoyin_release.mappers.ProductMapper;
import com.daoyintech.daoyin_release.repository.BannerRepository;
import com.daoyintech.daoyin_release.repository.product.ProductColorRepository;
import com.daoyintech.daoyin_release.repository.product.ProductFormatRepository;
import com.daoyintech.daoyin_release.repository.product.ProductImageRepository;
import com.daoyintech.daoyin_release.repository.product.ProductRepository;
import com.daoyintech.daoyin_release.response.BannerResponse;
import com.daoyintech.daoyin_release.response.DefinitionResponse;
import com.daoyintech.daoyin_release.response.product.ProductDetailResponse;
import com.daoyintech.daoyin_release.response.product.ProductFavoriteResponse;
import com.daoyintech.daoyin_release.response.product.ProductResponse;
import com.daoyintech.daoyin_release.response.result.ProductFavoriteResult;
import com.daoyintech.daoyin_release.response.result.ProductResult;
import com.daoyintech.daoyin_release.service.ProductService;
import com.daoyintech.daoyin_release.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductColorRepository productColorRepository;

    @Autowired
    private ProductFormatRepository productFormatRepository;

    @Autowired
    private UserFavoriteService userFavoriteService;

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductResponse findByProductId(Long productId) {
        Product product = productRepository.getOne(productId);
        List<ProductImage> images = productImageRepository.findByProductDetailIdAndImageType(productId, ImageType.banner);
        return ProductResponse.productResponseBuild(product, images);
    }

    @Override
    public Product findProductByProductId(Long productId) {
        return productRepository.getOne(productId);
    }

    @Override
    public ProductResponse findProductDetailByProductId(Long userId, Long productId) {
        Product product = productRepository.getOne(productId);
        Boolean isFav = userFavoriteService.isFavProduct(userId, product.getId());
        List<ProductImage> bannerImages = productImageRepository.findByProductDetailIdAndImageType(productId, ImageType.banner);
        List<ProductImage> detailImages = productImageRepository.findByProductDetailIdAndImageType(productId, ImageType.detail);
        return ProductResponse.productResponseBuild(product, bannerImages, detailImages, isFav);
    }

    @Override
    public List<ProductFavoriteResponse> findProductsAndFavoriteByProductTypeIdAndUserId(Long productTypeId, Long userId) {
        List<ProductFavoriteResult> results = productMapper.selectProductAndFavorite(productTypeId, userId);
        return ProductFavoriteResponse.productFavoriteResponsesBuild(results);
    }

    @Override
    public ProductDetailResponse findProductColorsAndFormatsByProductId(Long productId) {
        Product product = productRepository.getOne(productId);
        List<ProductColor> colors = productColorRepository.findByProductId(productId);
        List<ProductFormat> formats = productFormatRepository.findByProductId(productId);
        return ProductDetailResponse.productDetailResponseBuild(product, colors, formats);
    }

    @Override
    public List<ProductResult> seekProductLikeProductName(String productName) {
        List<ProductResult> results = productMapper.seekProductLikeProductName(productName);
        return ProductResult.productResultsBuild(results);
    }

    @Override
    public List<ProductResult> selectFavoriteProductsByUseId(Long userId){
        List<ProductResult> results = productMapper.selectFavoriteProductsByUseId(userId);
        return ProductResult.productResultsBuild(results);
    }

    @Override
    public ProductFormat findProductFormatById(Long formatId) {
        return productFormatRepository.getOne(formatId);
    }

    @Override
    public String findBannerUrlById(Long bannerId){
        Optional<Banner> banner = bannerRepository.findById(bannerId);
        String url = DefinitionResponse.getImgUrl(banner.get().getIconKey());
        return url;
    }
}

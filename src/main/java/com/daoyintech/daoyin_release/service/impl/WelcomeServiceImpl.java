package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.banner.Banner;
import com.daoyintech.daoyin_release.entity.product.Product;
import com.daoyintech.daoyin_release.entity.product.ProductType;
import com.daoyintech.daoyin_release.enums.ProductTypeStatus;
import com.daoyintech.daoyin_release.mappers.ProductMapper;
import com.daoyintech.daoyin_release.repository.BannerRepository;
import com.daoyintech.daoyin_release.repository.product.ProductRepository;
import com.daoyintech.daoyin_release.repository.product.ProductTypeRepository;
import com.daoyintech.daoyin_release.response.BannerResponse;
import com.daoyintech.daoyin_release.response.WelcomeResponse;
import com.daoyintech.daoyin_release.response.product.ProductResponse;
import com.daoyintech.daoyin_release.response.product.ProductTypeResponse;
import com.daoyintech.daoyin_release.response.result.ProductFavoriteResult;
import com.daoyintech.daoyin_release.service.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WelcomeServiceImpl implements WelcomeService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public WelcomeResponse findWelcomeResponseBy(ProductTypeStatus status, Boolean isSell, Long userId) {
        List<ProductType> productTypes = productTypeRepository.findByStatus(status);
        List<Banner> banners = bannerRepository.findAllByIsSellOrderByUpdatedAtDesc(isSell);
        List<ProductFavoriteResult> products = productMapper.selectProductAndFavoriteForHomePage(userId);
        return WelcomeResponse.welcomeResponseBuild(productTypes, banners, products);
    }

}

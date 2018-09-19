package com.daoyintech.daoyin_release.response.result;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductFavoriteResult {

    private Long productId;

    private String productName;

    private BigDecimal showPrice;

    private BigDecimal sellPrice;

    private String imgUrl;

    private Long userId;

}

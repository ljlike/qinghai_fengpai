package com.daoyintech.daoyin_release.response.order;

import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.entity.product.Product;
import com.daoyintech.daoyin_release.entity.product.ProductColor;
import com.daoyintech.daoyin_release.entity.product.ProductFormat;
import com.daoyintech.daoyin_release.response.product.ProductResponse;
import com.daoyintech.daoyin_release.response.product.ProductFormatResponse;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by pei
 */
@Data
public class LineItemDetailResponse {

    private Long id;

    private Integer quantity;

    private ProductResponse product;

    private BigDecimal singlePrice = new BigDecimal(0);

    private ProductFormatResponse format;

    public static LineItemDetailResponse buildByLineItem(LineItem item,
                                                         Product product,
                                                         ProductFormat format,
                                                         ProductColor color) {
        LineItemDetailResponse lineItemDetailResponse = new LineItemDetailResponse();
        lineItemDetailResponse.setId(item.getId());
        lineItemDetailResponse.setQuantity(item.getQuantity());
        lineItemDetailResponse.setSinglePrice(item.getPrice());
        lineItemDetailResponse.setProduct(ProductResponse.productTransformProductResponse(product,format,color));
        return lineItemDetailResponse;
    }
}

package com.daoyintech.daoyin_release.service;

import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.product.Product;
import com.daoyintech.daoyin_release.entity.product.ProductColor;
import com.daoyintech.daoyin_release.entity.product.ProductFormat;
import com.daoyintech.daoyin_release.response.order.OrderLineItemRequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author pei on 2018/08/14
 */
public interface LineItemService {


    LineItem getOne(Long lineItemId);


    /**
     * @param items
     * @return
     */
    Boolean handleProductInventory(List<LineItem> items);


    List<LineItem> transformFromModel(List<OrderLineItemRequest> items, Order order);


    List<LineItem> findByOrderId(Long orderId);


    LineItem createLineItemByProductAndProductFormat(Product product, ProductFormat format, ProductColor color);


    LineItem updatePriceByBargainOrder(Long LineItemId, BigDecimal price);


}










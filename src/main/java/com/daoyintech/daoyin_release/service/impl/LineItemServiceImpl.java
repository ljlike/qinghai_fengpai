package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.product.Product;
import com.daoyintech.daoyin_release.entity.product.ProductColor;
import com.daoyintech.daoyin_release.entity.product.ProductFormat;
import com.daoyintech.daoyin_release.repository.product.ProductRepository;
import com.daoyintech.daoyin_release.repository.order.LineItemRepository;
import com.daoyintech.daoyin_release.repository.product.ProductFormatRepository;
import com.daoyintech.daoyin_release.response.order.OrderLineItemRequest;
import com.daoyintech.daoyin_release.service.LineItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pei on 2018/08/14
 */
@Service
public class LineItemServiceImpl implements LineItemService{

    @Autowired
    private LineItemRepository lineItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductFormatRepository productFormatRepository;

    @Override
    public LineItem getOne(Long lineItemId) {
        return lineItemRepository.getOne(lineItemId);
    }


    @Override
    public synchronized Boolean handleProductInventory(List<LineItem> items) {
        for (LineItem lineItem : items) {
            Long productId = lineItem.getProductId();
            Product product = productRepository.getOne(productId);
            if (product.getInventory() - lineItem.getQuantity() < 0) {
                return true;
            }
            product.setInventory(product.getInventory() - lineItem.getQuantity());
            productRepository.save(product);
        }
        return false;
    }

    @Override
    public List<LineItem> transformFromModel(List<OrderLineItemRequest> items, Order order) {
        List<LineItem> lineItems = new ArrayList<>();
        items.forEach(wxLineItemRequest -> {
            LineItem item = lineItemRepository.getOne(wxLineItemRequest.getLineItemId());
            //TODO 普通订单购物车id
            item.setCartId(null);
            item.setOrderId(order.getId());
            item.setPrice(productFormatRepository.getOne(item.getFormatId()).getSellPrice());
            LineItem lineItem = lineItemRepository.save(item);
            lineItems.add(lineItem);
        });
        return lineItems;
    }

    @Override
    public List<LineItem> findByOrderId(Long orderId) {
        return lineItemRepository.findByOrderId(orderId);
    }

    @Override
    public LineItem createLineItemByProductAndProductFormat(Product product, ProductFormat format, ProductColor color) {
        LineItem item = new LineItem();
        item.setPrice(format.getSellPrice());
        item.setFormatId(format.getId());
        item.setProductId(product.getId());
        item.setQuantity(1);
        if (color != null) {
            item.setColorId(color.getId());
        }
        return item;
    }

    @Override
    public LineItem updatePriceByBargainOrder(Long lineItemId, BigDecimal price) {
        LineItem item = lineItemRepository.getOne(lineItemId);
        item.setPrice(price);
        return lineItemRepository.save(item);
    }


}











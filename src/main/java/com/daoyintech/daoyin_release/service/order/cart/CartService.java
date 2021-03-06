package com.daoyintech.daoyin_release.service.order.cart;

import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.response.result.LineItemResult;

import java.util.List;

public interface CartService {

    List<LineItemResult> getLineItemResultsByUserId(String unionId);

    LineItemResult saveOrUpdateLineItemByLineItemId(LineItem receivedItem);

    void delete(Long lineItemId);

    void createCartIfNotExist(Long userId);

    Long getCurrentCartIdByUnionId(String unionId);
}

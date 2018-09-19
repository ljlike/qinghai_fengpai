package com.daoyintech.daoyin_release.response.order;

import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by pei
 */
@Data
public class BargainResponse {

    private String bargainOrderNo;

    private BigDecimal price = new BigDecimal(0);

    private Boolean isFreightFree = false;

    private Boolean hasColorEgg = false;

    public static BargainResponse bargainOrderTransformBargainResponse(BargainOrder bargainOrder, Order order, LineItem item){
        BargainResponse bargainResponse = new BargainResponse();
        bargainResponse.setBargainOrderNo(bargainOrder.getOrderNo());
        bargainResponse.setPrice(item.getPrice());
        bargainResponse.setIsFreightFree(order.getIsFreightFree());
        bargainResponse.setHasColorEgg(bargainOrder.getHasColorEgg());
        return bargainResponse;
    }

}

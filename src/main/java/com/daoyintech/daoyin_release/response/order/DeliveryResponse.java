package com.daoyintech.daoyin_release.response.order;

import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.enums.order.OrderType;
import com.daoyintech.daoyin_release.utils.DateUtil;
import com.daoyintech.daoyin_release.utils.DateUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by pei
 */
@Slf4j
@Data
public class DeliveryResponse {

    private String name;

    private String address;

    private String phone;

    public static DeliveryResponse deliveryTransformDeliveryResponse(Order order) {
        DeliveryResponse deliveryResponse = new DeliveryResponse();
        deliveryResponse.setName(order.getName());
        String string = "";
        try {
            string = new StringBuilder(order.getProvince()).append(" ").append(order.getCity()).append("  ").append(order.getDistrict()).append(order.getStreet()).toString();
        }catch (Exception e){
            if (order.getOrderType().equals(OrderType.普通订单)){
                log.error("{}:订单地址为空:{}", DateUtils.getStringDate(),e.getMessage());
            }
        }
        deliveryResponse.setAddress(string);
        deliveryResponse.setPhone(order.getPhone());
        return deliveryResponse;
    }
}

package com.daoyintech.daoyin_release.response.order;

import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.order.OrderDate;
import lombok.Data;

import java.util.Date;

/**
 * Created by pei
 */
@Data
public class OrderTimeResponse {

    private Date createdAt;

    private Date sendAt;

    private Date payAt;

    private Date refundAt;

    private Date finishedAt;

    private Date canceledAt;

    private Date loseAt;

    public static OrderTimeResponse orderTimeTransformOrderTimeResponse(Order order) {
        OrderTimeResponse orderTimeResponse = new OrderTimeResponse();
        orderTimeResponse.setCreatedAt(order.getCreatedAt());
        orderTimeResponse.setFinishedAt(order.getFinishedAt());
        orderTimeResponse.setPayAt(order.getPayAt());
        orderTimeResponse.setSendAt(order.getSendAt());
        orderTimeResponse.setRefundAt(order.getRefundAt());
        orderTimeResponse.setCanceledAt(order.getCanceledAt());
        orderTimeResponse.setLoseAt(order.getLoseAt());
        return orderTimeResponse;
    }
}

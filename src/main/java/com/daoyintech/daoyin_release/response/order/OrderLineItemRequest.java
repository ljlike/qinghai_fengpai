package com.daoyintech.daoyin_release.response.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by pei
 */
@Data
public class OrderLineItemRequest {

    @ApiModelProperty("订单子数据id")
    private Long lineItemId;

}

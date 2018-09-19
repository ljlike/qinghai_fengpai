package com.daoyintech.daoyin_release.response.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pei
 */
@Data
public class OrderNewRequest {

    @ApiModelProperty("地址id")
    private Long addressId;

    @ApiModelProperty("是否使用积分")
    private Boolean isUseIntegral;

    @ApiModelProperty("是否自提")
    private Boolean isMyselfPick;

    @ApiModelProperty("订单子参数")
    private List<OrderLineItemRequest> items = new ArrayList<>();
}

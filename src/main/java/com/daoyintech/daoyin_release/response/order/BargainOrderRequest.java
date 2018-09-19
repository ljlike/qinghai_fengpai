package com.daoyintech.daoyin_release.response.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BargainOrderRequest {

	@ApiModelProperty("订单号")
	private String orderNo;

	@ApiModelProperty("地址id")
	private Long addressId;

	@ApiModelProperty("是否使用积分")
	private Boolean isUseIntegral;

	@ApiModelProperty("是否自提")
	private Boolean isMyselfPick;

}

package com.daoyintech.daoyin_release.response.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Administrator on 2017/8/30.
 */
@Data
public class UserAddressRequest {

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("区")
    private String district;

    @ApiModelProperty("详细地址")
    private String street;

    @ApiModelProperty("电话号码")
    private String phone;

    @ApiModelProperty("姓名")
    private String name;


}

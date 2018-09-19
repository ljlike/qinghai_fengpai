package com.daoyintech.daoyin_release.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * http请求返回的最外层对象
 * @author pei
 */
@Data
public class ResultResponse<T> {


    /** 错误码. */
    @ApiModelProperty("错误码")
    private Integer code;
    /** 提示信息. */
    @ApiModelProperty("提示信息")
    private String msg;

    /** 具体内容. */
    @ApiModelProperty("具体内容")
    private T data;
}

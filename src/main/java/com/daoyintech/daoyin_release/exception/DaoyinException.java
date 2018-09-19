package com.daoyintech.daoyin_release.exception;


import com.daoyintech.daoyin_release.enums.ResultEnum;

public class DaoyinException extends RuntimeException{

    private Integer code;

    public DaoyinException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public DaoyinException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}

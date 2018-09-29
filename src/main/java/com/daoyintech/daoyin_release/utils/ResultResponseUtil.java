package com.daoyintech.daoyin_release.utils;


import com.daoyintech.daoyin_release.response.ResultResponse;

public class ResultResponseUtil {

    @SuppressWarnings("unchecked")
    public static ResultResponse success(Object object) {
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setData(object);
        resultResponse.setCode(0);
        resultResponse.setMsg("成功");
        return resultResponse;
    }

    public static ResultResponse success() {
        return success(null);
    }

    public static ResultResponse error(Integer code, String msg) {
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setCode(code);
        resultResponse.setMsg(msg);
        return resultResponse;
    }

    @SuppressWarnings("unchecked")
    public static ResultResponse error(Integer code,Object object) {
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setCode(code);
        resultResponse.setData(object);
        return resultResponse;
    }
}

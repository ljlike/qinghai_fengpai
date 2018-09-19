package com.daoyintech.daoyin_release.enums.order;

public enum OrderPayType {

    微信,
    积分和微信,
    小程序;

    public static OrderPayType getOrderPayType(int status){
        switch (status){
            case 0 :
                return OrderPayType.微信;
            case 1:
                return OrderPayType.积分和微信;
                default:return OrderPayType.微信;
        }
    }
}

package com.daoyintech.daoyin_release.entity.user;

public enum UserProfitOrderType {
    公众号,
    小程序;

    public static UserProfitOrderType getType(int type){
        switch (type){
            case 0:
                return UserProfitOrderType.公众号;
            case 1:
                return UserProfitOrderType.小程序;
                default:
                    return UserProfitOrderType.公众号;
        }
    }
}

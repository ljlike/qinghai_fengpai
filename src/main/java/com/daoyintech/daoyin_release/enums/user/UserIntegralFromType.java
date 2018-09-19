package com.daoyintech.daoyin_release.enums.user;

public enum UserIntegralFromType {

    帮助朋友,
    购物消费,
    取消订单,
    订单分润,
    提现,
    关注公众号,
    退款,
    积分卷,
    订单分润回退;


    public static UserIntegralFromType getUserIntegralFromType(int status){
        switch (status){
            case 0:
                return UserIntegralFromType.帮助朋友;    //有用
            case 1:
                return UserIntegralFromType.购物消费;
            case 2:
                return UserIntegralFromType.取消订单;
            case 3:
                return UserIntegralFromType.订单分润;    //有用
            case 4:
                return UserIntegralFromType.提现;
            case 5:
                return UserIntegralFromType.关注公众号;
            case 6:
                return UserIntegralFromType.退款;
            case 7:
                return UserIntegralFromType.积分卷;      //有用
            default:return UserIntegralFromType.帮助朋友;
        }
    }
}

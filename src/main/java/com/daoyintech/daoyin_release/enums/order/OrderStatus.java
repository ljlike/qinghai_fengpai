package com.daoyintech.daoyin_release.enums.order;

public enum OrderStatus {
    等待支付,
    待发货,
    待收货,
    砍价中,
    已取消,
    退款中,
    拒绝退款,
    同意退款,
    已完成,
    退款失败,
    成功退款,
    已失效;

    public static OrderStatus getStatus(int o_status){
        switch (o_status){
            case 0:  //待付款
                return OrderStatus.等待支付;
            case 1:  //待发货
                return OrderStatus.待发货;
            case 2:   //待收货
                return OrderStatus.待收货;
            case 3:  //砍价中
                return OrderStatus.砍价中;
            case 4:   //已取消
                return OrderStatus.已取消;
            case 5:   //退款申请
                return OrderStatus.退款中;
            case 6:   //拒绝退款
                return OrderStatus.拒绝退款;
            case 7:   //同意退款
                return OrderStatus.同意退款;
            case 8 :
                return OrderStatus.退款失败;
            case 9 :
                return OrderStatus.成功退款;
            case 10:
                return OrderStatus.已失效;
            case 11:
                return OrderStatus.已完成;

            default:return OrderStatus.已完成;  //完成
        }
    }
}

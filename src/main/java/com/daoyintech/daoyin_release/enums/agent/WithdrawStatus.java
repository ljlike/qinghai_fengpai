package com.daoyintech.daoyin_release.enums.agent;

/**
 * Created by Administrator on 2017/9/5.
 */
public enum WithdrawStatus {
    已受理,
    提现失败,
    银行处理中,
    到账成功,
    到账失败,
    银行退票;

    public static WithdrawStatus getStatus(int o_status){
        switch (o_status){
            case 0:
                return WithdrawStatus.已受理;
            case 1:
                return WithdrawStatus.提现失败;
            case 2:
                return WithdrawStatus.银行处理中;
            case 3:
                return WithdrawStatus.到账成功;
            case 4:
                return WithdrawStatus.到账失败;
            case 5:
                return WithdrawStatus.银行退票;
            default:return WithdrawStatus.已受理;
        }
    }
}

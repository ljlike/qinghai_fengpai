package com.daoyintech.daoyin_release.enums.agent;

/**
 * Created by Administrator on 2017/9/5.
 */
public enum ApplyStatus {
    未提交,
    待审核,
    失败,
    通过,
    准合伙人,
    失效;

    public static ApplyStatus getStatus(int o_status){
        switch (o_status){
            case 0:
                return ApplyStatus.未提交;
            case 1:
                return ApplyStatus.待审核;
            case 2:
                return ApplyStatus.失败;
            case 3:
                return ApplyStatus.通过;
            case 4:
                return ApplyStatus.准合伙人;
            case 5:
                return ApplyStatus.失效;
            default:return ApplyStatus.准合伙人;
        }
    }
}

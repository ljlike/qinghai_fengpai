package com.daoyintech.daoyin_release.enums.bargain;

public enum BargainHelpType {
    彩蛋券,
    抵扣券,
    免邮券,
    积分券,
    心理券,
    法务券,
    线下活动卷;
    /*满减券;*/

    public static BargainHelpType getType(int type){
        switch (type){
            case 0:
                return BargainHelpType.彩蛋券;
            case 1:
                return BargainHelpType.抵扣券;
            case 2:
                return BargainHelpType.免邮券;
            case 3:
                return BargainHelpType.积分券;
            /*case 4:
                return BargainHelpType.满减券;*/
            case 4:
                return BargainHelpType.心理券;   //线下活动卷  type = 1 解惑人生重塑机缘
            case 5:
                return BargainHelpType.法务券;   //线下活动卷  type = 2 让幸福和性福有技可施
            case 6:
                return BargainHelpType.线下活动卷;
            default:
                return BargainHelpType.彩蛋券;
        }
    }
}

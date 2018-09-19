package com.daoyintech.daoyin_release.enums.agent;

public enum WithdrawType {
    零钱,
    银行卡;
    public static WithdrawType getType(Integer type){
        switch (type){
            case 0:
                return WithdrawType.零钱;
            case 1:
                return WithdrawType.银行卡;
                default:
                    return WithdrawType.零钱;
        }
    }
}

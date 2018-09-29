package com.daoyintech.daoyin_release.enums.card;

/**
 * @author pei on 2018/09/28
 */
public enum CardType {

    ONE(1,"线下活动券1"),
    TWO(2,"线下活动券2"),
    THREE(3,"积分券");

    private Integer code;

    private String message;

    CardType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Integer getType(int type){
        switch (type){
            case 3:
                return CardType.THREE.code;
            case 4:
                return CardType.ONE.code;
            case 5:
                return CardType.TWO.code;
            default:
                return CardType.TWO.code;
        }
    }

}

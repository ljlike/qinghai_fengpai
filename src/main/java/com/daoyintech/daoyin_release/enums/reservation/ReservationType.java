package com.daoyintech.daoyin_release.enums.reservation;

/**
 * Created by lj on 2018/9/21 13:28
 */
public enum ReservationType {
    拍照,
    选片,
    其他;

    public static ReservationType getReservationType(String type){
        if ("拍照".equals(type)){
            return ReservationType.拍照;
        }
        if ("选片".equals(type)){
            return ReservationType.选片;
        }
        return ReservationType.其他;
    }

}

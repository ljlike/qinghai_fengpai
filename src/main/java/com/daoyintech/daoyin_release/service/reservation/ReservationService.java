package com.daoyintech.daoyin_release.service.reservation;

import com.daoyintech.daoyin_release.response.ResultResponse;

/**
 * Created by lj on 2018/9/19 9:54
 */
public interface ReservationService {

    ResultResponse createReservationInfo(String userName, String phone, String strDate);

}

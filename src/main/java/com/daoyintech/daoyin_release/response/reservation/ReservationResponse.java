package com.daoyintech.daoyin_release.response.reservation;

import lombok.Data;

import java.util.Date;

/**
 * Created by lj on 2018/9/19 11:09
 */
@Data
public class ReservationResponse {

    private Date appointmentDate;

    private Integer maxReservationNumber;

    private Boolean isCameramanOut;
}

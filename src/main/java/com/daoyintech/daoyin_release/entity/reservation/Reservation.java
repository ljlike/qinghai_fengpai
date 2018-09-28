package com.daoyintech.daoyin_release.entity.reservation;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.daoyintech.daoyin_release.enums.reservation.ReservationType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lj on 2018/9/19 9:34
 */
@Entity
@Data
@Table(name = "Reservation")
@EqualsAndHashCode(callSuper = true)
public class Reservation extends AbstractEntity{

    @Column
    private Long userId;

    @Column
    private String userName;

    @Column
    private String phone;

    @Column
    private Date appointmentDate; //预约时间

    @Column
    @Enumerated(EnumType.STRING)
    private ReservationType reservationType;

    @Column
    private Boolean dispose = false;

}

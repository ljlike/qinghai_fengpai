package com.daoyintech.daoyin_release.repository.reservation;

import com.daoyintech.daoyin_release.entity.reservation.Reservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by lj on 2018/9/19 9:44
 */
@Repository
public interface ReservationRepository extends CrudRepository<Reservation,Long>{

    Integer countByAppointmentDate(Date appointmentDate);

}

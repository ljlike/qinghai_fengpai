package com.daoyintech.daoyin_release.service.impl.reservation;

import com.daoyintech.daoyin_release.entity.reservation.Reservation;
import com.daoyintech.daoyin_release.repository.reservation.ReservationRepository;
import com.daoyintech.daoyin_release.response.DefinitionResponse;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.reservation.ReservationResponse;
import com.daoyintech.daoyin_release.service.reservation.ReservationService;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.daoyintech.daoyin_release.response.DefinitionResponse.DEFAULT_RESERVATION_NUMBER;

/**
 * Created by lj on 2018/9/19 9:54
 */
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private RedisTemplate<Object, Object> redis;

    @Autowired
    private ReservationRepository repository;

    @Override
    public ResultResponse createReservationInfo(String userName, String phone, String strDate) {
        Date appointmentDate = changeDateFormat(strDate);
        if (appointmentDate != null && checkReservationDate(appointmentDate)) {
            if (checkReservationNumber(appointmentDate)) {
                Reservation reservation = new Reservation();
                reservation.setUserName(userName);
                reservation.setPhone(phone);
                reservation.setAppointmentDate(appointmentDate);
                repository.save(reservation);
                return ResultResponseUtil.success("预约成功");
            }
            return ResultResponseUtil.error(2,"当日预约已满,请选择其他时段");
        } else {
            return ResultResponseUtil.error(3,"请选择正确的预约时间");
        }
    }

    private Date changeDateFormat(String strDate) {
        Date appointmentDate = null;
        try {
            appointmentDate = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return appointmentDate;
    }

    /**
     * 检查用户提交的日期是否正确
     *
     * @param appointmentDate 用户提交的预约日期
     * @return
     */
    private boolean checkReservationDate(Date appointmentDate) {
        Long dateTime = appointmentDate.getTime();
        return dateTime - System.currentTimeMillis() > 0;
    }

    /**
     * 检查预约日期当天的预约人数是否满员
     *
     * @param appointmentDate 用户提交的预约日期
     * @return
     */
    private boolean checkReservationNumber(Date appointmentDate) {
        Integer reservationNumber = repository.countByAppointmentDate(appointmentDate);
        ReservationResponse response = (ReservationResponse) redis.opsForValue().get(DefinitionResponse.getReservationDate(appointmentDate));
        if (response == null) {
            return DEFAULT_RESERVATION_NUMBER - reservationNumber > 0;
        } else {
            return response.getMaxReservationNumber() - reservationNumber > 0 && response.getIsCameramanOut();
        }
    }


}

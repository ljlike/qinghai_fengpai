package com.daoyintech.daoyin_release.service.impl.reservation;

import com.daoyintech.daoyin_release.entity.reservation.Reservation;
import com.daoyintech.daoyin_release.enums.reservation.ReservationType;
import com.daoyintech.daoyin_release.repository.reservation.ReservationRepository;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.reservation.ReservationResponse;
import com.daoyintech.daoyin_release.service.reservation.ReservationService;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import com.daoyintech.daoyin_release.utils.helper.ReservationResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.daoyintech.daoyin_release.utils.helper.ReservationResponseHelper.DEFAULT_RESERVATION_NUMBER;

/**
 * Created by lj on 2018/9/19 9:54
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private RedisTemplate<Object, Object> redis;

    @Autowired
    private ReservationRepository repository;

    @Override
    public ResultResponse createReservationInfo(Long userId, String userName, String phone, String strDate, String reservationType) {
        Date appointmentDate = changeDateFormat(strDate);
        if (appointmentDate != null && checkReservationDate(appointmentDate)) {
            Reservation reservation = new Reservation();
            reservation.setUserId(userId);
            reservation.setUserName(userName);
            reservation.setPhone(phone);
            reservation.setAppointmentDate(appointmentDate);
            reservation.setReservationType(ReservationType.getReservationType(reservationType));
            if ("拍照".equals(reservationType) && checkReservationNumber(appointmentDate)) {
                repository.save(reservation);
                return ResultResponseUtil.success("预约成功");
            } else if ("选片".equals(reservationType)) {
                repository.save(reservation);
                return ResultResponseUtil.success("预约成功");
            }
            return ResultResponseUtil.error(3, "当日预约已满,请选择其他时段");
        } else {
            return ResultResponseUtil.error(4, "请选择正确的预约时间");
        }
    }

    @Override
    public List<Reservation> selectByReservationType(String type) {
        ReservationType reservationType = ReservationType.getReservationType(type);
        List<Reservation> reservations = repository.findByReservationType(reservationType);
        return reservations;
    }

    @Override
    public void reservationDispose(String id) {
        Reservation reservation = repository.findById(Long.valueOf(id)).get();
        if (reservation.getDispose()) {
            reservation.setDispose(false);
        } else {
            reservation.setDispose(true);
        }
        repository.save(reservation);
    }


    @Override
    public List<ReservationResponse> findReservationInfoDefinedByAdmin() {
        return ReservationResponseHelper.selectAllReservationResponse(redis);
    }

    @Override
    public ResultResponse createOrUpdateReservationInfoDefinedByAdmin(String strDate, String maxReservationNumber, String isCameramanOut) {
        Date appointmentDate = changeDateFormat(strDate);
        if (appointmentDate != null) {
            ReservationResponse response = new ReservationResponse();
            response.setAppointmentDate(appointmentDate);
            response.setIsCameramanOut("true".equals(isCameramanOut));
            response.setMaxReservationNumber(StringUtils.isEmpty(maxReservationNumber) ? 10 : Integer.valueOf(maxReservationNumber));
            ReservationResponseHelper.setReservationResponse(redis, response);
            return ResultResponseUtil.success();
        } else {
            return ResultResponseUtil.error(1, "操作失败");
        }

    }

    @Override
    public String deleteReservationInfoDefinedByAdmin(String strDate) {
        Date appointmentDate = changeDateFormat(strDate);
        if (appointmentDate != null) {
            ReservationResponseHelper.deleteReservationResponse(redis, appointmentDate);
            return "删除成功";
        }else {
            return "删除失败";
        }
    }


    /**
     * 预约日期格式转换
     *
     * @param strDate 预约日期
     * @return
     */
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
        ReservationResponse response = ReservationResponseHelper.getReservationResponse(redis, appointmentDate);
        if (response == null) {
            return DEFAULT_RESERVATION_NUMBER - reservationNumber > 0;
        } else {
            return response.getMaxReservationNumber() - reservationNumber > 0 && !response.getIsCameramanOut();
        }
    }

}

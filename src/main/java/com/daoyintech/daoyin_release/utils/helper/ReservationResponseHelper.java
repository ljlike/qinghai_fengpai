package com.daoyintech.daoyin_release.utils.helper;

import com.daoyintech.daoyin_release.response.reservation.ReservationResponse;
import org.springframework.data.redis.core.RedisTemplate;

import javax.validation.constraints.Null;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lj on 2018/9/20 9:15
 */
public class ReservationResponseHelper {

    public static final String RESERVATION_DATE = "reservation_date";

    public static final Integer DEFAULT_RESERVATION_NUMBER = 10;

    public static String getReservationKey(Date date) {
        Long time = date.getTime();
        return RESERVATION_DATE + "-" + time;
    }

    public static String getReservationKey(Long key) {
        return RESERVATION_DATE + "-" + key;
    }

    public static ReservationResponse getReservationResponse(RedisTemplate<Object, Object> redis, Date appointmentDate) {
        return (ReservationResponse) redis.opsForValue().get(getReservationKey(appointmentDate));
    }

    public static void setReservationResponse(RedisTemplate<Object, Object> redis, ReservationResponse response) {
        redis.opsForValue().set(getReservationKey(response.getAppointmentDate()), response);
    }

    public static void deleteReservationResponse(RedisTemplate<Object, Object> redis, Date appointmentDate) {
        redis.delete(getReservationKey(appointmentDate));
    }

    public static List<ReservationResponse> selectAllReservationResponse(RedisTemplate<Object, Object> redis) {
        List<Long> keys = getKeys(redis);
        List<ReservationResponse> result = new ArrayList<>();
        for (Long key : keys) {
            String sKey = getReservationKey(key);
            ReservationResponse response = (ReservationResponse) redis.opsForValue().get(sKey);
            result.add(response);
        }
        return result;
    }

    private static List<Long> getKeys(RedisTemplate<Object, Object> redis) {
        Set<Object> keys = redis.keys(RESERVATION_DATE + "*");
        List<Long> newKeys = new ArrayList<>();
        Long currentTime = System.currentTimeMillis();
        for (Object key : keys) {
            String[] strs = String.valueOf(key).split("-");
            Long newKey = Long.valueOf(strs[1]);
            if (newKey - currentTime > 0) {
                newKeys.add(newKey);
            }
        }
        newKeys.sort(Comparator.comparing(Long::longValue).reversed());
        return newKeys;
    }

}

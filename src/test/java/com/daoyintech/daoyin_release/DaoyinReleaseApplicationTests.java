package com.daoyintech.daoyin_release;

import com.daoyintech.daoyin_release.response.DefinitionResponse;
import com.daoyintech.daoyin_release.response.reservation.ReservationResponse;
import com.daoyintech.daoyin_release.utils.helper.ReservationResponseHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.SoundbankResource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DaoyinReleaseApplicationTests {

    @Autowired
    private RedisTemplate<Object, Object> redis;

    @Test
    public void contextLoads() {
        /*setRedis("2018-10-10");*/
        search();
    }

    private void setRedis(String strDate) {

        ReservationResponse response = new ReservationResponse();
        response.setMaxReservationNumber(10);
        response.setIsCameramanOut(false);
        try {
            response.setAppointmentDate(new SimpleDateFormat("yyyy-MM-dd").parse(strDate));
            ReservationResponseHelper.setReservationResponse(redis, response);
            System.out.println("success");
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("filed");
        }

    }

    private void search(){
        List<ReservationResponse> responses = ReservationResponseHelper.selectAllReservationResponse(redis);
        for (ReservationResponse response : responses) {
            System.out.println(response);
        }
    }

}

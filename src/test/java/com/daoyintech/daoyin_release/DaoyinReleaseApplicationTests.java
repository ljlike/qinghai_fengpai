package com.daoyintech.daoyin_release;

import com.daoyintech.daoyin_release.response.reservation.ReservationResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DaoyinReleaseApplicationTests {

    @Autowired
    private RedisTemplate<Object,Object> redis;

    @Test
    public void contextLoads() {
        ReservationResponse response = new ReservationResponse();
        response.setIsCameramanOut(false);
        response.setMaxReservationNumber(10);
        redis.opsForValue().set("test",response);

        ReservationResponse test = (ReservationResponse) redis.opsForValue().get("test");
        System.out.println(test);
        System.out.println("!!!!!!!!!!!!");
    }

   /* @Autowired
    private WxMaService wxMaService;

    @Autowired
    private UserService userService;

    @Value("${wx.service_open_id}")
    private String serviceOpenId;

    @Test
    public void contextLoads() {
        String unionId = getCurrentUnionId();
        User user = userService.findByUnionId(unionId);
        WxMaTemplateMessage wxMaTemplateMessage = new WxMaTemplateMessage();
        wxMaTemplateMessage.setToUser(serviceOpenId);
        wxMaTemplateMessage.setTemplateId("kWjqHoFV2Tzi2Q4rXcSfwUgM73qnaqUk4rCi3tcMThk");
        wxMaTemplateMessage.setPage("pages/index/mian"); //跳转页面 非必填
        wxMaTemplateMessage.setFormId("");
        List<WxMaTemplateMessage.Data> data = new ArrayList<>();
        data.add(new WxMaTemplateMessage.Data("预约客户",user.getNickName()));
        data.add(new WxMaTemplateMessage.Data("手机号","前端用户输入数据"));
        data.add(new WxMaTemplateMessage.Data("预约时间","前端用户输入数据"));
        wxMaTemplateMessage.setData(data);
        try {
            wxMaService.getMsgService().sendTemplateMsg(wxMaTemplateMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }*/



}

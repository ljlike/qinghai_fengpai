package com.daoyintech.daoyin_release.controller.customer.reservation;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lj on 2018/9/19 14:59
 */
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private WxMaService wxMaService;

    @Value("${wx.service_open_id}")
    private String serviceOpenId;

    @Value("${wx.service_template_id}")
    private String serviceTemplateId;

    @Value("${wx.home_page}")
    private String homePage;

    @PostMapping()
    public ResultResponse getReservationInfo(String userName, String phone, String appointmentDate, String formId) {
        WxMaTemplateMessage message = new WxMaTemplateMessage();
        message.setToUser(serviceOpenId);
        message.setTemplateId(serviceTemplateId);
        message.setPage(homePage); //跳转页面 非必填
        message.setFormId(formId);
        message.addData(new WxMaTemplateMessage.Data("预约客户", userName));
        message.addData(new WxMaTemplateMessage.Data("手机号", phone));
        message.addData(new WxMaTemplateMessage.Data("预约时间", appointmentDate));

        /*List<WxMaTemplateMessage.Data> data = new ArrayList<>();
        data.add(new WxMaTemplateMessage.Data("预约客户", userName));
        data.add(new WxMaTemplateMessage.Data("手机号", phone));
        data.add(new WxMaTemplateMessage.Data("预约时间", appointmentDate));
        message.setData(data);*/
        System.out.println(message.toJson());
        try {
            wxMaService.getMsgService().sendTemplateMsg(message);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return ResultResponseUtil.error(1, e);
        }
        return ResultResponseUtil.success();
    }
}

package com.daoyintech.daoyin_release.controller.customer.reservation;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import com.daoyintech.daoyin_release.controller.customer.user.BaseUserController;
import com.daoyintech.daoyin_release.entity.reservation.Reservation;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.reservation.ReservationResponse;
import com.daoyintech.daoyin_release.service.user.UserService;
import com.daoyintech.daoyin_release.service.reservation.ReservationService;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by lj on 2018/9/19 14:59
 */
@Controller
@RequestMapping("/reservation")
@Slf4j
public class ReservationController extends BaseUserController {

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @Value("${wx.template_id}")
    private String serviceTemplateId;

    @Value("${wx.home_page}")
    private String homePage;

    @Value("${wx.service_open_id}")
    private String serviceOpenId;

    @GetMapping("/show")
    public String searchReservation() {
        return "function/search_reservation";
    }

    @GetMapping("/restrain")
    public String showReservationRestrain(){
        return "show/reservation_restrain";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResultResponse addReservationRestrain(String appointmentDate,String maxReservationNumber,String isCameramanOut){
        return reservationService.createOrUpdateReservationInfoDefinedByAdmin(appointmentDate,maxReservationNumber,isCameramanOut);
    }

    @GetMapping("/{appointmentDate}/delete")
    public String deleteReservationDisposeDelete(@PathVariable String appointmentDate, Model model){
        String msg = reservationService.deleteReservationInfoDefinedByAdmin(appointmentDate);
        model.addAttribute("message",msg);
        return "result/result";
    }

    @GetMapping("/search")
    public String showReservation(Model model) {
        List<ReservationResponse> responses = reservationService.findReservationInfoDefinedByAdmin();
        model.addAttribute("reservations", responses);
        return "show/reservation";
    }

    @GetMapping("/{type}/search")
    public String searchReservationByType(@PathVariable String type, Model model){
        List<Reservation> reservations = reservationService.selectByReservationType(type);
        model.addAttribute("reservations",reservations);
        return "show/reservation_type";
    }

    @GetMapping("/{id}/dispose")
    public String reservationDispose(@PathVariable String id, Model model){
        reservationService.reservationDispose(id);
        model.addAttribute("message","预约信息处理成功");
        return "result/result";
    }

    @GetMapping("/dispose")
    public String reservationDisposeResult(String message, Model model){
        model.addAttribute("message",message);
        return "result/result";
    }

    @PostMapping()
    @ResponseBody
    public ResultResponse getReservationInfo(String userName, String phone, String appointmentDate, String reservationType, String formId) {
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(1,"请重新登录授权");
        }
        User user = userService.findByUnionId(unionId);

        ResultResponse response = reservationService.createReservationInfo(user.getId(), userName, phone, appointmentDate, reservationType);
        if (response.getCode() == 0) {
            WxMaTemplateMessage message = createTemplateMessage(user, userName, phone, appointmentDate, reservationType, formId);
            WxMaKefuMessage keFuMessage = createKeFuMessage(userName, phone, appointmentDate, reservationType);
            try {
                wxMaService.getMsgService().sendTemplateMsg(message);
                wxMaService.getMsgService().sendKefuMsg(keFuMessage);
            } catch (WxErrorException e) {
                log.error(e.getMessage());
                return ResultResponseUtil.error(2, "模板消息发送失败");
            }
            return ResultResponseUtil.success("预约成功");
        } else {
            return response;
        }
    }

    private WxMaTemplateMessage createTemplateMessage(User user, String userName, String phone, String appointmentDate, String reservationType, String formId) {
        WxMaTemplateMessage message = new WxMaTemplateMessage();
        message.setToUser(user.getAppletOpenId());
        message.setTemplateId(serviceTemplateId);
        message.setPage(homePage); //跳转页面 非必填
        message.setFormId(formId);
        message.addData(new WxMaTemplateMessage.Data("keyword1", userName));
        message.addData(new WxMaTemplateMessage.Data("keyword2", phone));
        message.addData(new WxMaTemplateMessage.Data("keyword3", appointmentDate));
        message.addData(new WxMaTemplateMessage.Data("keyword4", reservationType));
        message.addData(new WxMaTemplateMessage.Data("keyword5", "请等待客服人员与您联系!"));
        return message;
    }

    private WxMaKefuMessage createKeFuMessage(String userName, String phone, String appointmentDate, String reservationType) {
        WxMaKefuMessage keFuMessage = new WxMaKefuMessage();
        keFuMessage.setToUser(serviceOpenId);
        keFuMessage.setMsgType("text");
        String content = new StringBuilder("用户:").append(userName).append("\r\n")
                .append("联系方式:").append(phone).append("\r\n")
                .append("预约时间:").append(appointmentDate).append("\r\n")
                .append("预约项目:").append(reservationType).append("\r\n")
                .append("请尽快联系客户,确认预约时间!")
                .toString();
        keFuMessage.setText(new WxMaKefuMessage.KfText(content));
        return keFuMessage;
    }
}

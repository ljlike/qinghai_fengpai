package com.daoyintech.daoyin_release.controller.customer.order;

import com.daoyintech.daoyin_release.service.order.OrderNoticeService;
import com.daoyintech.daoyin_release.utils.DateUtils;
import com.daoyintech.daoyin_release.utils.pay.NotifyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.util.*;

/**
 * @author pei on 2018/08/16
 */
@Slf4j
@RestController
@RequestMapping("/pay")
public class OrderNoticeController {

    @Autowired
    private OrderNoticeService orderNoticeService;

    /**
     * 支付回调通知
     * @param request
     * @throws Exception
     */
    @RequestMapping("/notify")
    public void notice(HttpServletRequest request,HttpServletResponse response) throws Exception {
        log.info("{}:微信支付后异步通知开始",DateUtils.getStringDate());
        SortedMap<Object, Object> packageParams = NotifyUtils.noticeRequestResponse(request);
        log.info("{}:微信支付后异步通知:packageParams = {}", DateUtils.getStringDate(),packageParams);
        orderNoticeService.notice(packageParams);
        String xmlStr = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(xmlStr.getBytes());
        out.flush();
        out.close();
    }

}












package com.daoyintech.daoyin_release.controller.customer.user;

import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.repository.order.OrderRepository;
import com.daoyintech.daoyin_release.repository.user.UserRepository;
import com.daoyintech.daoyin_release.service.*;
import com.daoyintech.daoyin_release.service.order.OrderService;
import com.daoyintech.daoyin_release.service.order.bargain.BargainJoinerHelpService;
import com.daoyintech.daoyin_release.service.order.bargain.BargainOrderService;
import com.daoyintech.daoyin_release.service.user.UserIntegralService;
import com.daoyintech.daoyin_release.service.user.UserService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
/**
 * @author pei on 2018/08/10
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private BargainOrderService bargainOrderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private DrawService drawService;

    @Autowired
    private BargainJoinerHelpService helpService;

    @Autowired
    private UserIntegralService userIntegralService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void wxUserInfo() throws Exception {
//        User user = userService.findByUnionId("omOng0a9TgpSMI_9uEu-jyeEXGTk");
        //BargainOrder bargainOrder = bargainOrderService.create(108L, user,352L,100L);

//        User user = userService.findByUnionId("omOng0f_VWxuUjJSp33RXVN-dlOM");
//        BargainOrder bargainOrder = bargainOrderService.findOne(1175L);
//
//        BargainJoiner bargainJoiner = helpService.bargainDrawHelp(bargainOrder,user);

        Order order = orderRepository.findByOrderNo("oq0akz1537339578296");
        userIntegralService.userIntegralDraw(order);

    }




}











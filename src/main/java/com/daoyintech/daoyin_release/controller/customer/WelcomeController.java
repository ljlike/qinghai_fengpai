package com.daoyintech.daoyin_release.controller.customer;

import com.daoyintech.daoyin_release.controller.customer.user.BaseUserController;
import com.daoyintech.daoyin_release.enums.ProductTypeStatus;
import com.daoyintech.daoyin_release.enums.ResultEnum;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.service.CartService;
import com.daoyintech.daoyin_release.service.UserService;
import com.daoyintech.daoyin_release.service.WelcomeService;
import com.daoyintech.daoyin_release.utils.DateUtils;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class WelcomeController extends BaseUserController{

    @Autowired
    private WelcomeService welcomeService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping("/welcome")
    @ApiOperation("加载首页数据")
    public ResultResponse get(){
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            ResultResponseUtil.error(ResultEnum.PARAM_ERROR.getCode(),"请授权登录");
        }
        //log.info("{}:首页welcome获取:unionId = {}", DateUtils.getStringDate(),unionId);
        cartService.countByCart(unionId);
        Long userId = userService.findByUnionId(unionId).getId();
        return ResultResponseUtil.success(welcomeService.findWelcomeResponseBy(ProductTypeStatus.上架,true,userId));
    }

}
package com.daoyintech.daoyin_release.controller.customer;

import com.daoyintech.daoyin_release.controller.customer.user.BaseUserController;
import com.daoyintech.daoyin_release.enums.ProductTypeStatus;
import com.daoyintech.daoyin_release.enums.ResultEnum;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.WelcomeResponse;
import com.daoyintech.daoyin_release.service.user.UserService;
import com.daoyintech.daoyin_release.service.WelcomeService;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import io.swagger.annotations.ApiOperation;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class WelcomeController extends BaseUserController{

    @Autowired
    private WelcomeService welcomeService;

    @Autowired
    private UserService userService;

    @GetMapping("/welcome")
    @ApiOperation("加载首页数据")
    public ResultResponse get(){
        String unionId = getCurrentUnionId();
        if (StringUtil.isNotEmpty(unionId)){
            Long userId = userService.findByUnionId(unionId).getId();
            WelcomeResponse response = welcomeService.findWelcomeResponseBy(ProductTypeStatus.上架, true, userId);
            return ResultResponseUtil.success(response);
        }
        return ResultResponseUtil.error(ResultEnum.PARAM_ERROR.getCode(),"请授权登录");
    }
}
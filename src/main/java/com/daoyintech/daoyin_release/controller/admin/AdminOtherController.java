package com.daoyintech.daoyin_release.controller.admin;

import com.daoyintech.daoyin_release.controller.customer.user.BaseUserController;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.service.user.UserCardService;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pei on 2018/08/09
 */
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminOtherController extends BaseUserController{

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserCardService userCardService;

    @ApiOperation("unionId:12345678:测试")
    @GetMapping("/renpei")
    public ResultResponse renpeiSessionUnionId(){
        setCurrentUser("12345678");
        return ResultResponseUtil.success();
    }

    @ApiOperation("unionId:omOng0Vu8X0G0PyOI84QTwttfR4A:测试")
    @GetMapping("/lujiang")
    public ResultResponse lujiangSessionUnionId(){
        setCurrentUser("o9C7s5xw4uV1XrGvWYncH_10oOF8"); //鲁江
        return ResultResponseUtil.success();
    }

}
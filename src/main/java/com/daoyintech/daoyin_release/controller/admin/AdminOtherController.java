package com.daoyintech.daoyin_release.controller.admin;

import com.daoyintech.daoyin_release.controller.customer.user.BaseUserController;
import com.daoyintech.daoyin_release.enums.Integral.IntegralConstant;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.service.UserCardService;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        setCurrentUser("omOng0Vu8X0G0PyOI84QTwttfR4A");
        return ResultResponseUtil.success();
    }

   /* @ApiOperation("初始化redis积分奖池")
    @GetMapping("/redis")
    public ResultResponse initializeRedisJackpot(@ApiParam("初始积分")@RequestParam String integral){
        redisTemplate.opsForValue().set(IntegralConstant.JACKPOT_DRAW_INTEGRAL,integral);
        return ResultResponseUtil.success();
    }

    @ApiOperation("查询redis积分奖池剩余积分")
    @GetMapping("/find")
    public ResultResponse findRedisJackpot(){
        String integral = redisTemplate.opsForValue().get(IntegralConstant.JACKPOT_DRAW_INTEGRAL);
        return ResultResponseUtil.success(integral);
    }*/


   /* @ApiOperation("用户卡卷数据转移更新")
    @GetMapping("/userCardToNew")
    public ResultResponse userCardToNew(){
        userCardService.userCardToNew();
        return ResultResponseUtil.success();
    }*/



}
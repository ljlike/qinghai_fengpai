package com.daoyintech.daoyin_release.controller.customer.user;

import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.entity.user.integral.UserIntegral;
import com.daoyintech.daoyin_release.entity.user.integral.UserIntegralDetail;
import com.daoyintech.daoyin_release.enums.ResultEnum;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.user.UserIntegralDetailResponse;
import com.daoyintech.daoyin_release.response.user.UserResponse;
import com.daoyintech.daoyin_release.service.PrizeService;
import com.daoyintech.daoyin_release.service.user.UserIntegralDetailService;
import com.daoyintech.daoyin_release.service.user.UserIntegralService;
import com.daoyintech.daoyin_release.service.user.UserService;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;


@RequestMapping("/integral")
@RestController
public class UserIntegralController extends BaseUserController{

    @Autowired
    private UserService userService;

    @Autowired
    private UserIntegralDetailService userIntegralDetailService;

    @Autowired
    private UserIntegralService userIntegralService;

    @Autowired
    private PrizeService prizeService;

    @Value("${pay.freight_price}")
    private BigDecimal freight_price;

    /**
     * 我的积分
     * */
    @ApiOperation("我的积分")
    @GetMapping("/detail/{type}/{page}")
    public ResultResponse getAll(@ApiParam("积分类型,0:帮助朋友,1:购物消费,2:取消订单,3:订单分润,4:提现,5:关注公众号,6:退款")
                                  @PathVariable(name = "type") int type,
                                 @ApiParam("页数,第一页开始")@PathVariable(name = "page")int page){
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        List<UserIntegralDetail> userIntegralDetails = userIntegralDetailService.findByUserIdAndType(user,page-1,type);
        List<UserIntegralDetailResponse> userIntegralDetailResponseList = userIntegralDetailService.transformUserIntegralDetailModel(userIntegralDetails);
        return ResultResponseUtil.success(userIntegralDetailResponseList);
    }

    /**
     * 最新积分记录
     * */
    @ApiOperation("最新积分记录")
    @GetMapping("/detail/new")
    public ResultResponse findNewEstIntegral(){
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        List<UserIntegralDetail> userIntegralDetails = userIntegralDetailService.findByUserIdToNewest(user);
        List<UserIntegralDetailResponse> userIntegralDetailResponseList = userIntegralDetailService.transformUserIntegralDetailModel(userIntegralDetails);
        return ResultResponseUtil.success(userIntegralDetailResponseList);
    }


    /**
     * 我的积分,更新积分
     * */
    @ApiOperation("我的实时积分")
    @GetMapping("/my/integral")
    public ResultResponse findByMyNewIntegral(){
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        UserIntegral integral = userIntegralService.findByUserId(user.getId());
        UserResponse userResponse = UserResponse.userTransformUserResponse(user,integral.getIntegral(),freight_price);
        return ResultResponseUtil.success(userResponse);
    }


    /**
     * 我参与抽奖的记录
     * */
    @ApiOperation("我参与抽奖的记录")
    @GetMapping("/my/help")
    public ResultResponse findAllMyHelp(){
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        return userIntegralService.findAllMyHelp(user);
    }


    /**
     * 奖池剩余积分
     * */
    @ApiOperation("奖池剩余积分")
    @GetMapping("/jackpot")
    public ResultResponse findJackpotIntegral(){
        return prizeService.findJackpotIntegral();
    }






}













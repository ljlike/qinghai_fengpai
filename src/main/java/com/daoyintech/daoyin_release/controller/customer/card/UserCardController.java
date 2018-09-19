package com.daoyintech.daoyin_release.controller.customer.card;

import com.daoyintech.daoyin_release.controller.customer.user.BaseUserController;
import com.daoyintech.daoyin_release.entity.card.UserCard;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.enums.ResultEnum;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.card.UserCardResponse;
import com.daoyintech.daoyin_release.service.UserCardService;
import com.daoyintech.daoyin_release.service.UserService;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.bean.WxCardApiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author pei on 2018/08/22
 */
@RestController
@RequestMapping("/card")
public class UserCardController extends BaseUserController{

    @Autowired
    private UserService userService;

    @Autowired
    private UserCardService userCardService;

    @ApiOperation("卡包展示,获取用户所有卡包记录")
    @GetMapping
    public ResultResponse getUserCards(){
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        //List<UserCardResponse> cardList = userCardService.findUsercards(user);
        List<UserCardResponse> cardList = userCardService.findNewUsercards(user);
        return ResultResponseUtil.success(cardList);
    }


    /**
     * 唤起h5领取卡包  参数
     * */
/*
    @ApiOperation("唤起h5领取卡包  参数")
    @GetMapping("/own_card/{id}")
    public ResultResponse getH5Param(@PathVariable("id") Long id) throws WxErrorException {
        WxCardApiSignature signature = userCardService.getH5Param(id);
        return ResultResponseUtil.success(signature);
    }
*/







}






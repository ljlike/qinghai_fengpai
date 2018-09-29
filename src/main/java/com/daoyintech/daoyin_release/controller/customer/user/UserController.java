package com.daoyintech.daoyin_release.controller.customer.user;

import com.daoyintech.daoyin_release.enums.ResultEnum;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.result.ProductResult;
import com.daoyintech.daoyin_release.service.product.ProductService;
import com.daoyintech.daoyin_release.service.user.UserService;
import com.daoyintech.daoyin_release.service.user.WxUserInfoService;
import com.daoyintech.daoyin_release.utils.DateUtils;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import com.daoyintech.daoyin_release.utils.qiniu.ImageScalaTool;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;

/**
 * @author pei on 2018/08/09
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseUserController{

    @Autowired
    private UserService userService;

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageScalaTool imageScalaTool;

    /**
     * 初次初始化用户信息  已关注
     * */
    @ApiOperation("初次初始化用户信息,unionid未知")
    @PostMapping("/firstInit")
    public ResultResponse firstInitUserInfo(@ApiParam("微信code")@RequestParam("code") String code){
        String unionId = null;
        try {
            unionId = wxUserInfoService.sendPostGetUnionId(code);
        } catch (Exception e) {
            log.error("{}:unionId获取失败:{}",DateUtils.getStringDate(),e.getMessage());
            return ResultResponseUtil.error(ResultEnum.USER_ERROR.getCode(),"获取信息失败,请重新登录");
        }
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"该用户未关注公众号");
        }
        return userService.firstInitUserInfo(unionId);
    }

    /**
     * 再次初始化用户信息  未关注
     * */
    @ApiOperation("再次初始化用户信息,没有unionId")
    @PostMapping("/againInit")
    public ResultResponse againInitUserInfo(@ApiParam("微信code")@RequestParam("code") String code,
                                            @ApiParam("加密串")@RequestParam("encryptedData") String encryptedData,
                                            @ApiParam("加密串")@RequestParam("iv") String iv){
        HashMap<String, Object> userInfoMap = null;
        try {
            userInfoMap = wxUserInfoService.decryptUserInfo(code, encryptedData, iv);
            log.info("{}:解密后用户信息:{}",DateUtils.getStringDate(),userInfoMap);
        } catch (Exception e) {
            log.error("{}:解密获取用户信息失败:{}",DateUtils.getStringDate(),e.getMessage());
            return ResultResponseUtil.error(ResultEnum.USER_ERROR.getCode(),"用户信息获取失败");
        }
        return userService.againInitUserInfo(userInfoMap);
    }

    @ApiOperation("查询收藏列表")
    @GetMapping("/favorites")
    public ResultResponse favorites(){
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        Long userId = userService.findByUnionId(unionId).getId();
        List<ProductResult> results = productService.selectFavoriteProductsByUseId(userId);
        if(results==null|results.size()==0){
            return ResultResponseUtil.error(ResultEnum.NO_MATCHING_GOODS_WHERE_FOUND.getCode(),ResultEnum.NO_MATCHING_GOODS_WHERE_FOUND.getMessage());
        }else {
            return ResultResponseUtil.success(results);
        }
    }


   /* @ApiOperation("我的二维码")
    @GetMapping("/qrCode")
    public ResultResponse QrCodePic() throws QiniuException {
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        return ResultResponseUtil.success(imageScalaTool.generateUserPic(user));
    }
*/






}
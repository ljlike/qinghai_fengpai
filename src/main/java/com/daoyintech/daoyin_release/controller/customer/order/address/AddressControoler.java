package com.daoyintech.daoyin_release.controller.customer.order.address;

import com.daoyintech.daoyin_release.controller.customer.user.BaseUserController;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.entity.user.UserAddress;
import com.daoyintech.daoyin_release.enums.ResultEnum;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.user.UserAddressRequest;
import com.daoyintech.daoyin_release.response.user.UserAddressResponse;
import com.daoyintech.daoyin_release.service.user.UserAddressService;
import com.daoyintech.daoyin_release.service.user.UserService;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author pei on 2018/08/14
 */
@RestController
@RequestMapping("/address")
@Slf4j
public class AddressControoler extends BaseUserController{

    @Autowired
    private UserService userService;

    @Autowired
    private UserAddressService userAddressService;

    /**
     * 地址列表
     * @return
     */
    @ApiOperation("收货地址列表")
    @GetMapping("/findAll")
    public ResultResponse addresses(){
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        List<UserAddressResponse> userAddressResponseList = userAddressService.findByUserId(user.getId());
        log.info("收货地址列表:{}",userAddressResponseList);
        return ResultResponseUtil.success(userAddressResponseList);
    }

    /**
     * 添加地址
     * @param userAddressRequest
     * @return
     */
    @ApiOperation("添加收货地址")
    @PostMapping("/new")
    public ResultResponse newAddress(@RequestBody UserAddressRequest userAddressRequest){
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        UserAddress userAddress = userAddressService.createAddress(userAddressRequest,user.getId());
        UserAddressResponse transformResponse = UserAddressResponse.userAddressTransformResponse(userAddress);
        return ResultResponseUtil.success(transformResponse);
    }

    /**
     * 修改地址
     * @param addressId
     * @param userAddressRequest
     * @return
     */
    @ApiOperation("修改收货地址")
    @PostMapping("/{addressId}")
    public ResultResponse updateAddress(@ApiParam("地址id")@PathVariable Long addressId ,
                                        @RequestBody UserAddressRequest userAddressRequest){
        UserAddress userAddress = userAddressService.updateAddress(addressId, userAddressRequest);
        UserAddressResponse transformResponse = UserAddressResponse.userAddressTransformResponse(userAddress);
        return ResultResponseUtil.success(transformResponse);
    }

    /**
     * 删除地址
     * @param addressId
     * @return
     */
    @ApiOperation("删除收货地址")
    @DeleteMapping("/{addressId}")
    public ResultResponse deleteAddress(@ApiParam("地址id") @PathVariable Long addressId){
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        userAddressService.deleteAddress(addressId,user.getId());
        return ResultResponseUtil.success();
    }

    /**
     * 设置默认地址
     * @param addressId
     * @return
     */
    @ApiOperation("设置默认地址")
    @GetMapping("/{addressId}/setDefault")
    public ResultResponse setDefaultAddress(@ApiParam("地址id") @PathVariable Long addressId){
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        userAddressService.defaultAddress(addressId,user.getId());
        return ResultResponseUtil.success();
    }

    /**
     * 获取默认地址
     * @return
     */
    @ApiOperation("获取默认地址")
    @GetMapping("/default")
    public ResultResponse defaultAddress(){
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        return userAddressService.findDefaultByUserId(user.getId());
    }



}
















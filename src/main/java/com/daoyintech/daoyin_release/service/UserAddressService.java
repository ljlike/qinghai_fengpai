package com.daoyintech.daoyin_release.service;

import com.daoyintech.daoyin_release.entity.user.UserAddress;
import com.daoyintech.daoyin_release.entity.user.UserSetting;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.user.UserAddressRequest;
import com.daoyintech.daoyin_release.response.user.UserAddressResponse;

import java.util.List;

/**
 * @author pei on 2018/08/14
 */
public interface UserAddressService {

    List<UserAddressResponse> findByUserId(Long userId);


    UserAddress createAddress(UserAddressRequest userAddressRequest, Long userId);


    UserAddress updateAddress(Long addressId, UserAddressRequest userAddressRequest);


    void deleteAddress(Long addressId,Long userId);


    UserSetting defaultAddress(Long addressId, Long userId);


    ResultResponse findDefaultByUserId(Long userId);


    UserAddress findById(Long addressId);


}

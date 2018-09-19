package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.user.UserAddress;
import com.daoyintech.daoyin_release.entity.user.UserSetting;
import com.daoyintech.daoyin_release.repository.user.UserAddressRepository;
import com.daoyintech.daoyin_release.repository.user.UserSettingRepository;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.user.UserAddressRequest;
import com.daoyintech.daoyin_release.response.user.UserAddressResponse;
import com.daoyintech.daoyin_release.service.UserAddressService;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author pei on 2018/08/14
 */
@Service
public class UserAddressServiceImpl implements UserAddressService {


    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserSettingRepository userSettingRepository;

    @Override
    public List<UserAddressResponse> findByUserId(Long userId) {
        List<UserAddress> list = userAddressRepository.findByUserId(userId);
        if (list != null && list.size() != 0){
            return Lists.transform(list, input -> UserAddressResponse.userAddressTransformResponse(input,checkIsDefault(userId,input.getId())));
        }
        return null;
    }

    @Transactional
    @Override
    public UserAddress createAddress(UserAddressRequest userAddressRequest,Long userId) {
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressRequest,userAddress);
        userAddress.setUserId(userId);
        UserAddress address = userAddressRepository.save(userAddress);
        return address;
    }

    @Transactional
    @Override
    public UserAddress updateAddress(Long addressId, UserAddressRequest userAddressRequest) {
        UserAddress userAddress = userAddressRepository.getOne(addressId);
        BeanUtils.copyProperties(userAddressRequest,userAddress);
        UserAddress address = userAddressRepository.save(userAddress);
        return address;
    }

    @Override
    public void deleteAddress(Long addressId,Long userId) {
        UserSetting userSetting = userSettingRepository.findByUserIdAndUserDefaultAddressId(userId,addressId);
        if (userSetting != null){
            userSettingRepository.delete(userSetting);
        }
        userAddressRepository.deleteById(addressId);
    }

    @Override
    public UserSetting defaultAddress(Long addressId, Long userId) {
        UserSetting userSetting = userSettingRepository.findByUserId(userId);
        if (userSetting != null){
            userSetting.setUserDefaultAddressId(addressId);
        }else{
            userSetting = new UserSetting();
            userSetting.setUserId(userId);
            userSetting.setUserDefaultAddressId(addressId);
        }
        return userSettingRepository.save(userSetting);
    }

    @Override
    public ResultResponse findDefaultByUserId(Long userId) {
        UserSetting userSet = userSettingRepository.findByUserId(userId);
        if (userSet != null){
            UserAddress address = userAddressRepository.getOne(userSet.getUserDefaultAddressId());
            UserAddressResponse response = UserAddressResponse.userAddressTransformResponse(address, true);
            return ResultResponseUtil.success(response);
        }
        return ResultResponseUtil.success();
    }

    @Override
    public UserAddress findById(Long addressId) {
        return userAddressRepository.getOne(addressId);
    }

    private Boolean checkIsDefault(Long userId, Long addressId) {
        UserSetting userSet = userSettingRepository.findByUserIdAndUserDefaultAddressId(userId,addressId);
        if (userSet != null){
            return true;
        }
        return false;
    }

}








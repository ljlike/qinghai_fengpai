package com.daoyintech.daoyin_release.response.user;

import com.daoyintech.daoyin_release.entity.user.UserAddress;
import lombok.Data;

/**
 * Created by Administrator on 2017/8/30.
 */
@Data
public class UserAddressResponse {

    private Long id;

    private String name;

    private String phone;

    private String provinceName;

    private String cityName;

    private String districtName;

    private String street;

    private Boolean isDefault;

    public static UserAddressResponse userAddressTransformResponse(UserAddress userAddress, Boolean isDefault){
        UserAddressResponse userAddressResponse = new UserAddressResponse();
        userAddressResponse.setId(userAddress.getId());
        userAddressResponse.setName(userAddress.getName());
        userAddressResponse.setPhone(userAddress.getPhone());
        userAddressResponse.setProvinceName(userAddress.getProvince());
        userAddressResponse.setCityName(userAddress.getCity());
        userAddressResponse.setDistrictName(userAddress.getDistrict());
        userAddressResponse.setStreet(userAddress.getStreet());
        userAddressResponse.setIsDefault(isDefault);
        return userAddressResponse;
    }

    public static UserAddressResponse userAddressTransformResponse(UserAddress userAddress){
        UserAddressResponse userAddressResponse = new UserAddressResponse();
        userAddressResponse.setId(userAddress.getId());
        userAddressResponse.setCityName(userAddress.getCity());
        userAddressResponse.setName(userAddress.getName());
        userAddressResponse.setPhone(userAddress.getPhone());
        userAddressResponse.setProvinceName(userAddress.getProvince());
        userAddressResponse.setDistrictName(userAddress.getDistrict());
        userAddressResponse.setStreet(userAddress.getStreet());
        return userAddressResponse;
    }




}

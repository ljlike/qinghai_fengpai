package com.daoyintech.daoyin_release.service.user;

import java.util.HashMap;

/**
 * @author pei on 2018/08/09
 */
public interface WxUserInfoService {


    HashMap<String,Object> decryptUserInfo(String code, String encryptedData, String iv) throws Exception;


    String sendPostGetUnionId(String code) throws Exception;



}

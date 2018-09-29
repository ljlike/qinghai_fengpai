package com.daoyintech.daoyin_release.service.impl.user;

import com.daoyintech.daoyin_release.enums.ResultEnum;
import com.daoyintech.daoyin_release.exception.DaoyinException;
import com.daoyintech.daoyin_release.service.user.WxUserInfoService;
import com.daoyintech.daoyin_release.utils.DateUtils;
import com.daoyintech.daoyin_release.utils.HttpRequest;
import com.daoyintech.daoyin_release.utils.aes.AES;
import com.daoyintech.daoyin_release.utils.aes.WxPKCS7Encoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.HashMap;


/**
 * @author pei on 2018/08/09
 */
@Slf4j
@Service
public class WxUserInfoServiceImpl implements WxUserInfoService {

    private AES aes = new AES();

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.appSecret}")
    private String secret;

    @Override
    @SuppressWarnings("unchecked")
    public HashMap<String,Object> decryptUserInfo(String code, String encryptedData, String iv) throws Exception {
        String info = sendPostGetInfo(code);
        HashMap<String,Object> mapInfo = objectMapper.readValue(info, HashMap.class);
        String sessionKey = (String) mapInfo.get("session_key");
        //解密算法获取unionid
        byte[] resultByte = aes.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));
        String userInfo = null;
        if(resultByte != null && resultByte.length > 0){
            userInfo = new String(WxPKCS7Encoder.decode(resultByte),"UTF-8");
        }
        return objectMapper.readValue(userInfo, HashMap.class);
    }


    @Override
    @SuppressWarnings("unchecked")
    public String sendPostGetUnionId(String code) throws Exception {
        String info = sendPostGetInfo(code);
        log.info("{}:发送请求获取信息:info = {}",DateUtils.getStringDate(),info);
        HashMap<String,Object> resultMap = objectMapper.readValue(info, HashMap.class);
        if (StringUtils.isEmpty(resultMap.get("session_key"))){
            throw new DaoyinException(ResultEnum.USER_ERROR);
        }
        String unionId = (String) resultMap.get("unionid");
        if (StringUtils.isEmpty(unionId)){
            return null;
        }
        return unionId;
    }

    private String sendPostGetInfo(String code){
        String param = "appid=" + appId + "&secret=" + secret +"&js_code="+code+"&grant_type=authorization_code";
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        return  HttpRequest.sendGet(url,param);
    }



}

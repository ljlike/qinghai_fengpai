package com.daoyintech.daoyin_release.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.daoyintech.daoyin_release.configs.qiniu.QiniuProperties;
import com.daoyintech.daoyin_release.controller.customer.user.BaseUserController;
import com.daoyintech.daoyin_release.entity.agent.AgentBean;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.entity.user.integral.UserIntegral;
import com.daoyintech.daoyin_release.enums.ResultEnum;
import com.daoyintech.daoyin_release.enums.agent.ApplyStatus;
import com.daoyintech.daoyin_release.repository.user.UserIntegralRepository;
import com.daoyintech.daoyin_release.repository.user.UserRepository;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.bank.BankCardInfoRequest;
import com.daoyintech.daoyin_release.response.user.UserResponse;
import com.daoyintech.daoyin_release.service.AgentService;
import com.daoyintech.daoyin_release.service.UserService;
import com.daoyintech.daoyin_release.service.WxUserInfoService;
import com.daoyintech.daoyin_release.utils.DateUtil;
import com.daoyintech.daoyin_release.utils.DateUtils;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import com.daoyintech.daoyin_release.utils.pay.WxPayUtil;
import com.daoyintech.daoyin_release.utils.qiniu.QiniuUploadTool;
import com.qiniu.storage.model.DefaultPutRet;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpQrcodeService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author pei on 2018/08/09
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseUserController implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserIntegralRepository userIntegralRepository;

    @Autowired
    private QiniuProperties qiniuProperties;

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private QiniuUploadTool qiniuUploadTool;

    @Autowired
    private AgentService agentService;

    @Value("${pay.freight_price}")
    private BigDecimal freight_price;

    @Value("${wx.applet}")
    private String applet;

    @Override
    public ResultResponse firstInitUserInfo(String unionId){
        UserIntegral userIntegral = null;
        User user = userRepository.findByUnionId(unionId);
        if (StringUtils.isEmpty(user)){
            //新
            user = initUserInfo(unionId);
            userIntegral = createUserIntegral(user);
        }else{
            userIntegral = userIntegralRepository.findByUserId(user.getId());
        }
        if (StringUtils.isEmpty(user.getAppletOpenId())){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"该用户未关注公众号");
        }
        UserResponse userResponse = UserResponse.userTransformUserResponse(user, userIntegral.getIntegral(), freight_price);
        return ResultResponseUtil.success(userResponse);
    }

    @Transactional
    @Override
    public ResultResponse againInitUserInfo(HashMap<String, Object> userInfoMap){
        UserIntegral userIntegral = null;
        String unionId = (String) userInfoMap.get("unionId");
        User user = userRepository.findByUnionId(unionId);
        if (StringUtils.isEmpty(user)){
            user = initMapToUserInfo(userInfoMap);
            userIntegral = createUserIntegral(user);
        }else{
            //更新用户数据
            user = updateUserInfo(user,userInfoMap);
            userIntegral = userIntegralRepository.findByUserId(user.getId());
        }
        UserResponse userResponse = UserResponse.userTransformUserResponse(user, userIntegral.getIntegral(), freight_price);
        setCurrentUser(unionId);
        return ResultResponseUtil.success(userResponse);
    }

    private User updateUserInfo(User user,HashMap<String, Object> userInfoMap){
        user.setNickName(String.valueOf(userInfoMap.get("nickName")));
        user.setAvatar(String.valueOf(userInfoMap.get("avatarUrl")));
        String unionId = String.valueOf(userInfoMap.get("unionId"));
        user.setUnionId(unionId);
        user.setAppletOpenId(String.valueOf(userInfoMap.get("openId")));
        user.setAppletQrCode(createUserQrCode(unionId));
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User findByUnionId(String unionId) {
        return userRepository.findByUnionId(unionId);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.getOne(userId);
    }

    public User findIsAgent(Long userId) {
        User user = findById(userId);
        AgentBean agentBean = agentService.findByUserIdAndIsSuccess(user, ApplyStatus.准合伙人);

        if (agentBean != null){
            return user;
        } else {
            return null;
        }
    }

    @Override
    public User UpdateOrCreateBankCardInfo(BankCardInfoRequest bankCardInfo, User user) {
        user.setBankCardNum(bankCardInfo.getBankCardNum());
        user.setOpenBank(bankCardInfo.getOpenBank());
        user.setHoldCardName(bankCardInfo.getHoldCardName());
        return userRepository.save(user);
    }

    private UserIntegral createUserIntegral(User user) {
        UserIntegral integral = userIntegralRepository.findByUserId(user.getId());
        if (StringUtils.isEmpty(integral)){
            UserIntegral userIntegral = new UserIntegral();
            userIntegral.setUserId(user.getId());
            return userIntegralRepository.save(userIntegral);
        }
        return null;
    }

    private User initUserInfo(String unionId){
        User user = new User();
        user.setUnionId(unionId);
        user.setAppletQrCode(createUserQrCode(unionId));
        return userRepository.save(user);
    }

    private User initMapToUserInfo(HashMap<String, Object> userInfoMap){
        User user = new User();
        user.setIsSubscribe(false);
        //String nickName = URLEncoder.encode(String.valueOf(userInfoMap.get("nickName")), "utf-8");
        user.setNickName(String.valueOf(userInfoMap.get("nickName")));

        user.setAvatar(String.valueOf(userInfoMap.get("avatarUrl")));
        String unionId = String.valueOf(userInfoMap.get("unionId"));
        user.setUnionId(unionId);
        user.setAppletOpenId(String.valueOf(userInfoMap.get("openId")));
        user.setAppletQrCode(createUserQrCode(unionId));
        return userRepository.save(user);
    }

    public String createUserQrCode(String unionId){
        File qrcode = null;
        try {
            qrcode = wxMaService.getQrcodeService().createQrcode(applet+"?unionId=" + unionId);
        } catch (WxErrorException e) {
            log.error("{}:创建用户二维码错误:{}",DateUtils.getStringDate(),e.getMessage());
        }
        DefaultPutRet putRet = null;
        try {
            putRet = qiniuUploadTool.upload(FileUtils.readFileToByteArray(qrcode), "applet/qrCode/"+unionId+ WxPayUtil.getNonceStr());
            log.info("******{}********",putRet.key);
        } catch (IOException e) {
            log.error("{}:七牛云上传用户二维码错误:{}",DateUtils.getStringDate(),e.getMessage());
        }
        return qiniuProperties.getDomain() + "/" + putRet.key;
    }



    public static String filterEmoji(String source) {
        if (!StringUtils.isEmpty(source) && !containsEmoji(source)) {
            return source;// 如果不包含，直接返回
        }
        StringBuilder buf = null;
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }
                buf.append(codePoint);
            } else {
            }
        }
        if (buf == null) {
            return "";
        } else {
            if (buf.length() == len) {// 这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }
    }

    // 判别是否包含Emoji表情
    private static boolean containsEmoji(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (isEmojiCharacter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }


}

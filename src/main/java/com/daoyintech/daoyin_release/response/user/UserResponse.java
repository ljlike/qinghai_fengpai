package com.daoyintech.daoyin_release.response.user;

import com.daoyintech.daoyin_release.entity.user.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;

/**
 * @author pei on 2018/08/09
 */
@Data
public class UserResponse {

    private Long id;

    private String unionId;

    private String nickName;
    /**
     * 积分
     * */
    private BigDecimal integral;
    /**
     * 头像
     * */
    private String avatar;
    /**
     * 二维码
     * */
    private String qrCodeUrl;

    private Boolean isAgent;
    /**
     * 银行卡number
     * */
    private String bankCardNum;
    /**
     * 银行
     * */
    private Long openBank;
    /**
     * 留卡人姓名
     * */
    private String holdCardName;
    /**
     * 运费
     * */
    private BigDecimal freightPrice;
    /**
     * 提现密码
     * */
    private Boolean isSetPassword;

    public static UserResponse userTransformUserResponse(User user, BigDecimal userIntegral, BigDecimal freightPrice){
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user,userResponse);
        //String nickname = URLDecoder.decode(user.getNickName(), "utf-8");
        userResponse.setNickName(user.getNickName());
        userResponse.setIntegral(userIntegral.setScale(2,BigDecimal.ROUND_HALF_UP));
        userResponse.setFreightPrice(freightPrice);
        if (StringUtils.isEmpty(user.getBankPassword())){
            userResponse.setIsSetPassword(false);
        }else{
            userResponse.setIsSetPassword(true);
        }
        return userResponse;
    }


}

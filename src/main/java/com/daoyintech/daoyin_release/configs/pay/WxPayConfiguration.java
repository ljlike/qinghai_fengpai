package com.daoyintech.daoyin_release.configs.pay;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.EntPayService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.EntPayServiceImpl;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

/**
 * Created by xuzhaolin on 2017/5/19.
 */
@Configuration
public class WxPayConfiguration {

    @Value("${wxPay.appId}")
    private String appId;
    @Value("${wxPay.mchId}")
    private String mchId;
    @Value("${wxPay.mchKey}")
    private String mchKey;
    @Value("${wxPay.keyPath}")
    private String keyPath;
    @Value("${wxPay.tradeType}")
    private String tradeType;

    @Bean
    public WxPayConfig wxPayConfig(){
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(appId);
        payConfig.setMchId(mchId);
        payConfig.setMchKey(mchKey);
        payConfig.setKeyPath(keyPath);
        payConfig.setTradeType(tradeType);
        return payConfig;
    }

    @Bean
    public WxPayService wxPayService(WxPayConfig wxPayConfig){
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(wxPayConfig);
        return wxPayService;
    }

    @Bean
    public EntPayService entPayService(@Qualifier("wxPayService") WxPayService wxPayService) {
        EntPayService entPayService = new EntPayServiceImpl(wxPayService);
        return entPayService;
    }

}

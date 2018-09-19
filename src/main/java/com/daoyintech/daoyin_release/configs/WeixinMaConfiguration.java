package com.daoyintech.daoyin_release.configs;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@Configuration
public class WeixinMaConfiguration {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.appSecret}")
    private String appSecret;

    public WeixinMaConfiguration(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public WxMaService wxMaService(){
        WxMaSpringRedisConfigStorage configStorage = new WxMaSpringRedisConfigStorage(redisTemplate);
        configStorage.setAppId(appId);
        configStorage.setSecret(appSecret);
        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(configStorage);
        return wxMaService;
    }

}

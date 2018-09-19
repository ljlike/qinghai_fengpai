package com.daoyintech.daoyin_release.configs;


import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@Configuration
public class WeixinMpConfiguration {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${official.appId}")
    private String appId;

    @Value("${official.appSecret}")
    private String appSecret;

    public WeixinMpConfiguration(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public WxMpService wxMpService(){
        WxMpSpringRedisConfigStorage configStorage = new WxMpSpringRedisConfigStorage(redisTemplate);
        configStorage.setAppId(appId);
        configStorage.setSecret(appSecret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(configStorage);
        return wxMpService;
    }

}

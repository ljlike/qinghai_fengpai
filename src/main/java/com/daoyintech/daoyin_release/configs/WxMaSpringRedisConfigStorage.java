package com.daoyintech.daoyin_release.configs;

import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class WxMaSpringRedisConfigStorage extends WxMaInMemoryConfig {

    private final static String ACCESS_TOKEN_KEY = "wechat_access_token_";

    private final RedisTemplate<String, String> redisTemplate;

    private String accessTokenKey;


    public WxMaSpringRedisConfigStorage(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setAppId(String appId) {
        super.setAppid(appId);
//        定义redis中存储的名称
        this.accessTokenKey = ACCESS_TOKEN_KEY.concat(appId);
    }

    @Override
    public String getAccessToken() {
        return redisTemplate.opsForValue().get(this.accessTokenKey);
    }

    @Override
    public boolean isAccessTokenExpired() {
        return redisTemplate.getExpire(this.accessTokenKey) < 2;
    }

    @Override
    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
        redisTemplate.opsForValue().set(this.accessTokenKey,accessToken,expiresInSeconds - 200, TimeUnit.SECONDS);
    }

    @Override
    public void expireAccessToken() {
        redisTemplate.expire(this.accessTokenKey, 0, TimeUnit.SECONDS);
    }



}

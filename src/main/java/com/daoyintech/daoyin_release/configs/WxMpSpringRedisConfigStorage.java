package com.daoyintech.daoyin_release.configs;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class WxMpSpringRedisConfigStorage extends WxMpInMemoryConfigStorage {

    private final static String ACCESS_TOKEN_KEY = "wechat_access_token_";

    private final static String JSAPI_TICKET_KEY = "wechat_jsapi_ticket_";

    private final static String CARDAPI_TICKET_KEY = "wechat_cardapi_ticket_";

    private final RedisTemplate<String, String> redisTemplate;


    private String accessTokenKey;

    private String jsapiTicketKey;

    private String cardapiTicketKey;


    public WxMpSpringRedisConfigStorage(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setAppId(String appId) {
        super.setAppId(appId);
//        定义redis中存储的名称
        this.accessTokenKey = ACCESS_TOKEN_KEY.concat(appId);
        this.jsapiTicketKey = JSAPI_TICKET_KEY.concat(appId);
        this.cardapiTicketKey = CARDAPI_TICKET_KEY.concat(appId);
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

    @Override
    public String getJsapiTicket() {
        return redisTemplate.opsForValue().get(this.jsapiTicketKey);
    }

    @Override
    public boolean isJsapiTicketExpired() {
        return redisTemplate.getExpire(this.jsapiTicketKey) < 2;
    }

    @Override
    public synchronized void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
        redisTemplate.opsForValue().set(this.jsapiTicketKey, jsapiTicket, expiresInSeconds - 200, TimeUnit.SECONDS);
    }

    @Override
    public void expireJsapiTicket() {
        redisTemplate.expire(this.jsapiTicketKey, 0, TimeUnit.SECONDS);
    }


    @Override
    public String getCardApiTicket() {
        return redisTemplate.opsForValue().get(this.cardapiTicketKey);
    }

    @Override
    public boolean isCardApiTicketExpired() {
        return redisTemplate.getExpire(this.cardapiTicketKey) < 2;
    }

    @Override
    public synchronized void updateCardApiTicket(String cardApiTicket, int expiresInSeconds) {
        redisTemplate.opsForValue().set(this.cardapiTicketKey, cardApiTicket, expiresInSeconds - 200, TimeUnit.SECONDS);
    }

    @Override
    public void expireCardApiTicket() {
        redisTemplate.expire(this.cardapiTicketKey, 0, TimeUnit.SECONDS);
    }




}

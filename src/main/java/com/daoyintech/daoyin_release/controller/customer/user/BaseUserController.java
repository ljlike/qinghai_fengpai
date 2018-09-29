package com.daoyintech.daoyin_release.controller.customer.user;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author pei on 2018/08/11
 */
@Slf4j
public class BaseUserController {

    @Autowired
    private WxMpService wxMpService;

    public static final String SESSION_ID = "daoyin_release_session_id";

    public void setCurrentUser(String unionId){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String attribute = (String) request.getSession().getAttribute(SESSION_ID);
        if (StringUtils.isEmpty(attribute)){
            request.getSession().setAttribute(SESSION_ID,unionId);
        }
    }

    public String getCurrentUnionId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String unionId = (String) request.getSession().getAttribute(SESSION_ID);
        //String unionId = "o9C7s5xw4uV1XrGvWYncH_10oOF8";
        return unionId;
    }

    public String getUserIp(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip;
        String header = "x-forwarded-for";
        if (request.getHeader(header) == null) {
            ip = request.getRemoteAddr();
        }else{
            ip = request.getHeader(header);
        }
        return ip;
    }


    public WxJsapiSignature getWxJsApi() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String requestUrl;
        StringBuffer url = request.getRequestURL();
        String queryString = request.getQueryString();
        if(queryString == null){
            requestUrl = url.toString();
        }else{
            requestUrl = url.append("?").append(queryString).toString();
        }
        WxJsapiSignature jsapiSignature = null;
        try {
            jsapiSignature = wxMpService.createJsapiSignature(requestUrl);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return jsapiSignature;
    }



}












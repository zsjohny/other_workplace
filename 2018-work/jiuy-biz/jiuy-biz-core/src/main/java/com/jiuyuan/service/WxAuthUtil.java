package com.jiuyuan.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiuyuan.util.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/13 20:35
 * @Copyright 玖远网络
 */
public class WxAuthUtil{

    private static final Map<String, Object> EMPTY_MAP = new HashMap<> ();

    private static final Logger LOGGER = LoggerFactory.getLogger(WxAuthUtil.class);

    private static final String COMPONENT_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
    private static final String SESSION_KEY_URL = "https://api.weixin.qq.com/sns/component/jscode2session";
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";


    /**
     * 获取第三方accessToken
     *
     * @param cpnAppId component_appid
     * @param cpnAppSecret component_appsecret
     * @param cpnVerifyTicket component_verify_ticket
     * @return java.lang.String
     * <p>
     *     accessToken = {component_access_token=13_gaKx6NdNJB3BlO4AaLE5wxJuql1B_GEakUs04xvNTzrbS3G31tG2Q2_WCg9GwubPj8_y01QsbcHs8awTNNczQjZmmOpBADyh4JegXm1Of6LPwDLURc2PbGTf5qJqiZuUn-VxiNY7WgBglIAbHMChAJADIY, expires_in=7200}
     * </p>
     * @author Charlie
     * @date 2018/9/13 21:31
     */
    public static Map<String, Object> componentAccessToken(String cpnAppId, String cpnAppSecret, String cpnVerifyTicket) {
        Map<String, Object> param = new HashMap<> (4);
        param.put ("component_appid", cpnAppId);
        param.put ("component_appsecret", cpnAppSecret);
        param.put ("component_verify_ticket", cpnVerifyTicket);
        String paramJson = new Gson().toJson (param);
        LOGGER.info ("获取第三方票据 url --> url[{}].paramJson[{}]", COMPONENT_ACCESS_TOKEN_URL, paramJson);
        String result = HttpClientUtils.post (COMPONENT_ACCESS_TOKEN_URL, paramJson);
        LOGGER.info ("获取第三方票据 componentAccessToken --> retMap[{}]", result);
        if (StringUtils.isBlank (result)) {
            return EMPTY_MAP;
        }
        else {
            return new Gson ().fromJson (result, new TypeToken<HashMap<String, Object>> (){}.getType());
        }
    }




    /**
     * 获取sessionKey 可用
     *
     * @param componentAccessToken componentAccessToken
     * @param appId appId
     * @param jsCode jsCode
     * @param cpnAppId cpnAppId
     * @return <P> sessionKey = {"session_key":"vOHeWWhpZLgBb6bmnrwUHw==","openid":"o01of0W-wibaVzzYKHJoTlNH5a7Y"} </P>
     * @author Charlie
     * @date 2018/9/13 22:06
     */
    public static String sessionKey(String componentAccessToken, String appId, String jsCode, String cpnAppId) {
        Map<String, Object> params = new HashMap(6);
        //小程序的AppID
        params.put("appid", appId);
        params.put("js_code", jsCode);
        params.put("grant_type", "authorization_code");
        params.put("component_access_token", componentAccessToken);
        params.put ("component_appid", cpnAppId);
        return HttpClientUtils.get (SESSION_KEY_URL, params);
    }



    /**
     * 获取sessionKey todo... 没测
     *
     * @param appId appId
     * @param jsCode jsCode
     * @param cpnAppId cpnAppId
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/13 22:06
     */
    @Deprecated
    public static String sessionKey(String appId, String jsCode, String cpnAppId, String cpnAppSecret, String cpnVerifyTicket) {
        Map<String, Object> params = new HashMap(6);
        params.put("appid", appId);//小程序的AppID
        params.put("js_code", jsCode);
        params.put("grant_type", "authorization_code");
        params.put("component_appid", cpnAppId);
        Object accessToken = componentAccessToken (cpnAppId, cpnAppSecret, cpnVerifyTicket).get ("component_access_token");
        if (null == accessToken) {
            LOGGER.info ("获取sessionKey, componentAccessToken为空");
            return null;
        }
        params.put("component_access_token", accessToken);
        return HttpClientUtils.get (SESSION_KEY_URL, params);
    }


    /**
     * accessToken
     *
     * @author Charlie(唐静)
     * @date
     */
    public static String accessToken(String appId, String secret) {
        String url = ACCESS_TOKEN_URL + "&appid="+appId+"&secret="+secret;
        LOGGER.info ("获取访问token url[{}].", url);
        String result = HttpClientUtils.get (url);
        LOGGER.info ("获取访问token accessToken[{}].", result);
        return result;
    }


    public static void main(String[] args) {
        String cpnAppId = "wx3ef25e066e478873";
        String cpnAppSecret = "f646bd674afb7357fd06536c2fea626b";
        String cpnVerifyTicket = "ticket@@@JkF08UE-47Dr5GBrVoOGteqWd_3IvpB_85gNnydmszPc31xgyltdLa2ATlwHG9OTUGcm8_YM-MRtmzMxt-urCA";
//        Map<String, Object> accessToken = componentAccessToken (cpnAppId, cpnAppSecret, cpnVerifyTicket);
//        System.out.println ("accessToken = " + accessToken);
//
        String appId = "wxf99f985dc7f79695";
        String jsCode = "071cv5hz0XcROf1kc3iz0yt7hz0cv5ho";
        String accToken = "13_Zz6z66r35NebJEOYbvDFsxa0iM_Sf0GEHMTtAj_sztEvxlOFMU-TRkWtT9lS7MTAKmEpjEyPggISaKXB3SCbYgwV217Z2PeacHykdVKzMMAY64aNiGkEl6D3W9-lG4r9ikKInIRJLE88a-AbYSIbAJASMC";

//        String sessionKey2 = sessionKey (accToken, appId, jsCode, cpnAppId);
//        System.out.println ("sessionKey2 = " + sessionKey2);


    }


}

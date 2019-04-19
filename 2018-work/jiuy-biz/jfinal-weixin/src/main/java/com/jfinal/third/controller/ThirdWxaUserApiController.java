package com.jfinal.third.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Duang;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.third.api.ThirdWxaUserApi;
import com.jfinal.weixin.jiuy.cache.MemcacheApi;
import com.jfinal.weixin.jiuy.service.MemberService;
import com.jfinal.weixin.sdk.api.ApiResult;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

//import com.jfinal.weixin.sdk.api.ApiConfigKit;

/**
 * 微信小程序用户api接口
 *
 * @author L.cm
 */
public class ThirdWxaUserApiController extends ThirdWxaController {


    static Log logger = Log.getLog(ThirdWxaUserApiController.class);
    /** 微信用户接口api */
    protected ThirdWxaUserApi thirdWxaUserApi = Duang.duang(ThirdWxaUserApi.class);

    protected MemcacheApi memcacheApi = Duang.duang(MemcacheApi.class);


    /**
     * 登陆接口
     * http://dev.yujiejie.com/wxa/user/login?code=071s3uyG1YKmY50eFBzG1SstyG1s3uyQ
     * https://weixintest.yujiejie.com/wxa/user/login?code=071s3uyG1YKmY50eFBzG1SstyG1s3uyQ
     * <p>
     * return
     * {"errcode":40163,"errmsg":"code been used, hints: [ req_id: P2JB0128th31 ]"}
     */
    public void login() {
        String jsCode = getPara("code");
        String appId = getPara("appId");
        String storeId = getPara("storeId");
        String from = getPara("from");
        String loginType = getPara("loginType");
        //0专享小程序,1店中店
        loginType = loginType == null || "".equals(loginType) ? "0":loginType;

        if (StrKit.isBlank(jsCode)) {
            Kv data = Kv.by("errcode", 500)
                .set("errmsg", "code is blank");
            logger.info("/wxa/user/login返回信息：" + data.toJson());
            renderJson(data);
            return;
        }

        if (StrKit.isBlank(appId)) {
            Kv data = Kv.by("errAppId", 500)
                .set("errmsg", "appId is blank");
            logger.info("/wxa/user/login返回信息：" + data.toJson());
            renderJson(data);
            return;
        }
        if (StrKit.isBlank(storeId)) {
            Kv data = Kv.by("errStoreId", 500)
                .set("errmsg", "storeId is blank");
            logger.info("/wxa/user/login返回信息：" + data.toJson());
            renderJson(data);
            return;
        }
        ApiResult apiResult = thirdWxaUserApi.getSessionKey(appId, jsCode);
        String openid = apiResult.get("openid");
        logger.info("获取openId===>" + openid);
        if (!apiResult.isSucceed()) {
            renderJson(apiResult.getJson());
            return;
        }

        String sessionId = StrKit.getRandomUUID();
        ////登陆
        Map<String, String> memberInfo = MemberService.login(openid, appId, storeId, from, loginType);

        memcacheApi.set("wxa:session:" + sessionId + appId, apiResult.getJson());

        Map<String, Object> retunrMap = new HashMap<String, Object>();
        retunrMap.put("sessionId", sessionId);
        retunrMap.put("memberInfo", memberInfo);

        logger.info("/wxa/user/login返回信息：" + JSONObject.toJSONString(retunrMap));
        renderJson(JSONObject.toJSONString(retunrMap));
    }

    /**
     * 服务端解密用户信息接口code=071e5EJF0RSWAi2ASBLF0ydGJF0e5EJT  code=081O1Nxm1fG0Jl0bSOzm1d8Oxm1O1Nxd
     * 获取unionId
     */
    public void infos() {
        logger.info("进入程序了");
        String signature = getPara("signature");
        String rawData = getPara("rawData");

        String encryptedData = getPara("encryptedData");
        String iv = getPara("iv");
        String from = getPara("from");

        // 参数空校验 不做演示
        // 利用 appId 与 accessToken 建立关联，支持多账户
//		IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
        String sessionId = getHeader("wxa-sessionid");
        String appId = getHeader("wxa-appid");
        String storeId = getHeader("wxa-storeid");
        logger.info("微信登陆=sessionId={}，" + sessionId + "，appId={}" + appId + "，storeId={}" + storeId);
        if (StrKit.isBlank(storeId)) {
            Kv data = Kv.by("errcode", 500)
                .set("errmsg", "wxa-storeid Header is blank");
            logger.info("/wxa/user/info返回信息：" + data.toJson());
            renderJson(data);
            return;
        }
        if (StrKit.isBlank(appId)) {
            Kv data = Kv.by("errcode", 500)
                .set("errmsg", "wxa-appid Header is blank");
            logger.info("/wxa/user/info返回信息：" + data.toJson());
            renderJson(data);
            return;
        }
        if (StrKit.isBlank(sessionId)) {
            Kv data = Kv.by("errcode", 500)
                .set("errmsg", "wxa-sessionid Header is blank");
            renderJson(data);
            return;
        }
        String sessionJson = memcacheApi.get("wxa:session:" + sessionId + appId);
        logger.info("sessionJson from 缓存 " + sessionJson);
        if (StrKit.isBlank(sessionJson)) {
            Kv data = Kv.by("errcode", 500)
                .set("errmsg", "wxa_session sessionJson is blank");
            logger.info("/wxa/user/info返回信息：" + data.toJson());
            renderJson(data);
            return;
        }
        ApiResult sessionResult = ApiResult.create(sessionJson);
        // 获取sessionKey
        String sessionKey = sessionResult.get("session_key");
        if (StrKit.isBlank(sessionKey)) {
            Kv data = Kv.by("errcode", 500)
                .set("errmsg", "sessionKey is blank");
            logger.info("/wxa/user/info返回信息：" + data.toJson());
            renderJson(data);
            return;
        }
        // 用户信息校验
        boolean check = thirdWxaUserApi.checkUserInfo(sessionKey, rawData, signature);
        if (!check) {
            Kv data = Kv.by("errcode", 500)
                .set("errmsg", "UserInfo check fail");
            logger.info("/wxa/user/info返回信息：" + data.toJson());
            renderJson(data);
            return;
        }
        // 服务端解密用户信息
//		{
//		    "openId": "o2Kn-0GB5SOFo4bLtN5v9lO8nStQ",
//		    "nickName": "诸葛正雄",
//		    "gender": 1,
//		    "language": "zh_CN",
//		    "city": "Hangzhou",
//		    "province": "Zhejiang",
//		    "country": "CN",
//		    "avatarUrl": "https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTK7PqbGBfGAN5HsbomvUroyggljtbc0ibXvsquYSyxsINteCZJPTbC5veGgVZgmibd8NmVOfFcibnIcA/0",
//		    "watermark": {
//		        "timestamp": 1498735065,
//		        "appid": "wx96be78720a632a45"
//		    }
//		}
        ApiResult apiResult = thirdWxaUserApi.getUserInfo(sessionKey, encryptedData, iv);
        logger.info("========用户信息，apiResult" + apiResult.toString());
        if (!apiResult.isSucceed()) {
            renderJson(apiResult.getJson());
            return;
        }
        // 如果开发者拥有多个移动应用、网站应用、和公众帐号（包括小程序），可通过unionid来区分用户的唯一性
        // 同一用户，对同一个微信开放平台下的不同应用，unionid是相同的。
        String unionId = apiResult.get("unionId"); //TODO 为空

        String openId = apiResult.get("openId");
        String nickName = apiResult.get("nickName");
        String avatarUrl = apiResult.get("avatarUrl");
        //性别
        String gender = apiResult.get ("gender").toString ();

        LinkedHashMap<String, String> watermark = apiResult.get("watermark");//{timestamp=1498735881, appid=wx96be78720a632a45}
//		JSONObject watermarkJsonObject = JSONUtil.parseObj(watermark);
        String authAppId = watermark.get("appid");//watermarkJsonObject.getStr("appid");
        //授权
        String jsonStr = MemberService.authoriz(openId, nickName, avatarUrl, authAppId, storeId, from, gender, unionId);
        logger.info("/wxa/user/info返回信息：" + jsonStr);
        //返回用户信息
        renderJson(jsonStr);
    }

}

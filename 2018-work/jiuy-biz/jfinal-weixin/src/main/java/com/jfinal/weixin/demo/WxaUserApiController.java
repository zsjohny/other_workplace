package com.jfinal.weixin.demo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Duang;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.third.api.ThirdWxaUserApi;
import com.jfinal.weixin.jiuy.service.MemberService;
import com.jfinal.weixin.jiuy.service.MsgService;
import com.jfinal.weixin.jiuy.service.ParamSignUtils;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.cache.IAccessTokenCache;
import com.jfinal.wxaapp.api.WxaUserApi;
import com.jfinal.wxaapp.jfinal.WxaController;
import net.dongliu.requests.Requests;
import net.dongliu.requests.Response;

/**
 * 微信小程序用户api接口
 * @author L.cm
 */
public class WxaUserApiController extends WxaController {

	static Log logger = Log.getLog(WxaUserApiController.class);
	// 微信用户接口api
	protected WxaUserApi wxaUserApi = Duang.duang(WxaUserApi.class);

    protected ThirdWxaUserApi thirdWxaUserApi = Duang.duang(ThirdWxaUserApi.class);

    static final String version = "1.0.0";

	/**
	 * 登陆接口
	 * http://dev.yujiejie.com/wxa/user/login?code=071s3uyG1YKmY50eFBzG1SstyG1s3uyQ
	 * https://weixintest.yujiejie.com/wxa/user/login?code=071s3uyG1YKmY50eFBzG1SstyG1s3uyQ
	 *
	 * return
	 * {"errcode":40163,"errmsg":"code been used, hints: [ req_id: P2JB0128th31 ]"}
	 */
	public void login() {
		String jsCode = getPara("code");
		String appId = getPara("appId");
		String storeId = getPara("storeId");
		String from = getPara("form");
        String loginType = getPara("loginType");
        //0专享小程序,1店中店
        loginType = loginType == null || "".equals(loginType) ? "0":loginType;

		if (StrKit.isBlank(jsCode)) {
			Kv data = Kv.by("errcode", 500)
					.set("errmsg", "code is blank");
			logger.info("/wxa/user/login返回信息："+data.toJson());
			renderJson(data);
			return;
		}

		if (StrKit.isBlank(appId)) {
			Kv data = Kv.by("errAppId", 500)
					.set("errmsg", "appId is blank");
			logger.info("/wxa/user/login返回信息："+data.toJson());
			renderJson(data);
			return;
		}
		if (StrKit.isBlank(storeId)) {
			Kv data = Kv.by("errStoreId", 500)
					.set("errmsg", "storeId is blank");
			logger.info("/wxa/user/login返回信息："+data.toJson());
			renderJson(data);
			return;
		}
		// 获取SessionKey  {"session_key":"uF48RJnzNwmAWPrNirMH2w==","expires_in":7200,"openid":"o2Kn-0GB5SOFo4bLtN5v9lO8nStQ"}
		ApiResult apiResult = wxaUserApi.getSessionKey(jsCode);
		String sessionKey = apiResult.get("session_key");
		String openid = apiResult.get("openid");
		if (!apiResult.isSucceed()) {
			renderJson(apiResult.getJson());
			return;
		}


		String sessionId =   StrKit.getRandomUUID();
        ////登陆
		Map<String,String > memberInfo = MemberService.login(openid,appId,storeId,from, loginType);

		// 利用 appId 与 accessToken 建立关联，支持多账户
		IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
		accessTokenCache.set("wxa:session:"  + sessionId  , apiResult.getJson());
//		Map<String,String> retunrMap = new HashMap<String,String>();
//		retunrMap.put("sessionId", sessionId);
//		retunrMap.put("memberInfo", memberInfo);

		Map<String,Object> retunrMap = new HashMap<String,Object>();
		retunrMap.put("sessionId", sessionId);
		retunrMap.put("memberInfo", memberInfo);

		logger.info("/wxa/user/login返回信息："+JSONObject.toJSONString(retunrMap));
		renderJson(JSONObject.toJSONString(retunrMap));

	}

	/**
	 * 服务端解密用户信息接口code=071e5EJF0RSWAi2ASBLF0ydGJF0e5EJT  code=081O1Nxm1fG0Jl0bSOzm1d8Oxm1O1Nxd
	 * 获取unionId
	 */
	public void info() {
		String signature = getPara("signature");
		String rawData = getPara("rawData");

		String encryptedData = getPara("encryptedData");
		String iv = getPara("iv");

		// 参数空校验 不做演示
		// 利用 appId 与 accessToken 建立关联，支持多账户
		IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
		String sessionId = getHeader("wxa-sessionid");
		String appId = getHeader("wxa-appid");

		if (StrKit.isBlank(sessionId)) {
			Kv data = Kv.by("errcode", 500)
					.set("errmsg", "wxa_session Header is blank");
			renderJson(data);
			return;
		}
		String sessionJson = accessTokenCache.get("wxa:session:"  + sessionId );
		if (StrKit.isBlank(sessionJson)) {
			Kv data = Kv.by("errcode", 500)
					.set("errmsg", "wxa_session sessionJson is blank");
			renderJson(data);
			return;
		}
		ApiResult sessionResult = ApiResult.create(sessionJson);
		// 获取sessionKey
		String sessionKey = sessionResult.get("session_key");
		if (StrKit.isBlank(sessionKey)) {
			Kv data = Kv.by("errcode", 500)
					.set("errmsg", "sessionKey is blank");
			renderJson(data);
			return;
		}
		// 用户信息校验
		boolean check = wxaUserApi.checkUserInfo(sessionKey, rawData, signature);
		if (!check) {
			Kv data = Kv.by("errcode", 500)
					.set("errmsg", "UserInfo check fail");
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
		ApiResult apiResult = wxaUserApi.getUserInfo(sessionKey, encryptedData, iv);
		logger.info("===================================================================用户信息，apiResult"+apiResult.toString());
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
		LinkedHashMap<String,String> watermark = apiResult.get("watermark");//{timestamp=1498735881, appid=wx96be78720a632a45}
//		JSONObject watermarkJsonObject = JSONUtil.parseObj(watermark);
		String authAppId = watermark.get("appid");//watermarkJsonObject.getStr("appid");
		//授权
		String jsonStr = "";// MemberService.authoriz(openId,nickName,avatarUrl,authAppId,storeId);

		//返回用户信息
		renderJson(jsonStr);
	}

}

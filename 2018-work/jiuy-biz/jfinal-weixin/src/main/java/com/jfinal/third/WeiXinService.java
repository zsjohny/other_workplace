/**
 * 
 */
package com.jfinal.third;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.log.Log;
import com.jfinal.third.util.MemcachedKey;
import com.jfinal.third.util.WXBizMsgCrypt;
import com.jfinal.third.util.weixinpay.WapPayHttpUtil;
import com.jfinal.third.util.weixinpay.WeixinPayCore;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.cache.IAccessTokenCache;

public class WeiXinService {
	static Log logger = Log.getLog(WeiXinService.class);

//    @Autowired
//    private MemcachedService memcachedService;
    
    
    public static String component_appid = "wxddd55d6028f404ab";// ShopConfig.component_appid;//"wxb11529c136998cb6";
    public static String component_appsecret = "725058b52fa0f53f28d9a5a6bde1c0ea";//ShopConfig.component_appsecret;
	public static String component_encodingAesKey = "9HuP5H2P2Fk5wiZyYoWAAMjwJqq3Vt9HuP5H2P2Fk5w";//"abcdefghijklmnopqrstuvwxyz0123456789ABCDEFG";
	public static String component_token = "jiuyuan2017";//公众号消息校验Token
	
	
//	public static String authorizer1_appid = "wx95c37c75c641bc5e";//ShopConfig.authorizer_appid1;
//	public static String authorizer1_token = "yujiejie2017jiuyuan";
//	public static String authorizer1_encodingAesKey = "jSYLj1gmn9VQKVPH0EynevH3OKvdiu8nA1pmejXtIAN";
	
//	public static String authorizer2_appid = "wx570bc396a51b8ff8";//微信自动化测试appid
    
    /**
     * 接收推送component_verify_ticket
     * @return
     */
	public void component_verify_ticket(String fromXML,String signature,String timestamp,String nonce,String encrypt_type,String msg_signature) throws Exception{
		logger.info("微信推送component_verify_ticket成功, fromXML:{},signature:{},timestamp:{},nonce:{}，encrypt_type:{}");
    	
    	WXBizMsgCrypt pc = new WXBizMsgCrypt(component_token, component_encodingAesKey, component_appid);
    	String result = pc.decryptMsg(msg_signature, timestamp, nonce, fromXML);
		System.out.println("微信推送component_verify_ticket成功，解密后明文: " + result);
		
//		 <xml><AppId><![CDATA[wxddd55d6028f404ab]]></AppId>
//		 <CreateTime>1496654711</CreateTime>
//		 <InfoType><![CDATA[component_verify_ticket]]></InfoType>
//		 <ComponentVerifyTicket><![CDATA[ticket@@@6qAXiAW7TwQJadv89I93Mlb-E8Y_HMysAFgnaK8wxuVxUWUihh4rPSDqPnnd6dbTsShn_K32nzk_kFHKVm_tIw]]></ComponentVerifyTicket>
//		 </xml>
//		 
		Map<String, String> params = WeixinPayCore.decodeXml(result);
	    String AppId = params.get("AppId");
	    String CreateTime = params.get("CreateTime");
	    String InfoType = params.get("InfoType");
	    String ComponentVerifyTicket = params.get("ComponentVerifyTicket");
    	
    	
    	logger.info("微信推送component_verify_ticket成功, AppId:{}, CreateTime:{}, InfoType:{}, ComponentVerifyTicket:{}");
        if(StringUtils.isEmpty(AppId) || StringUtils.isEmpty(ComponentVerifyTicket)){
        	logger.info("微信推送component_verify_ticket成功, 但是AppId或ComponentVerifyTicket为空，AppId:{}, CreateTime:{}, InfoType:{}, ComponentVerifyTicket:{},");
        }else{
        	
        	String ticket_groupKey = MemcachedKey.GROUP_KEY_component_verify_ticket;
        	IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
    		accessTokenCache.set(ticket_groupKey   +  AppId , ComponentVerifyTicket);
//          	String memcachedTicket = memcachedService.getStr(ticket_groupKey, ticket_groupKey+component_appid);
          	logger.info("微信推送component_verify_ticket成功, AppId:{}, CreateTime:{}, InfoType:{}, ComponentVerifyTicket:"+ComponentVerifyTicket+",memcachedTicket:{}");
        }
	}
	
	

    /**
     * 获取第三方平台component_access_token
     * @return
     */
	public String get_component_access_token() {
		String token_groupKey = MemcachedKey.GROUP_KEY_compoment_access_token;
		//TODO 测试时先不走缓存
//		String memcachedToken = memcachedService.getStr(token_groupKey, token_groupKey+component_appid);
//		if(StringUtils.isNotEmpty(memcachedToken)){
//			logger.info("获取第三方平台component_access_token成功,memcachedToken:{}", memcachedToken);
//			return memcachedToken;
//		}
		
		String url = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
    	
    	String ticket_groupKey = MemcachedKey.GROUP_KEY_component_verify_ticket;

    	
//    	String component_verify_ticket = "ticket@@@-13NLl6QzLHN0OgTJrBBZbb5u81Tj-7fjBalT3rrBhAn5XTfxT6ohu3aB8uOqUvOPCTVRCEHnSHoClFthe5L6A";//
//    	String component_verify_ticket =  memcachedService.getStr(ticket_groupKey, ticket_groupKey+component_appid);
    	IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
    	String component_verify_ticket = accessTokenCache.get(ticket_groupKey  + component_appid );
		
    			
    	Map<String, String> paramMap = new HashMap<String, String>();
    	paramMap.put("component_appid", component_appid);
    	paramMap.put("component_appsecret", component_appsecret);
    	paramMap.put("component_verify_ticket", component_verify_ticket);
    	String param = JSONObject.toJSONString(paramMap);
    	//String param = "{\"component_appid\":"+component_appid+",\"component_appsecret\": "+component_appsecret+",\"component_verify_ticket\":"+component_verify_ticket+"}";
    	logger.info("获取第三方平台component_access_token成功, url:{}, param:{}");
    	
    	Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
    	//{errcode=41002, errmsg=appid missing hint: [Ln36_0041e575]}
    	//{errcode=61004, errmsg=access clientip is not registered hint: [ZJ6NTA0211e578] requestIP: 58.101.200.168}
    	
    	String component_access_token = (String) retMap.get("component_access_token");
    	logger.info("获取第三方平台component_access_token成功,ret:{} ");
    	if(StringUtils.isNotEmpty(component_access_token)){
    		Integer expires_in = (Integer) retMap.get("expires_in");//7200
        	logger.info("获取第三方平台component_access_token成功,retMap:{} component_access_token:{}, expires_in:{}");
        	accessTokenCache.set(token_groupKey   +  component_appid , component_access_token);
//        	memcachedService.set(token_groupKey,  token_groupKey+component_appid, 7000, component_access_token);
//    		String newMemcachedToken = memcachedService.getStr(token_groupKey, token_groupKey+component_appid);
    		logger.info("获取第三方平台component_access_token成功,ret:{} component_access_token:{}, expires_in:{},newMemcachedToken:{}");
    	}
    	return component_access_token;
	}

	
	  /**
     * 获取预授权码pre_auth_code
     * @return
     */
	public String get_pre_auth_code() {
		String pre_auth_code_groupKey = MemcachedKey.GROUP_KEY_pre_auth_code;
//		String memcachedAuthCode = memcachedService.getStr(pre_auth_code_groupKey, pre_auth_code_groupKey + component_appid);
		IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
    	String memcachedAuthCode = accessTokenCache.get(pre_auth_code_groupKey  + component_appid );
		
		if(StringUtils.isNotEmpty(memcachedAuthCode)){
			logger.info("获取第三方平台pre_auth_code成功,memcachedAuthCode:{}");
			return memcachedAuthCode;
		}
		
		//获取第三方平台component_access_token
    	String component_access_token = get_component_access_token();
    	String url = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token="+component_access_token;
     	Map<String, String> paramMap = new HashMap<String, String>();
    	paramMap.put("component_appid", component_appid);
    	String param = JSONObject.toJSONString(paramMap);
//    	String param = "{\"component_appid\":"+component_appid+"}";
    	logger.info("获取预授权码pre_auth_code成功, url:{}, param:{}");
    	
    	Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
    	logger.info("获取预授权码pre_auth_code成功,ret:{}");
    	
    	String pre_auth_code = (String) retMap.get("pre_auth_code");
    	if(StringUtils.isNotEmpty(pre_auth_code)){
	    	Integer expires_in = (Integer) retMap.get("expires_in");
	    	logger.info("获取预授权码pre_auth_code成功,ret:{}, pre_auth_code:{}, expires_in:{}");
	    	accessTokenCache.set(pre_auth_code_groupKey   +  component_appid , pre_auth_code);
//	    	memcachedService.set(pre_auth_code_groupKey,  pre_auth_code_groupKey + component_appid, 600, pre_auth_code);
//			String newMemcachedAuthCode = memcachedService.getStr(pre_auth_code_groupKey, pre_auth_code_groupKey + component_appid);
			logger.info("获取预授权码pre_auth_code成功,ret:{}, pre_auth_code:{}, expires_in:{},newMemcachedAuthCode:");
	    }
    	return pre_auth_code;
	}
	
	
	/**
	 * 获取公众号授权路径
	 * @param webBaseUrl 项目根路径
	 * @return
	 */
	public String getGongZhongHaoAuthUrl(String webBaseUrl) {
    	String url = "https://mp.weixin.qq.com/cgi-bin/componentloginpage";
   	 	StringBuilder urlBuilder = new StringBuilder(url);
   	 	urlBuilder.append("?component_appid=").append(component_appid);
   	 	urlBuilder.append("&pre_auth_code=").append(get_pre_auth_code());
   	 
   	 	String redirect_uri = webBaseUrl + "/weixin/gongZhongHaoCallback.do";
   	 	urlBuilder.append("&redirect_uri=").append(redirect_uri);
//   	 redirect_uri = URLEncoder.encode(URLEncoder.encode(redirect_uri,"UTF-8"));
   	 	return urlBuilder.toString();
	}
	
	
	/**
	 * 公众号授权回调
	 * @param auth_code 授权码
	 * @param expires_in 有效时间
	 * @return 
	 */
	public void gongZhongHaoCallback(String auth_code, String expires_in) {
		logger.info("公众号授权回调gongZhongHaoCallback成功,auth_code:{}, expires_in:{}");
		if(StringUtils.isNotEmpty(auth_code)){
    		String auth_code_groupKey = MemcachedKey.GROUP_KEY_auth_code;
//        	memcachedService.set(auth_code_groupKey,  auth_code_groupKey + authorizer2_appid, 3000, auth_code);
    		IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
        	accessTokenCache.set(auth_code_groupKey   +  component_appid , auth_code);
        	
//        	String newMemcachedAuthCode = memcachedService.getStr(auth_code_groupKey, auth_code_groupKey + authorizer2_appid);
        	String newMemcachedAuthCode = accessTokenCache.get(auth_code_groupKey  + component_appid );
    		
			logger.info("获取授权码auth_code成功,auth_code:{}, expires_in:{},newMemcachedAuthCode:"+newMemcachedAuthCode+"");
			
			//根据授权码获取token
			get_authorization_info(auth_code);
    	}
	}
	
	
	/**
	 *  4、使用授权码换取公众号或小程序的接口调用凭据和授权信息
	 *  * https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=xxxx
     * 
     * POST数据示例:
		{
		"component_appid":"appid_value" ,//第三方平台appid
		"authorization_code": "auth_code_value"//授权code,会在授权成功时返回给第三方平台，详见第三方平台授权流程说明
		}
     * 返回结果示例
		{ 
			"authorization_info": {//授权信息
				"authorizer_appid": "wxf8b4f85f3a794e77", //授权方appid
				"authorizer_access_token": "QXjUqNqfYVH0yBE1iI_7vuN_9gQbpjfK7hYwJ3P7xOa88a89-Aga5x1NMYJyB8G2yKt1KCl0nPC3W9GJzw0Zzq_dBxc8pxIGUNi_bFes0qM", //授权方接口调用凭据（在授权的公众号或小程序具备API权限时，才有此返回值），也简称为令牌
				"expires_in": 7200,//有效期（在授权的公众号或小程序具备API权限时，才有此返回值） 
				"authorizer_refresh_token": "dTo-YCXPL4llX-u1W1pPpnp8Hgm4wpJtlR6iV0doKdY", //接口调用凭据刷新令牌（在授权的公众号具备API权限时，才有此返回值），刷新令牌主要用于第三方平台获取和刷新已授权用户的access_token，只会在授权时刻提供，请妥善保存。 一旦丢失，只能让用户重新授权，才能再次拿到新的刷新令牌
				"func_info": [//公众号授权给开发者的权限集列表	请注意：该字段的返回不会考虑公众号是否具备该权限集的权限（因为可能部分具备），请根据公众号的帐号类型和认证情况，来判断公众号的接口权限。
								//ID为1到15时分别代表：1消息管理权限、2用户管理权限、3帐号服务权限、4网页服务权限、5微信小店权限、6微信多客服权限、7群发与通知权限、8微信卡券权限、9微信扫一扫权限、10微信连WIFI权限、11素材管理权限、12微信摇周边权限、13微信门店权限、14微信支付权限、15自定义菜单权限
					{
						"funcscope_category": {
							"id": 1
						}
					}, {
						"funcscope_category": {
						"id": 2
						}
					}, {
						"funcscope_category": {
						"id": 3
						}
				}
				]
			}
		}
		
		{
    "successful":true,
    "error":null,
    "code":0,
    "data":{
        "authorization_info":{
            "authorization_info":{
                "authorizer_appid":"wx95c37c75c641bc5e",
                "authorizer_access_token":"ggzG7uxURAcwrxdEtL6ckpkIpb7fEJAqYe46NcUL5AEeVw9Pgp6svCSq8INKz7DlXEraYvPrwa1gORMurBH9Mv0RhZmk3yl0PZLTjZ3Ugfk1TsqL2BIcUsmqfbRH1FC2MHGcAFDYTS",
                "authorizer_refresh_token":"refreshtoken@@@R6JlONWKJlJF1arx28ju-QDoXfvI9PhcI2UVSUJfELc",
                "expires_in":7200,
                "func_info":[]
            }
        }
    },
    "html":null
}

©2014 Json.cn All right reserved. 
	 * @return
	 */
	public String get_authorization_info(String authorization_code) {
		//获取第三方平台component_access_token
    	String component_access_token = get_component_access_token();
		String url = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token="+component_access_token;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("component_appid", component_appid);
		paramMap.put("authorization_code", authorization_code);
    	String param = JSONObject.toJSONString(paramMap).toString();
//		String param = "{\"component_appid\":"+component_appid+",\"authorization_code\":"+authorization_code+"}";
    	logger.info("获取授权信息authorization_info成功, url:{}, param:{}");
    	
    	String returnStr = WapPayHttpUtil.sendPostHttp(url, param);
    	logger.info("获取授权信息authorization_info成功, url:{}, param:{}，returnStr：{}");
    	
    	if(returnStr != null){
    		 	JSONObject jsonObj = JSONObject.parseObject(returnStr); 
    		 	JSONObject authorization_info_jsonObj = jsonObj.getJSONObject("authorization_info");
		    	String authorizer_appid = authorization_info_jsonObj.getString("authorizer_appid");
		    	String authorizer_access_token = authorization_info_jsonObj.getString("authorizer_access_token");
		     	Integer expires_in = (Integer) authorization_info_jsonObj.get("expires_in");
		    	String authorizer_refresh_token = authorization_info_jsonObj.getString("authorizer_refresh_token");
//		    	JSONArray func_info_array = authorization_info_jsonObj.getJSONArray("func_info");
		    	IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
		    	
		    	logger.info("获取授权信息authorization_info成功, url:{}, param:{}，retMap{}，authorizer_appid{}，authorizer_access_token{},expires_in{},authorizer_refresh_token{}，func_info{}");
		    	if(StringUtils.isNotEmpty(authorizer_access_token)){
		    		String authorizer_access_token_groupKey = MemcachedKey.GROUP_KEY_authorizer_access_token;
		    		accessTokenCache.set(authorizer_access_token_groupKey   +  component_appid , authorizer_access_token);
//		    		memcachedService.set(authorizer_access_token_groupKey,  authorizer_access_token_groupKey + authorizer2_appid, 7000, authorizer_access_token);
		        	String authorizer_refresh_token_groupKey = MemcachedKey.GROUP_KEY_authorizer_refresh_token;
		        	
		        	accessTokenCache.set(authorizer_refresh_token_groupKey   +  component_appid , authorizer_refresh_token);
//		        	memcachedService.set(authorizer_refresh_token_groupKey,  authorizer_refresh_token_groupKey + authorizer2_appid, 7000, authorizer_refresh_token);
		    	}
	    	
    	}
		return returnStr;
	}
	
}

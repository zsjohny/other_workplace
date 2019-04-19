/**
 * 
 */
package com.yujj.web.controller.shop;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.newentity.weixinpay.WeixinPayCore;
import com.jiuyuan.service.common.MemcachedService;
import com.yujj.util.weixinpay.ShopConfig;
import com.yujj.web.controller.shop.weixin.util.WXBizMsgCrypt;
import com.yujj.web.controller.wap.pay2.WapPayHttpUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Service
public class WeiXinService {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinService.class);

    @Autowired
    private MemcachedService memcachedService;
    
    
    public static String component_appid = "wxddd55d6028f404ab";// ShopConfig.component_appid;//"wxb11529c136998cb6";
    public static String component_appsecret = "725058b52fa0f53f28d9a5a6bde1c0ea";//ShopConfig.component_appsecret;
	public static String component_encodingAesKey = "9HuP5H2P2Fk5wiZyYoWAAMjwJqq3Vt9HuP5H2P2Fk5w";//"abcdefghijklmnopqrstuvwxyz0123456789ABCDEFG";
	public static String component_token = "jiuyuan2017";//公众号消息校验Token
	
	
//	public static String authorizer1_appid = "wx95c37c75c641bc5e";//ShopConfig.authorizer_appid1;
//	public static String authorizer1_token = "yujiejie2017jiuyuan";
//	public static String authorizer1_encodingAesKey = "jSYLj1gmn9VQKVPH0EynevH3OKvdiu8nA1pmejXtIAN";
	
	public static String authorizer2_appid = "wx570bc396a51b8ff8";//微信自动化测试appid
    
    /**
     * 接收推送component_verify_ticket
     * @return
     */
	public void component_verify_ticket(String fromXML,String signature,String timestamp,String nonce,String encrypt_type,String msg_signature) throws Exception{
		logger.info("微信推送component_verify_ticket成功, fromXML:{},signature:{},timestamp:{},nonce:{}，encrypt_type:{}", fromXML,signature,timestamp,nonce,encrypt_type,msg_signature);
    	
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
    	
    	
    	logger.info("微信推送component_verify_ticket成功, AppId:{}, CreateTime:{}, InfoType:{}, ComponentVerifyTicket:{}", AppId, CreateTime,InfoType,ComponentVerifyTicket);
        if(StringUtils.isEmpty(AppId) || StringUtils.isEmpty(ComponentVerifyTicket)){
        	logger.info("微信推送component_verify_ticket成功, 但是AppId或ComponentVerifyTicket为空，AppId:{}, CreateTime:{}, InfoType:{}, ComponentVerifyTicket:{},", AppId, CreateTime,InfoType,ComponentVerifyTicket);
        }else{
        	String ticket_groupKey = MemcachedKey.GROUP_KEY_component_verify_ticket;
          	memcachedService.set(ticket_groupKey,  ticket_groupKey+AppId, 1000000, ComponentVerifyTicket);
          	String memcachedTicket = memcachedService.getStr(ticket_groupKey, ticket_groupKey+component_appid);
          	logger.info("微信推送component_verify_ticket成功, AppId:{}, CreateTime:{}, InfoType:{}, ComponentVerifyTicket:{},memcachedTicket:{}", AppId, CreateTime,InfoType,ComponentVerifyTicket,memcachedTicket);
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
    	String component_verify_ticket =  memcachedService.getStr(ticket_groupKey, ticket_groupKey+component_appid);
    	
    			
    	Map<String, String> paramMap = new HashMap<String, String>();
    	paramMap.put("component_appid", component_appid);
    	paramMap.put("component_appsecret", component_appsecret);
    	paramMap.put("component_verify_ticket", component_verify_ticket);
    	String param = JSONObject.fromObject(paramMap).toString();
    	//String param = "{\"component_appid\":"+component_appid+",\"component_appsecret\": "+component_appsecret+",\"component_verify_ticket\":"+component_verify_ticket+"}";
    	logger.info("获取第三方平台component_access_token成功, url:{}, param:{}", url, param);
    	
    	Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
    	//{errcode=41002, errmsg=appid missing hint: [Ln36_0041e575]}
    	//{errcode=61004, errmsg=access clientip is not registered hint: [ZJ6NTA0211e578] requestIP: 58.101.200.168}
    	
    	String component_access_token = (String) retMap.get("component_access_token");
    	logger.info("获取第三方平台component_access_token成功,ret:{} ", retMap.toString());
    	if(StringUtils.isNotEmpty(component_access_token)){
    		Integer expires_in = (Integer) retMap.get("expires_in");//7200
        	logger.info("获取第三方平台component_access_token成功,retMap:{} component_access_token:{}, expires_in:{}", retMap.toString(),component_access_token, expires_in);
    		memcachedService.set(token_groupKey,  token_groupKey+component_appid, 7000, component_access_token);
    		String newMemcachedToken = memcachedService.getStr(token_groupKey, token_groupKey+component_appid);
    		logger.info("获取第三方平台component_access_token成功,ret:{} component_access_token:{}, expires_in:{},newMemcachedToken:{}", retMap.toString(),component_access_token, expires_in,newMemcachedToken);
    	}
    	return component_access_token;
	}
    
    
  
    /**
     * 获取预授权码pre_auth_code
     * @return
     */
	public String get_pre_auth_code() {
		String pre_auth_code_groupKey = MemcachedKey.GROUP_KEY_pre_auth_code;
		String memcachedAuthCode = memcachedService.getStr(pre_auth_code_groupKey, pre_auth_code_groupKey + component_appid);
		if(StringUtils.isNotEmpty(memcachedAuthCode)){
			logger.info("获取第三方平台pre_auth_code成功,memcachedAuthCode:{}", memcachedAuthCode);
			return memcachedAuthCode;
		}
		
		//获取第三方平台component_access_token
    	String component_access_token = get_component_access_token();
    	String url = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token="+component_access_token;
     	Map<String, String> paramMap = new HashMap<String, String>();
    	paramMap.put("component_appid", component_appid);
    	String param = JSONObject.fromObject(paramMap).toString();
//    	String param = "{\"component_appid\":"+component_appid+"}";
    	logger.info("获取预授权码pre_auth_code成功, url:{}, param:{}", url, param);
    	
    	Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
    	logger.info("获取预授权码pre_auth_code成功,ret:{}",retMap.toString());
    	
    	String pre_auth_code = (String) retMap.get("pre_auth_code");
    	if(StringUtils.isNotEmpty(pre_auth_code)){
	    	Integer expires_in = (Integer) retMap.get("expires_in");
	    	logger.info("获取预授权码pre_auth_code成功,ret:{}, pre_auth_code:{}, expires_in:{}",retMap.toString(), pre_auth_code, expires_in);
	    	
	    	memcachedService.set(pre_auth_code_groupKey,  pre_auth_code_groupKey + component_appid, 600, pre_auth_code);
			String newMemcachedAuthCode = memcachedService.getStr(pre_auth_code_groupKey, pre_auth_code_groupKey + component_appid);
			logger.info("获取预授权码pre_auth_code成功,ret:{}, pre_auth_code:{}, expires_in:{},newMemcachedAuthCode:",retMap.toString(), pre_auth_code, expires_in,newMemcachedAuthCode);
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
		logger.info("公众号授权回调gongZhongHaoCallback成功,auth_code:{}, expires_in:{}",auth_code, expires_in);
		if(StringUtils.isNotEmpty(auth_code)){
    		String auth_code_groupKey = MemcachedKey.GROUP_KEY_auth_code;
        	memcachedService.set(auth_code_groupKey,  auth_code_groupKey + authorizer2_appid, 3000, auth_code);
        	String newMemcachedAuthCode = memcachedService.getStr(auth_code_groupKey, auth_code_groupKey + authorizer2_appid);
			logger.info("获取授权码auth_code成功,auth_code:{}, expires_in:{},newMemcachedAuthCode:{}", auth_code, expires_in,newMemcachedAuthCode);
			
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
    	String param = JSONObject.fromObject(paramMap).toString();
//		String param = "{\"component_appid\":"+component_appid+",\"authorization_code\":"+authorization_code+"}";
    	logger.info("获取授权信息authorization_info成功, url:{}, param:{}", url, param);
    	
    	String returnStr = WapPayHttpUtil.sendPostHttp(url, param);
    	logger.info("获取授权信息authorization_info成功, url:{}, param:{}，returnStr：{}", url, param,returnStr);
    	
    	if(returnStr != null){
    		 	JSONObject jsonObj = JSONObject.fromObject(returnStr); 
    		 	JSONObject authorization_info_jsonObj = jsonObj.getJSONObject("authorization_info");
		    	String authorizer_appid = authorization_info_jsonObj.getString("authorizer_appid");
		    	String authorizer_access_token = authorization_info_jsonObj.getString("authorizer_access_token");
		     	Integer expires_in = (Integer) authorization_info_jsonObj.get("expires_in");
		    	String authorizer_refresh_token = authorization_info_jsonObj.getString("authorizer_refresh_token");
		    	JSONArray func_info_array = authorization_info_jsonObj.getJSONArray("func_info");
		    	
		    	logger.info("获取授权信息authorization_info成功, url:{}, param:{}，retMap{}，authorizer_appid{}，authorizer_access_token{},expires_in{},authorizer_refresh_token{}，func_info{}",
		    			url, param,returnStr,authorizer_appid,authorizer_access_token,expires_in,authorizer_refresh_token,func_info_array.toString());
		    	if(StringUtils.isNotEmpty(authorizer_access_token)){
		    		String authorizer_access_token_groupKey = MemcachedKey.GROUP_KEY_authorizer_access_token;
		        	memcachedService.set(authorizer_access_token_groupKey,  authorizer_access_token_groupKey + authorizer2_appid, 7000, authorizer_access_token);
		        	String authorizer_refresh_token_groupKey = MemcachedKey.GROUP_KEY_authorizer_refresh_token;
		        	memcachedService.set(authorizer_refresh_token_groupKey,  authorizer_refresh_token_groupKey + authorizer2_appid, 7000, authorizer_refresh_token);
		    	}
	    	
    	}
		return returnStr;
	}

	/**
     * 5、获取（刷新）授权公众号或小程序的接口调用凭据（令牌）
     * 该API用于在授权方令牌（authorizer_access_token）失效时，可用刷新令牌（authorizer_refresh_token）获取新的令牌。
     * 请注意，此处token是2小时刷新一次，开发者需要自行进行token的缓存，避免token的获取次数达到每日的限定额度。缓存方法可以参考：http://mp.weixin.qq.com/wiki/2/88b2bf1265a707c031e51f26ca5e6512.html
     * https:// api.weixin.qq.com /cgi-bin/component/api_authorizer_token?component_access_token=xxxxx
     * POST数据示例:
		{
			"component_appid":"appid_value",//第三方平台appid
			"authorizer_appid":"auth_appid_value",//授权方appid
			"authorizer_refresh_token":"refresh_token_value",//授权方的刷新令牌，刷新令牌主要用于第三方平台获取和刷新已授权用户的access_token，只会在授权时刻提供，请妥善保存。一旦丢失，只能让用户重新授权，才能再次拿到新的刷新令牌
		}
		返回结果示例
		{
			"authorizer_access_token": "aaUl5s6kAByLwgV0BhXNuIFFUqfrR8vTATsoSHukcIGqJgrc4KmMJ-JlKoC_-NKCLBvuU1cWPv4vDcLN8Z0pn5I45mpATruU0b51hzeT1f8", //授权方令牌
			"expires_in": 7200, //有效期，为2小时
			"authorizer_refresh_token": "BstnRqgTJBXb9N2aJq6L5hzfJwP406tpfahQeLNxX0w"//刷新令牌
		}
     */
	public Map<String, Object> get_api_authorizer_token() {
		//获取第三方平台component_access_token
    	String component_access_token = get_component_access_token();
		String url = "https://api.weixin.qq.com /cgi-bin/component/api_authorizer_token?component_access_token="+component_access_token;
		String authorizer_refresh_token_groupKey = MemcachedKey.GROUP_KEY_authorizer_refresh_token;
		String authorizer_refresh_token = memcachedService.getStr(authorizer_refresh_token_groupKey, authorizer_refresh_token_groupKey + authorizer2_appid);
    
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("component_appid", component_appid);
		paramMap.put("authorizer_appid", authorizer2_appid);
		paramMap.put("authorizer_refresh_token", authorizer_refresh_token);
    	String param = JSONObject.fromObject(paramMap).toString();
		
    	logger.info("获取授权信息authorization_info成功, url:{}, param:{}", url, param);
    	
    	Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
    	logger.info("获取授权信息api_authorizer_token成功, url:{}, param:{}，retMap{}", url, param,retMap);
		
    	String new_authorizer_access_token = (String) retMap.get("authorizer_access_token");
    	Integer expires_in = (Integer) retMap.get("expires_in");
    	String new_authorizer_refresh_token = (String) retMap.get("authorizer_refresh_token");
    	String authorizer_access_token_groupKey = MemcachedKey.GROUP_KEY_authorizer_access_token;
    	memcachedService.set(authorizer_access_token_groupKey,  authorizer_access_token_groupKey + authorizer2_appid, 7000, new_authorizer_access_token);
    	memcachedService.set(authorizer_refresh_token_groupKey,  authorizer_refresh_token_groupKey + authorizer2_appid, 7000, new_authorizer_refresh_token);
    	logger.info("获取授权信息api_authorizer_token成功, url:{}, param:{}，retMap{},new_authorizer_access_token{},expires_in,new_authorizer_refresh_token", url, param,retMap,new_authorizer_access_token,expires_in,new_authorizer_refresh_token);
		
		return retMap;
	}

	 /**
     * 6、获取授权方的帐号基本信息
     * 该API用于获取授权方的基本信息，包括头像、昵称、帐号类型、认证类型、微信号、原始ID和二维码图片URL。
		需要特别记录授权方的帐号类型，在消息及事件推送时，对于不具备客服接口的公众号，需要在5秒内立即响应；而若有客服接口，则可以选择暂时不响应，而选择后续通过客服接口来发送消息触达粉丝。
		https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=xxxx
		POST数据示例:
		{
			"component_appid":"appid_value" ,//服务appid
			"authorizer_appid": "auth_appid_value" //授权方appid
		}
		返回结果示例
		{
		    "authorizer_info": {
		        "nick_name": "微信SDK Demo Special",//授权方昵称
		        "head_img": "http://wx.qlogo.cn/mmopen/GPy",//授权方头像
		        "service_type_info": {//授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号
		            "id": 2
		        },
		        "verify_type_info": {//授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
		            "id": 0
		        },
		        "user_name": "gh_eb5e3a772040",//授权方公众号的原始ID
		        "principal_name": "腾讯计算机系统有限公司",//公众号的主体名称
		        "alias": "paytest01",//授权方公众号所设置的微信号，可能为空
		        "business_info": {"open_store": 0, "open_scan": 0, "open_pay": 0, "open_card": 0,"open_shake": 0
		        },//用以了解以下功能的开通状况（0代表未开通，1代表已开通）：open_store:是否开通微信门店功能、 open_scan:是否开通微信扫商品功能、 open_pay:是否开通微信支付功能、 open_card:是否开通微信卡券功能、open_shake:是否开通微信摇一摇功能
		        "qrcode_url": "URL", //二维码图片的URL，开发者最好自行也进行保存
		    },
		    "authorization_info": {//授权信息
		        "appid": "wxf8b4f85f3a794e77",//授权方appid
		        "func_info": [//公众号授权给开发者的权限集列表，ID为1到15时分别代表：1消息管理权限、2用户管理权限、3帐号服务权限、4网页服务权限、5微信小店权限、6微信多客服权限、7群发与通知权限、8微信卡券权限、9微信扫一扫权限、10微信连WIFI权限、11素材管理权限、12微信摇周边权限、13微信门店权限、14微信支付权限、15自定义菜单权限
		            {"funcscope_category": {"id": 1 }},{ "funcscope_category": {"id": 2}}, { "funcscope_category": { "id": 3}}
		        ]
		    }
		}
   
     */
	@SuppressWarnings("unchecked")
	public String api_get_authorizer_info() {
		//获取第三方平台component_access_token
    	String component_access_token = get_component_access_token();
		String url = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token="+component_access_token;
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("component_appid", component_appid);
		paramMap.put("authorizer_appid", authorizer2_appid);
    	String param = JSONObject.fromObject(paramMap).toString();
//		String param = "{\"component_appid\":"+component_appid+",\"authorizer_appid\":"+authorizer_appid1+"}";
    	logger.info("获取授权方的帐号基本信息api_get_authorizer_info成功, url:{}, param:{}", url, param);
    	String returnStr = WapPayHttpUtil.sendPostHttp(url, param);
//    	Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
    	logger.info("获取授权方的帐号基本信息api_get_authorizer_info成功, url:{}, param:{}，returnStr{}", url, param,returnStr.toString());
    	
    	if(returnStr != null){
    		JSONObject jsonObj = JSONObject.fromObject(returnStr); 
    		JSONObject authorizer_info = jsonObj.getJSONObject("authorizer_info");
    		
    		if(authorizer_info != null){
    			String nick_name = authorizer_info.getString("nick_name");
    			String head_img = authorizer_info.getString("head_img");
    			String user_name = authorizer_info.getString("user_name");
    			String principal_name = authorizer_info.getString("principal_name");
    			String alias = authorizer_info.getString("alias");
    			String qrcode_url = authorizer_info.getString("qrcode_url");
            	logger.info("获取授权方的帐号基本信息api_get_authorizer_info成功, url:{}, param:{}，returnStr{},nick_name{}，head_img{},user_name{},principal_name{}，alias{}，qrcode_url{}", url, param,returnStr,nick_name,head_img,user_name,principal_name,alias,qrcode_url);
    		}
    		JSONArray authorization_info_array = jsonObj.getJSONArray("authorization_info");
        	logger.info("获取授权方的帐号基本信息api_get_authorizer_info成功, url:{}, param:{}，returnStr{},authorization_info_array{}", url, param,returnStr,authorization_info_array.toArray());
       	}
    	
    	return returnStr;
	}

	  /**
     * 7、获取授权方的选项设置信息
     * 该API用于获取授权方的公众号或小程序的选项设置信息，如：地理位置上报，语音识别开关，多客服开关。注意，获取各项选项设置信息，需要有授权方的授权，详见权限集说明。
     * https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_option?component_access_token=xxxx
     * POST数据示例
		{
//			"component_appid":"appid_value",//第三方平台appid
			"authorizer_appid": " auth_appid_value ",//授权公众号或小程序的appid
			"option_name": "option_name_value"//选项名称
		}
		返回结果示例
		{
			"authorizer_appid":"wx7bc5ba58cabd00f4",//授权公众号或小程序的appid
			"option_name":"voice_recognize",//选项名称
			"option_value":"1"//选项值
		}
     */
	public Map<String, Object> api_get_authorizer_option(String option_name) {
		//获取第三方平台component_access_token
    	String component_access_token = get_component_access_token();
		String url = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_option?component_access_token="+component_access_token;
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("component_appid", component_appid);
		paramMap.put("authorizer_appid", authorizer2_appid);
		paramMap.put("option_name", option_name);
    	String param = JSONObject.fromObject(paramMap).toString();
		
//		String param = "{\"component_appid\":"+component_appid+",\"authorizer_appid\":"+authorizer_appid1+",\"option_name\":"+option_name+"}";
		logger.info("获取授权信息api_get_authorizer_option成功, url:{}, param:{}", url, param);
    	
		Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
    	logger.info("获取授权信息api_get_authorizer_option成功, url:{}, param:{}，retMap{}", url, param,retMap);
    	
    	String new_authorizer_appid = (String) retMap.get("authorizer_appid");
    	String new_option_name = (String) retMap.get("option_name");
    	String option_value = (String) retMap.get("option_value");
    	logger.info("获取授权信息api_get_authorizer_option成功, url:{}, param:{}，retMap{},new_authorizer_appid{}，new_option_name{}，option_value{}", url, param,retMap,new_authorizer_appid,new_option_name,option_value);
    	
		return retMap;
	}

	/**
     *8、设置授权方的选项信息
     *API用于设置授权方的公众号或小程序的选项信息，如：地理位置上报，语音识别开关，多客服开关。注意，设置各项选项设置信息，需要有授权方的授权，详见权限集说明。
     *https://api.weixin.qq.com/cgi-bin/component/api_set_authorizer_option?component_access_token=xxxx
     *POST数据示例
		{
			"component_appid":"appid_value",//第三方平台appid
			"authorizer_appid": " auth_appid_value ",//授权公众号或小程序的appid
			"option_name": "option_name_value",//选项名称
			"option_value":"option_value_value"//设置的选项值
		}
		返回结果示例
		{
			"errcode":0,//错误码
			"errmsg":"ok"//错误信息
		}
		选项名和选项值表:
		location_report(地理位置上报选项):0无上报、1进入会话时上报、2每5s上报
		oice_recognize（语音识别开关选项）：0关闭语音识别、1开启语音识别
		customer_service（多客服开关选项）：0关闭多客服、1开启多客服
		
     */
	public Map<String, Object> api_set_authorizer_option(String option_name,String option_value) {
		//获取第三方平台component_access_token
    	String component_access_token = get_component_access_token();
		String url = "https://api.weixin.qq.com/cgi-bin/component/api_set_authorizer_option?component_access_token="+component_access_token;
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("component_appid", component_appid);
		paramMap.put("authorizer_appid", authorizer2_appid);
		paramMap.put("option_name", option_name);
		paramMap.put("option_value", option_value);
		
    	String param = JSONObject.fromObject(paramMap).toString();
		
//		String param = "{\"component_appid\":"+component_appid+",\"authorizer_appid\":"+authorizer_appid1+",\"option_name\":"+option_name+",\"option_value\":"+option_value+"}";
		logger.info("api_set_authorizer_option成功, url:{}, param:{}", url, param);
		
		Map<String, Object> retMap = WapPayHttpUtil.sendPostHttpReturnMap(url, param);
		logger.info("api_set_authorizer_option成功, url:{}, param:{}，retMap{}", url, param,retMap);
		
		String errcode = (String) retMap.get("errcode");
    	String errmsg = (String) retMap.get("errmsg");
    	logger.info("获取授权信息api_get_authorizer_option成功, url:{}, param:{}，retMap{},errcode{}，errmsg{}", url, param,retMap,errcode,errmsg);
    	
    	return retMap;
	}
	 
	/**
     *9、推送授权相关通知
     *当公众号对第三方平台进行授权、取消授权、更新授权后，微信服务器会向第三方平台方的授权事件接收URL（创建第三方平台时填写）推送相关通知。
     *第三方平台方在收到授权相关通知后也需进行解密（详细请见【消息加解密接入指引】），接收到后之后只需直接返回字符串success。为了加强安全性，postdata中的xml将使用服务申请时的加解密key来进行加密，具体请见【公众号第三方平台的加密解密技术方案】
     *POST数据示例（取消授权通知）
		<xml>
		<AppId>第三方平台appid</AppId>//第三方平台appid
		<CreateTime>1413192760</CreateTime>//时间戳
		<InfoType>unauthorized</InfoType>//unauthorized是取消授权，updateauthorized是更新授权，authorized是授权成功通知
		<AuthorizerAppid>公众号appid</AuthorizerAppid>公众号或小程序
		</xml>
		POST数据示例（授权成功通知）
		<xml>
		<AppId>第三方平台appid</AppId>
		<CreateTime>1413192760</CreateTime>
		<InfoType>authorized</InfoType>
		<AuthorizerAppid>公众号appid</AuthorizerAppid>
		<AuthorizationCode>授权码（code）</AuthorizationCode>//授权码，可用于换取公众号的接口调用凭据，详细见上面的说明
		<AuthorizationCodeExpiredTime>过期时间</AuthorizationCodeExpiredTime>//授权码过期时间
		</xml>
		POST数据示例（授权更新通知）
		<xml>
		<AppId>第三方平台appid</AppId>
		<CreateTime>1413192760</CreateTime>
		<InfoType>updateauthorized</InfoType>
		<AuthorizerAppid>公众号appid</AuthorizerAppid>
		<AuthorizationCode>授权码（code）</AuthorizationCode>
		<AuthorizationCodeExpiredTime>过期时间</AuthorizationCodeExpiredTime>
		</xml>
     */
	public void receive_auth_notification(String requestBody) {
    	logger.info("微信推送授权相关通知成功, requestBody:{}", requestBody);
    	Map<String, String> params = WeixinPayCore.decodeXml(requestBody);
    	String AppId = params.get("AppId");
    	String CreateTime = params.get("CreateTime");
    	String InfoType = params.get("InfoType");
    	String AuthorizerAppid = params.get("AuthorizerAppid");
    	String AuthorizationCode = params.get("AuthorizationCode");
    	String AuthorizationCodeExpiredTime = params.get("AuthorizationCodeExpiredTime");
    	logger.info("微信推送component_verify_ticket成功, AppId:{}, CreateTime:{}, InfoType:{}, AuthorizerAppid:{},AuthorizationCode{},AuthorizationCodeExpiredTime{}", AppId, CreateTime,InfoType,AuthorizerAppid,AuthorizationCode,AuthorizationCodeExpiredTime);
	}




	


	
	
}

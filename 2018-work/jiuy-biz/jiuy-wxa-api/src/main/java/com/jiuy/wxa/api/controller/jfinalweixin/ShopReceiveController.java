package com.jiuy.wxa.api.controller.jfinalweixin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.web.help.JsonResponse;
import com.store.service.StoreWxaService;
import com.store.service.WxaService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 接收处理小程序商家相关接口
 * 
 * 为JFinal 微信提供的业务http业务接口
 * @author 赵兴林
 * @since 2017-06-21
 */
@Controller
@RequestMapping("/wxa")
public class ShopReceiveController {
	 private static final Log logger = LogFactory.get();
    
	    @Autowired
	    WxaService wxaService;
	  
	    @Autowired
		StoreWxaService storeWxaService;
	 	/**
	  	 * 
		 * 保存小程序刷新token
		 * ok
		 * @return
		 */
	    @RequestMapping("/setRefreshToken")
	    @ResponseBody
		public JsonResponse setRefreshToken(String appId, String refreshToken) {
			logger.info("开始保存小程序刷新token,appId:"+appId+",refreshToken:"+refreshToken);
			JsonResponse jsonResponse = new JsonResponse();
			storeWxaService.setRefreshToken(appId,refreshToken);
			logger.info("保存小程序刷新token完成,appId:"+appId+",refreshToken:"+refreshToken);
			return jsonResponse.setSuccessful();
		}



		/**
		 * 从数据库获取刷新token
		 * @param
		 * @return
		 */
		@RequestMapping("/getRefreshToken")
	    @ResponseBody
		public  JsonResponse getRefreshToken(String appId) {
			logger.info(" 从数据库获取刷新token,appId:"+appId);
			JsonResponse jsonResponse = new JsonResponse();
			String refreshToken = storeWxaService.getRefreshToken(appId);
			logger.info(" 从数据库获取刷新tok生成二维码失败en完成,appId:"+appId+",refreshToken:"+refreshToken);
			Map<String, String> map = new HashMap<String, String>();
	    	map.put("refreshToken", refreshToken);
			return jsonResponse.setSuccessful().setData(map);
		}
		
	/**
	 * 授权
	 */
	@RequestMapping("/wxaAuth")
	@ResponseBody
    public JsonResponse wxaAuth(String storeId,String authorizer_appid,String authorizer_info_jsonstr) {
		logger.info("小程序授权给第三方：storeId："+storeId+"authorizer_appid"+authorizer_appid+",authorizer_info_jsonstr:"+authorizer_info_jsonstr);
		
    	String tipMsg = wxaService.wxaAuth(storeId,authorizer_appid, authorizer_info_jsonstr);
   	 	JsonResponse jsonResponse = new JsonResponse();
   	 	return jsonResponse.setSuccessful().setData(tipMsg);
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

	    @RequestMapping("/wxaAuthNotification")
	    @ResponseBody
    public JsonResponse wxaAuthNotification(String AppId, String CreateTime, String InfoType, String AuthorizerAppid,String AuthorizationCode,String AuthorizationCodeExpiredTime) {
    	 wxaService.wxaAuthNotification( AppId,  CreateTime, InfoType, AuthorizerAppid,AuthorizationCode,AuthorizationCodeExpiredTime);
   	 	JsonResponse jsonResponse = new JsonResponse();
   	 	return jsonResponse.setSuccessful();
    }
    
}



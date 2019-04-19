/**
 * 
 */
package com.jiuy.store.weixin.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.util.WebUtil;
import com.jiuyuan.util.spring.ControllerUtil;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 微信第三方授权
 * 文档地址：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1453779503&token=beb28404001a834eb809cd59e8275e8ce5c4ed12&lang=zh_CN
 */
@Controller
@RequestMapping("/weixin")
public class WeiXinController {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinController.class);



    
    @Autowired
    private WeiXinService weiXinService;
    
    /**
     * /weixin/component_verify_ticket.do?signature=1cabeab3f885e2a27e8fe55351e57d0065bd2a63&timestamp=1496654711&nonce=1268843946&encrypt_type=aes&msg_signature=751d4009411a47db02cb5d12065edffdb2aa564b, referer: null, ua: Mozilla/4.0, remoteIp: 10.252.145.26, message: Handler processing failed; nested exception is java.lang.ExceptionInInitializerError
     *1、推送component_verify_ticket协议
     *在第三方平台创建审核通过后，微信服务器会向其“授权事件接收URL”每隔10分钟定时推送component_verify_ticket。第三方平台方在收到ticket推送后也需进行解密（详细请见【消息加解密接入指引】），接收到后必须直接返回字符串success。
     * <xml>
	 *	  	<AppId> </AppId>
	 *		<CreateTime>1413192605 </CreateTime>
	 *		<InfoType> </InfoType>
	 *		<ComponentVerifyTicket> </ComponentVerifyTicket>
	 *	</xml>
     * 
     * @param requestBody  postdata中的XML体，将使用第三方平台申请时的接收消息的加密symmetric_key（也称为EncodingAESKey）来进行加密
     * @param signature
     * @param
     * @param
     * @param ，为aes
     * @param ，用于验证消息体的正确性
     * @return
     * @throws Exception 
     */
    @RequestMapping("/component_verify_ticket")
    @ResponseBody
    public String component_verify_ticket(String signature,String timestamp,String nonce,String encrypt_type,String msg_signature,@RequestBody String requestBody) throws Exception {
    	//@RequestBody String requestBody ,String signature,String timestamp,String nonce,String encrypt_type,String msg_signature 
    	//测试使用
//       String testRequestBody = "<xml>"+
//        							"<AppId>123456</AppId>"+
//        							"<CreateTime>1413192605</CreateTime>"+
//        							"<InfoType>aaa</InfoType>"+
//        							"<ComponentVerifyTicket>bbb</ComponentVerifyTicket>"+
//        				"</xml>";
    	
//       String testRequestBody = "<xml>"+
//    		   		"<AppId><![CDATA[wxddd55d6028f404ab]]></AppId>"+
//    		   		"<Encrypt><![CDATA[zghpfXpVF70RDWeoRLTvLe9iaqzxfuP4OmiBmHm6mBnu2jfeD/PThlApKIEKBEIyOJICTUU0Sj/80c05gZW4VYPbCvGlfqJhgOXbKMnbM3oZnpC1KLk7kuChLl24DIF+IaXOA6DFoGHih5RriDy6+ahE4MPz7YApBwjvhhHbsWoxUtFj6fYg8ME8xCQbZuy/5v+JjM6nmGxjdc95v7V9mGBix5DbdBzmFHH2LWi7tbvcgr4Y/PJSSA3iFz6Z9B5GgcNY9sSEIzuTw7wrKPQx27De+9iZ1ZHnqkNgjanCmweZOI3eWl8FQbFv+zFtqfA6mefxbJhnCWanugsBIYYJTrIPFCSFl+nIppl9fMzzYojiaY91qCVbphx21iWRY8pEQER4OS68weCjoqtZiTchh8D9S62OdueWXFS1V6GMENQTMa0dioVhdoMzwTU1tDlG70jnHi1um+C5A4at8heEAw==]]></Encrypt>"+
//    		   "</xml>";
//     
//       String signature = "1cabeab3f885e2a27e8fe55351e57d0065bd2a63";
//       String timestamp = "1496654711";
//       String nonce = "1268843946";
//       String encrypt_type = "aes";
//       String msg_signature = "751d4009411a47db02cb5d12065edffdb2aa564b";
    	weiXinService.component_verify_ticket(requestBody,signature,timestamp,nonce,encrypt_type,msg_signature);//requestBody
    	return "success";
    }
    
    
    
    
    /**
     * 该方法完成待测试，TODO 改接口需要定时刷新获取最新token,过期时间需要根据接口数据进行设置缓存失效时间
     * 2、获取第三方平台component_access_token
     * 第三方平台compoment_access_token是第三方平台的下文中接口的调用凭据，也叫做令牌（component_access_token）。每个令牌是存在有效期（2小时）的，且令牌的调用不是无限制的，请第三方平台做好令牌的管理，在令牌快过期时（比如1小时50分）再进行刷新。
     https://api.weixin.qq.com/cgi-bin/component/api_component_token
     POST数据示例:
		{
			"component_appid":"appid_value" ,//第三方平台appid
			"component_appsecret": "appsecret_value", //第三方平台appsecret
			"component_verify_ticket": "ticket_value" //微信后台推送的ticket，此ticket会定时推送，具体请见本页的推送说明
		}
		返回结果示例
		{
			"component_access_token":"61W3mEpU66027wgNZ_MhGHNQDHnFATkDa9-2llqrMBjUwxRSNPbVsMmyD-yq8wZETSoE5NQgecigDrSHkPtIYA", //第三方平台access_token
			"expires_in":7200 //有效期
		}
     * @return
     */
    @RequestMapping("/compoment_access_token")
    @ResponseBody
    public JsonResponse compoment_access_token() {
    	//获取第三方平台component_access_token
    	String component_access_token = weiXinService.get_component_access_token();
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String,String> returnMap = new HashMap();
    	returnMap.put("component_access_token", component_access_token);
    	jsonResponse.setData(returnMap);
    	return jsonResponse.setSuccessful();
    }
    
    /**
     * 3、获取预授权码pre_auth_code
     * 该API用于获取预授权码。预授权码用于公众号或小程序授权时的第三方平台方安全验证。
     * https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=xxx
     * POST数据示例:
		{
			"component_appid":"appid_value" //第三方平台方appid
		}
		返回结果示例
		{
			"pre_auth_code":"Cx_Dk6qiBE0Dmx4EmlT3oRfArPvwSQ-oa3NL_fwHM7VI08r52wazoZX2Rhpz1dEw",//预授权码
			"expires_in":600//有效期，为20分钟
		}
     * @return
     */
    @RequestMapping("/pre_auth_code")
    @ResponseBody
    public JsonResponse pre_auth_code() {
    	//获取第三方平台component_access_token
    	String pre_auth_code = weiXinService.get_pre_auth_code();
        
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String,String> returnMap = new HashMap();
    	returnMap.put("pre_auth_code", pre_auth_code);
    	jsonResponse.setData(returnMap);
    	return jsonResponse.setSuccessful();
    }
    
    /**
	 * 跳转到公众号授权页面
	 * @return
	 */
    @RequestMapping("/gotoGongZhongHaoAuth")
    public String gotoGongZhongHaoAuth(HttpServletRequest request
    		) throws UnsupportedEncodingException {//@RequestParam("returnUrl") String returnUrl
    	 String webBaseUrl = WebUtil.getWebBaseUrl(request);
    	 String gongZhongHaoAuthUrl = weiXinService.getGongZhongHaoAuthUrl(webBaseUrl);
    	 return ControllerUtil.redirect(gongZhongHaoAuthUrl);
    } 
   
    
    /**
  	 * 公众号授权回调路径（经测算访问出错）
  	 * http://test.yujiejie.com/weixin/gongZhongHaoCallback.do
  	 * ?auth_code=queryauthcode@@@wIrz7IS4oW7n3FOed8cGDVm_9W6a1sPdkOekO16Q9cXOynfIYeVkWFX9TLv9KMOlFgAXlXKlOd5v2fDm_rzCPA
  	 * &expires_in=3600
  	 * @return
  	 */
      @RequestMapping("/gongZhongHaoCallback")
      public String gongZhongHaoCallback(HttpServletRequest request ,String auth_code,String expires_in) throws UnsupportedEncodingException {
    	 weiXinService.gongZhongHaoCallback(auth_code,expires_in);
    	 
    	 String webBaseUrl = WebUtil.getWebBaseUrl(request);
    	 logger.info("公众号授权回调gongZhongHaoCallback成功,webBaseUrl:{}",webBaseUrl);
      	 return ControllerUtil.redirect(webBaseUrl);
      }
      
      
      
      
      
      
      
    
    /**
     * 4、使用授权码换取公众号或小程序的接口调用凭据和授权信息
     * 该API用于使用授权码换取授权公众号或小程序的授权信息，并换取authorizer_access_token和authorizer_refresh_token。
     *  授权码的获取，需要在用户在第三方平台授权页中完成授权流程后，在回调URI中通过URL参数提供给第三方平台方。
     * 请注意，由于现在公众号或小程序可以自定义选择部分权限授权给第三方平台，
     * 因此第三方平台开发者需要通过该接口来获取公众号或小程序具体授权了哪些权限，而不是简单地认为自己声明的权限就是公众号或小程序授权的权限。
     */
    @RequestMapping("/authorization_info")
    @ResponseBody
    public JsonResponse authorization_info(String authorization_code) {
    	String authorization_info = weiXinService.get_authorization_info(authorization_code);
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String,Object> returnMap = new HashMap<String,Object>();
    	returnMap.put("authorization_info", authorization_info);
    	jsonResponse.setData(returnMap);
    	return jsonResponse.setSuccessful();
    	
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
    @RequestMapping("/api_authorizer_token")
    @ResponseBody
    public JsonResponse api_authorizer_token() {
    	Map<String, Object> retMap = weiXinService.get_api_authorizer_token();
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String,Object> returnMap = new HashMap<String,Object>();
    	returnMap.put("api_authorizer_token", retMap);
    	jsonResponse.setData(returnMap);
    	return jsonResponse.setSuccessful();
    	
    }
    
    
    
    /**
     * 6、获取授权方的帐号基本信息
     * 该API用于获取授权方的基本信息，包括头像、昵称、帐号类型、认证类型、微信号、原始ID和二维码图片URL。
		需要特别记录授权方的帐号类型，在消息及事件推送时，对于不具备客服接口的公众号，需要在5秒内立即响应；而若有客服接口，则可以选择暂时不响应，而选择后续通过客服接口来发送消息触达粉丝。
     */
    @RequestMapping("/api_get_authorizer_info")
    @ResponseBody
    public JsonResponse api_get_authorizer_info() {
    	String returnStr = weiXinService.api_get_authorizer_info();
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String,Object> returnMap = new HashMap<String,Object>();
    	returnMap.put("api_authorizer_token", returnStr);
    	jsonResponse.setData(returnMap);
    	return jsonResponse.setSuccessful();
    	
    }
    
    /**
     * 7、获取授权方的选项设置信息
     * 该API用于获取授权方的公众号或小程序的选项设置信息，如：地理位置上报，语音识别开关，多客服开关。注意，获取各项选项设置信息，需要有授权方的授权，详见权限集说明。
     * https://api.weixin.qq.com/cgi-bin/component/ api_get_authorizer_option?component_access_token=xxxx
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
    @RequestMapping("/api_get_authorizer_option")
    @ResponseBody
    public JsonResponse api_get_authorizer_option(String option_name) {
    	Map<String, Object> retMap = weiXinService.api_get_authorizer_option(option_name);
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String,Object> returnMap = new HashMap<String,Object>();
    	returnMap.put("api_get_authorizer_option", retMap);
    	jsonResponse.setData(returnMap);
    	return jsonResponse.setSuccessful();
    	
    }
    
    
    
    
    
    /**
     *8、设置授权方的选项信息
     *API用于设置授权方的公众号或小程序的选项信息，如：地理位置上报，语音识别开关，多客服开关。注意，设置各项选项设置信息，需要有授权方的授权，详见权限集说明。
     *https://api.weixin.qq.com/cgi-bin/component/ api_set_authorizer_option?component_access_token=xxxx
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
    @RequestMapping("/api_set_authorizer_option")
    @ResponseBody
    public JsonResponse api_set_authorizer_option(String option_name,String option_value) {
    	Map<String, Object> retMap = weiXinService.api_set_authorizer_option(option_name,option_value);
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String,Object> returnMap = new HashMap<String,Object>();
    	returnMap.put("api_set_authorizer_option", retMap);
    	jsonResponse.setData(returnMap);
    	return jsonResponse.setSuccessful();
    	
    }
    
    /**
     *9、推送授权相关通知
     *当公众号对第三方平台进行授权、取消授权、更新授权后，微信服务器会向第三方平台方的授权事件接收URL（创建第三方平台时填写）推送相关通知。
     *第三方平台方在收到授权相关通知后也需进行解密（详细请见【消息加解密接入指引】），接收到后之后只需直接返回字符串success。为了加强安全性，postdata中的xml将使用服务申请时的加解密key来进行加密，具体请见【公众号第三方平台的加密解密技术方案】
     */
    @RequestMapping("/receive_auth_notification")
    @ResponseBody
    public String receive_auth_notification(@RequestBody String requestBody) {
    	weiXinService.receive_auth_notification(requestBody);
     	return "success";
    }
}

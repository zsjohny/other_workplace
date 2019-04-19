package com.yujj.web.controller.wap;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.OAuthCallbackModule;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.service.common.YunXinSmsService;
import com.jiuyuan.util.CheckUtil;
import com.jiuyuan.util.oauth.common.Display;
import com.jiuyuan.util.oauth.sns.weixin.WeiXinV2API;
import com.jiuyuan.util.oauth.v2.credential.IRawDataV2TokenCredentials;
import com.jiuyuan.util.spring.ControllerUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.service.UserService;
import com.yujj.entity.account.UserDetail;
import com.yujj.util.uri.UriParams;
import com.yujj.web.controller.delegate.LoginDelegator;

@Controller
@RequestMapping("/m/ext/oauth")
public class WapExternalOAuthController {
    private static final Logger logger = LoggerFactory.getLogger(WapExternalOAuthController.class);
    public static final String ATTR_NAME_TOKEN_CREDENTIALS = "OAUTH_TOKEN_CREDENTIALS";

    public static final String ATTR_NAME_TARGET_URL = "OAUTH_TARGET_URL";
    @Autowired
    private YunXinSmsService yunXinSmsService;
    @Autowired
	 private LoginDelegator loginDelegator;
    @Autowired
    private UserService userService;
    
    @Autowired
    @Qualifier("weiXinV2API4MP")
    private WeiXinV2API weiXinV2API;
    
      @RequestMapping("/test123")
      public String test123() {
//          var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe1169bab39d015c6&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
       String path = "";
       System.out.println("path================"+path);
        return path;
      } 
    
   
    
   
    
    /**
     * 由于微信服务器将发送GET请求到应用上，所以首先创建authurl来处理这个验证请求。
     * 验证服务器地址有效性：微信服务器将发送GET请求到填写的服务器地址URL上，GET请求携带四个参数
     * 例子：http://dev.yujiejie.com:8080/m/ext/oauth/authurl.do?signature=098871962aaa754474edd69b8b255538751325ce&echostr=1407812100401020765&timestamp=1491473173&nonce=591408126
     * @param signature 微信加密签名
     * @param echostr  随机字符串
     * @param timestamp  时间戳
     * @param nonce  随机数
     * @return
     * @throws IOException 
     */
    @RequestMapping("/authurl")
    public String authurl(HttpServletRequest request, HttpServletResponse response,
    						@RequestParam("signature") String signature, 
    						@RequestParam("timestamp") String timestamp, 
                            @RequestParam(value = "nonce") String nonce,
                            @RequestParam("echostr") String echostr ) throws IOException {

    	 	PrintWriter out = response.getWriter();
            if(CheckUtil.checkSignature(signature, timestamp, nonce)){
                out.print(echostr);
            }else{
            	response.getWriter().append("Served at: ").append(request.getContextPath());
            }
            return null;
    }
    
    
  
    
   
   
   
   //手机用户 绑定微信时发送短信
   @RequestMapping(value = "/wapWeixinBindPhoneSendMsg", method = RequestMethod.POST)
   @ResponseBody
   public JsonResponse extWeixinSendMsg(@RequestParam("code") String code,@RequestParam("phone") String phone, UserDetail userDetail,HttpServletResponse response) {
	   	JsonResponse jsonResponse = new JsonResponse();
   		Map<String, Object> data = new HashMap<String, Object>();
   		jsonResponse.setData(data);
//   		//1、通过code换取网页授权access_token
//   		IAccessToken accessToken =  weiXinV2API.getWapAccessToken(code);
//    	if(accessToken == null){
//    		return jsonResponse.setResultCode(ResultCode.WAP_GET_WEIXIN_INFO_ERROR);//获取微信用户信息失败
//    	}
//    	String weixinId = accessToken.getOpenid();
//    	 //2、拉取用户信息
//		String access_token = accessToken.getAccessToken();
//		WapWeixinUser wapWeixinUser = weiXinV2API.getWapWeixinUser(access_token,weixinId);
//       	if(wapWeixinUser == null ){
//       		return jsonResponse.setResultCode(ResultCode.WAP_GET_WEIXIN_INFO_ERROR);
//       	}
        //2、获取应用中用户对象
        int ret = userService.weixinBindPhoneSendMsg(phone);
        if(ret == -1){//手机号已经绑定微信不能再次绑定微信
        	return jsonResponse.setResultCode(ResultCode.WAP_WEIXIN_BIND_PHONE_ERROR_PHONE_ERROE);
        }else if(ret == -2){
        	return jsonResponse.setResultCode(ResultCode.PHONE_SEND_FAIL);
        }
        
        return jsonResponse.setSuccessful();
   }
   
    

    @RequestMapping("/authorize")
    public String authorize(@RequestParam("vendor_type") String vendorType, String module,
                            @RequestParam(value = "target_url") String targetUrl) {
        UriParams callbackParams = new UriParams();
        callbackParams.add("vendor_type", vendorType).add("module", module).set("target_url", targetUrl);

        return ControllerUtil.redirect(weiXinV2API.getAuthorizeUrlFit(callbackParams, "", Display.MOBILE));
    }

    @RequestMapping(value = "/callback")
    public String callback(String code, @RequestParam("vendor_type") String vendorType, String version, String module,
                           @RequestParam(value = "target_url") String targetUrl, Map<String, Object> model) {
        UriParams callbackParams = new UriParams();
        callbackParams.add("vendor_type", vendorType).add("module", module).set("target_url", targetUrl);

        IRawDataV2TokenCredentials tokenCredentials = weiXinV2API.getTokenCredentials(code, null, callbackParams);
        model.put(ATTR_NAME_TOKEN_CREDENTIALS, tokenCredentials);
        model.put(ATTR_NAME_TARGET_URL, targetUrl);
        return OAuthCallbackModule.getUrl(module);
    }

}

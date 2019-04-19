package com.yujj.web.controller.wap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//import com.jiuyuan.business.service.common.SecurityService;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.account.UserRegSource;
import com.jiuyuan.constant.account.UserStatus;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.account.BindUserRelation;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.ext.spring.web.method.RequestAttribute;
import com.jiuyuan.service.SecurityService;
import com.jiuyuan.service.WhitePhoneService;
import com.jiuyuan.service.common.YunXinSmsService;
import com.jiuyuan.util.EncodeUtil;
import com.jiuyuan.util.LogUtil;
import com.jiuyuan.util.WebUtil;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.oauth.sns.common.response.ISnsResponse;
import com.jiuyuan.util.oauth.sns.common.response.SnsResponseType;
import com.jiuyuan.util.oauth.sns.common.user.IAccessToken;
import com.jiuyuan.util.oauth.sns.common.user.ISnsEndUser;
import com.jiuyuan.util.oauth.sns.common.user.WapWeixinUser;
import com.jiuyuan.util.oauth.sns.weixin.WeiXinV2API;
import com.jiuyuan.util.oauth.v2.credential.IRawDataV2TokenCredentials;
import com.jiuyuan.util.spring.ControllerUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.RegisterFacade;
import com.yujj.business.service.UserService;
import com.yujj.business.service.UserSharedService;
import com.yujj.entity.account.User;
import com.yujj.entity.account.UserDetail;
import com.yujj.web.controller.delegate.LoginDelegator;

@Controller
@RequestMapping("/m/login")
public class WapLoginController {
    
    private static final Logger logger = LoggerFactory.getLogger(WapLoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private RegisterFacade registerFacade;

    @Autowired
    private LoginDelegator loginDelegator;

    @Autowired
    @Qualifier("weiXinV2API4MP")
    private WeiXinV2API weiXinV2API;
    
    @Autowired
	private WhitePhoneService whitePhoneService;
    
    @Autowired
    private YunXinSmsService yunXinSmsService;
    @Autowired
    private UserSharedService userSharedService;


    /**
     * 获得登陆用户ID（可用来验证用户是否登陆）
     * 
     * @param reqdev
     * @param skuId
     * @param count
     * @param userDetail
     * @return
     */
    @RequestMapping("/getLoginUserId")
    @ResponseBody
    public JsonResponse getLoginUserId(HttpServletRequest request, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String,Object> data =new HashMap<String,Object>();
    	long userId = userDetail.getUserId();
    	if(userId == 0){
    		data.put("userId", 0);
    	}else{
    		User user = userService.checkUserByUserId(userId);
        	if(user == null){
        		data.put("userId", 0);
        	}else{
        		data.put("userId", userId);
        	}
    	}
   		return jsonResponse.setSuccessful().setData(data);
    }
   
    
   
      
      /**
    	 * 第一步：跳转微信授权页面
    	 * 跳转指定页面并进行微信授权
    	 * @return
    	 */
    @RequestMapping("/gotoWxAuthorize")
    public String gotoWxAuthorize(HttpServletRequest request, UserDetail userDetail,@RequestParam("backUrl") String backUrl) {
    	
      	String webBaseUrl =   WebUtil.getWebBaseUrl(request);
      	String authorizeResultUrl = webBaseUrl + "/m/login/authorizeResult.do?returnUrl="+backUrl;//EncodeUtil.encodeURL(backUrl);
      	String wapAuthorizeUrl = getWapAuthorizeUrl(request, userDetail, authorizeResultUrl);
      	logger.info(LogUtil.getLogPrefix(request)+"wapAuthorizeUrl[{}];returnUrl[{}]",wapAuthorizeUrl,backUrl);
      	
        return ControllerUtil.redirect(wapAuthorizeUrl);
    }
//    /**
//  	 * 跳转确认订单页面
//  	 * 
//  	 * @return
//  	 */
//      @RequestMapping("/gotoConfirmOrder")
//      public String gotoConfirmOrder(HttpServletRequest request, UserDetail userDetail,
//    		  @RequestParam("userSharedRecordId") long userSharedRecordId,
//    		  @RequestParam("backUrl") String backUrl) {
//    	String webBaseUrl =   WebUtil.getWebBaseUrl(request);
////    	String backUrl = webBaseUrl + "/static/wap/order.html?sid_count="+sid_count;
//    	String authorizeResultUrl = webBaseUrl + "/m/login/confirmOrderAuthorizeResult.do?userSharedRecordId="+userSharedRecordId+"&returnUrl="+EncodeUtil.encodeURL(backUrl);
//    	String gotoUrl = getWapAuthorizeUrl(request, userDetail, authorizeResultUrl);
//        return ControllerUtil.redirect(gotoUrl);
//      }
   
    /**
   	 * 第二步：跳转到授权结果页面,获得微信openId进行尝试登陆
   	 * 说明：授权后页面会跳到此路径，根据授权code获得accessToken
   	 * （会带上code、state、returnUrl（成功后要跳转的路径））
   	 * 如果能登陆则进行登陆
   	 * code说明 ： code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
   	 * @return
   	 */
   	@RequestMapping("/authorizeResult")
   	public String authorizeResult(HttpServletRequest request,HttpServletResponse response,
   			@RequestParam("returnUrl") String returnUrl,
   			@RequestParam(value = "code", required = false, defaultValue = "") String code,
   			@RequestParam(value = "state", required = false, defaultValue = "") String state, 
   			UserDetail userDetail
   			,ClientPlatform clientPlatform,@ClientIp String ip
   			) {
   		logger.info(LogUtil.getLogPrefix(request)+"code[{}];state[{}];returnUrl[{}]",code,state,returnUrl);
   		JsonResponse jsonResponse = new JsonResponse();
   		Map<String, Object> retMap = tryLoginByCode(code,userDetail,clientPlatform, ip,response);
   		int retCode = (Integer) retMap.get("code");
   		if(retCode == -1){
   			logger.info(LogUtil.getLogPrefix(request)+"ResultCode[{}]",ResultCode.WAP_GET_WEIXIN_INFO_ERROR.toString());
   			return ControllerUtil.redirect(returnUrl);
   		}else if(retCode == -2){
   			logger.info(LogUtil.getLogPrefix(request)+"ResultCode[{}]",ResultCode.WAP_WEIXIN_NOT_FIND_USERINFO.toString());
   			return ControllerUtil.redirect(returnUrl);
   		}else{
   			return ControllerUtil.redirect(returnUrl);
   		}
   	}
   	
    /**
     * 
   	 * 第二步：跳转到授权结果页面,获得微信openId进行尝试登陆
   	 * 说明：授权后页面会跳到此路径，根据授权code获得accessToken
   	 * （会带上code、state、returnUrl（成功后要跳转的路径））
   	 * 如果能登陆则进行登陆
   	 * code说明 ： code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
   	 * @return
   	 */
   	@RequestMapping("/confirmOrderAuthorizeResult")
   	public String confirmOrderAuthorizeResult(HttpServletRequest request,HttpServletResponse response,
   			@RequestParam("userSharedRecordId") String userSharedRecordId,
   			@RequestParam("returnUrl") String returnUrl,
   			@RequestParam(value = "code", required = false, defaultValue = "") String code,
   			@RequestParam(value = "state", required = false, defaultValue = "") String state, 
   			UserDetail userDetail
   			,ClientPlatform clientPlatform,@ClientIp String ip) {
   		logger.info(LogUtil.getLogPrefix(request)+"code[{}];state[{}];returnUrl[{}]",code,state,returnUrl);
   		JsonResponse jsonResponse = new JsonResponse();
   		Map<String, Object> retMap = tryLoginByCode(code,userDetail,clientPlatform, ip,response);
   		int retCode = (Integer) retMap.get("code");
   		IAccessToken accessToken = (IAccessToken) retMap.get("accessToken");
   		if(retCode == -1){
   			logger.info(LogUtil.getLogPrefix(request)+"ResultCode[{}]",ResultCode.WAP_GET_WEIXIN_INFO_ERROR.toString());
   			//如果未登录确认订单页面要能自动跳回商品详情页面
   			return ControllerUtil.redirect(returnUrl);
   		}else if(retCode == -2){
   			logger.info(LogUtil.getLogPrefix(request)+"ResultCode[{}]",ResultCode.WAP_WEIXIN_NOT_FIND_USERINFO.toString());
   			return ControllerUtil.redirect("/m/login/wapAuthorizeByNoSilent.do?userSharedRecordId="+userSharedRecordId+"&returnUrl="+returnUrl);
   		}else{
   			return ControllerUtil.redirect(returnUrl);
   		}
   	}

      /**
       * 获得调转URL（如果没登陆则进行静默授权，如果已登录则不需要静默授权直接跳转）
       * @param request
       * @param userDetail
       * @param confirmOrdeUrl
       * @return
       */
	private String getWapAuthorizeUrl(HttpServletRequest request, UserDetail userDetail, String returnUrl) {
		String gotoUrl = null;
    	 BindUserRelation bindUserRelation = null;
    	long userId = userDetail.getUserId();
    	User currentUser = userDetail.getUser();
    	if(userId > 0){
    	    String weixinId = currentUser.getWeixinId();
            bindUserRelation = userService.getBindUserRelationByUid(weixinId);
    	}
    	
    
      	if (userId > 0 && bindUserRelation != null) {
      		gotoUrl = returnUrl;
  		}else{
  			try {
  				gotoUrl = weiXinV2API.getWapAuthorizeUrlBySilent(request,returnUrl);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				logger.error(" is error e:{}",e.getMessage());
			}
  		}
		return gotoUrl;
	}
      
      
      
	
   	
//      /**
//       * 登陆用根据授权code
//       * @param reqdev
//       * @param skuId
//       * @param count
//       * @param userDetail
//       * @return
//       */
//      
//      @RequestMapping("/wapUserLogin")
//      @ResponseBody
//      public JsonResponse wapUserLogin(HttpServletRequest request,HttpServletResponse response,
//     			@RequestParam(value = "code", required = false, defaultValue = "") String code,
//     			@RequestParam(value = "state", required = false, defaultValue = "") String state, 
//     			UserDetail userDetail) {
//      	//skuCountPairArray = new String[]{"479:1"};
//    	logger.info("--------WAP--------wapUserLogin =  userDetail:"+userDetail.getUserId());
//      	JsonResponse jsonResponse = new JsonResponse();
//      	Map<String, Object> retMap = tryLoginByCode(code,userDetail,response);
//      	int retCode = (Integer) retMap.get("code");
//   		if(retCode == -1){
//   			logger.info(LogUtil.getLogPrefix(request)+"ResultCode[{}]",ResultCode.WAP_GET_WEIXIN_INFO_ERROR.toString());
//   			logger.error("获取微信用户信息失败");
//       		return jsonResponse.setResultCode(ResultCode.WAP_GET_WEIXIN_INFO_ERROR);//获取微信用户信息失败
//   		}else if(retCode == -2){
//   			logger.info(LogUtil.getLogPrefix(request)+"ResultCode[{}]",ResultCode.WAP_WEIXIN_NOT_FIND_USERINFO.toString());
//   			IAccessToken accessToken= (IAccessToken) retMap.get("accessToken");
//   			return jsonResponse.setResultCode(ResultCode.WAP_WEIXIN_NOT_FIND_USERINFO).setData(accessToken);
//   		}else{
//   			return jsonResponse.setSuccessful();
//   		}
//      }
      
      
      /**
       *  静默授权后尝试登陆
       *  TODO 这里的逻辑代码暂时可用，需要以后进行优化整理一下
       * @param code
       * @param userDetail
       * @param response
       * @return
       */
      private Map<String, Object> tryLoginByCode(String code,UserDetail userDetail
    		  ,ClientPlatform clientPlatform, String ip
    		  ,HttpServletResponse response){
    	  Map<String, Object>  retMap = new HashMap<String, Object>();
     		if (userDetail.getUserId() <= 0) {//如果未登录则进行登陆
    		  //1、通过code换取网页授权access_token
    			if(StringUtils.isEmpty(code)){
    				logger.error("wapUserLogin cod is null code:"+code);
    				retMap.put("code", -1);
    				return retMap;//jsonResponse.setResultCode(ResultCode.WAP_GET_WEIXIN_INFO_ERROR);//获取微信用户信息失败
    			}
    			IAccessToken accessToken =  weiXinV2API.getWapAccessToken(code);
             	if(accessToken == null){
             		logger.error("获取微信用户信息失败");
             		retMap.put("code", -1);
    				return retMap;//jsonResponse.setResultCode(ResultCode.WAP_GET_WEIXIN_INFO_ERROR);//获取微信用户信息失败
             	}
             	String publicOpenId = accessToken.getOpenid();
             	 
             	//2、获取应用中用户对象     User user = userService.getUserByAllWay(weixinId);
                User user = userService.getUserByPublicOpenId(publicOpenId);
        		//手机用户类型或已经绑定手机
        		if(user == null){
        			//需要跳转到非静默授权页面
        			retMap.put("code", -2);
        		 	retMap.put("accessToken", accessToken);
    				return retMap;//jsonResponse.setResultCode(ResultCode.WAP_WEIXIN_GOTO_NOT_SILENT).setData(accessToken);
        		}else if(StringUtils.isEmpty(user.getBindPhone()) &&  UserType.PHONE.getIntValue() != user.getUserType().getIntValue()){
        			//需要跳转到非静默授权页面
        			retMap.put("code", -2);
        		 	retMap.put("accessToken", accessToken);
    				return retMap;//jsonResponse.setResultCode(ResultCode.WAP_WEIXIN_GOTO_NOT_SILENT).setData(accessToken);
        		}
        		userService.loginUser(response,user,ip,clientPlatform);
        		logger.error("--------WAP--------wapUserLogin is ok =  user:"+user.toString());
  	  	 }else{//或者关联关系记录无
  	  		User user = userDetail.getUser();
  	  		String weixinId = user.getWeixinId();
  	  		BindUserRelation bindUserRelation = userService.getBindUserRelationByUid(weixinId);
  	  		//绑定关系为空或没绑定手机
  	  		if(bindUserRelation == null || !user.checkBindPhone()){
  	  			 //1、通过code换取网页授权access_token
  	  			if(StringUtils.isEmpty(code)){
  	  				logger.error("wapUserLogin cod is null code:"+code);
  	  				retMap.put("code", -1);
  	  				return retMap;//jsonResponse.setResultCode(ResultCode.WAP_GET_WEIXIN_INFO_ERROR);//获取微信用户信息失败
  	  			}
  	    	    IAccessToken accessToken =  weiXinV2API.getWapAccessToken(code);
  	           	if(accessToken == null){
  	           		logger.error("获取微信用户信息失败");
  	           		retMap.put("code", -1);
  	           		return retMap;//jsonResponse.setResultCode(ResultCode.WAP_GET_WEIXIN_INFO_ERROR);//获取微信用户信息失败
  	           	}
  	          //需要跳转到非静默授权页面
  	           	retMap.put("code", -2);
  	           	retMap.put("accessToken", accessToken);
				return retMap;//jsonResponse.setResultCode(ResultCode.WAP_WEIXIN_GOTO_NOT_SILENT).setData(accessToken);
  	  		}
  	  	 }
     	retMap.put("code", 0);
		return retMap;
   		
     }
      
      
      
      
      
      
      /**
    	 * 第一步：用户同意授权，获取code
    	 * 在确保微信公众账号拥有授权作用域（scope参数）的权限的前提下（服务号获得高级接口后，默认拥有scope参数中的snsapi_base和snsapi_userinfo），引导关注者打开如下页面：
    	 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect 若提示“该链接无法访问”，请检查参数是否填写错误，是否拥有scope参数对应的授权作用域权限。 
    	 * 如果用户同意授权，页面将跳转至 redirect_uri/?code=CODE&state=STATE。
    	 * code说明 ： code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。 
    	 * 调试URL："https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe1169bab39d015c6&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
    	 * @param redirect_uri
    	 * @param state
    	 * @param isSilent 是否静默
    	 * @return
    	 */
        @RequestMapping("/wapAuthorizeByNoSilent")
        public String wapAuthorizeByNoSilent(HttpServletRequest request,
        		@RequestParam("userSharedRecordId") long userSharedRecordId,
        		@RequestParam("returnUrl") String returnUrl) throws UnsupportedEncodingException {
        	 String webBaseUrl = WebUtil.getWebBaseUrl(request);
        	 String redirect_uri = webBaseUrl + "/m/login/gotoBindPage.do";
        	 StringBuilder urlBuilder = new StringBuilder(redirect_uri);
        	 urlBuilder.append("?backUrl=").append(returnUrl);
        	 urlBuilder.append("&userSharedRecordId=").append(userSharedRecordId);
//        	 redirect_uri = URLEncoder.encode(URLEncoder.encode(redirect_uri,"UTF-8"));
        	 return ControllerUtil.redirect(weiXinV2API.getWapAuthorizeUrlByNoSilent(request, urlBuilder.toString()));
        } 
        
        /**
      	 * 跳转绑定页面
      	 *  授权完成后进行调整页面，如果没绑定手机则调整到绑定手机页面，如果已经绑定则跳回回调页面
      	 * @return
         * @throws UnsupportedEncodingException 
      	 */
          @RequestMapping("/gotoBindPage")
          public String gotoBindPage(HttpServletResponse response,HttpServletRequest request,
        		  @RequestParam(value="userSharedRecordId") long userSharedRecordId,
        		  @RequestParam(value="code") String code,
        		  @RequestParam("backUrl") String backUrl
        		  ,ClientPlatform clientPlatform, @ClientIp String ip
        		  ) throws UnsupportedEncodingException {
        	 if(StringUtils.isEmpty(code)){
    				logger.error("gotoBindPage cod is null code:"+code);
    				  return ControllerUtil.redirect(backUrl);
//    				return jsonResponse.setResultCode(ResultCode.WAP_GET_WEIXIN_INFO_ERROR);//获取微信用户信息失败
    		 }
//        	  JsonResponse jsonResponse = new JsonResponse();
        	  //1、通过code换取网页授权access_token
    	    IAccessToken accessToken =  weiXinV2API.getWapAccessToken(code);
           	if(accessToken == null){
           		logger.error("获取微信用户信息失败");
//           		return jsonResponse.setResultCode(ResultCode.WAP_GET_WEIXIN_INFO_ERROR);//获取微信用户信息失败
           		return ControllerUtil.redirect(backUrl);
           	}
           	String publicOpenId = accessToken.getOpenid();
    		String access_token = accessToken.getAccessToken();
    		//拉取用户信息
    		WapWeixinUser weixinUser = weiXinV2API.getWapWeixinUser(access_token,publicOpenId);
         	if(weixinUser == null ){
         		logger.error("获取微信用户信息失败");
//         		return jsonResponse.setResultCode(ResultCode.WAP_GET_WEIXIN_INFO_ERROR);
         		return ControllerUtil.redirect(backUrl);
         	}
         	String headimgurl = weixinUser.getHeadimgurl();
         	String nickname = weixinUser.getNickname();
         	 String openId = weixinUser.getOpenid();//公众号应用ID
    	    String weixinId = weixinUser.getUnionid();//微信用户唯一ID
    		//1、验证微信是否符合绑定条件
    	    User userByWeixin = userService.getUserByAllWay(weixinId);
    	   
    	    if(userByWeixin == null || !userByWeixin.checkBindPhone()){
    	    	 //绑定页面
    	    	String gotoUrl = WebUtil.getWebBaseUrl(request) + "/static/wap/bound1.html"
    	   	     		+ "?userSharedRecordId="+userSharedRecordId
    	   	     		+ "&weixinId="+weixinId+"&openId="+openId
    	   	    		+ "&nickname="+URLEncoder.encode(URLEncoder.encode(nickname,"UTF-8"))
    	   	    		+ "&headimgurl="+headimgurl
    	   	    		+ "&returnUrl="+URLEncoder.encode(URLEncoder.encode(backUrl,"UTF-8"));
    	   	  	return ControllerUtil.redirect(gotoUrl);
    	    }else{//绑定了手机则添加关联关系
    	    	userService.addBindUserRelation(weixinId,publicOpenId,userByWeixin.getUserId());
    	    	logger.error("添加关联关系userService.addBindUserRelation");
    	    	//登陆过后进行调整页面
    			userService.loginUser(response,userByWeixin,ip,clientPlatform);
    			return ControllerUtil.redirect(backUrl);
    	    }
          }
//    /**
//  	 * 第一步：用户同意授权，获取code
//  	 * 在确保微信公众账号拥有授权作用域（scope参数）的权限的前提下（服务号获得高级接口后，默认拥有scope参数中的snsapi_base和snsapi_userinfo），引导关注者打开如下页面：
//  	 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect 若提示“该链接无法访问”，请检查参数是否填写错误，是否拥有scope参数对应的授权作用域权限。 
//  	 * 如果用户同意授权，页面将跳转至 redirect_uri/?code=CODE&state=STATE。
//  	 * code说明 ： code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。 
//  	 * 调试URL："https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe1169bab39d015c6&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
//  	 * @param redirect_uri
//  	 * @param state
//  	 * @param isSilent 是否静默
//  	 * @return
//  	 */
//      @RequestMapping("/wapAuthorize")
//      public String wapAuthorize(HttpServletRequest request,@RequestParam("redirect_uri") String redirect_uri,@RequestParam("state") String state,@RequestParam("isSilent") String isSilent) throws UnsupportedEncodingException {
//    	  return  ControllerUtil.redirect(weiXinV2API.getWapAuthorizeUrl(request,redirect_uri,state,isSilent));
//      } 
      
//      /**
//       * 用于微信公众号用户登陆系统（静默获取页面授权，已经是本系统用户则登陆，不是则说明都不做）
//       * 
//       * 
//     	 * 第二步：通过code换取网页授权access_token
//     	 * 首先请注意，这里通过code换取的是一个特殊的网页授权access_token,与基础支持中的access_token（该access_token用于调用其他接口）不同。公众号可通过下述接口来获取网页授权access_token。如果网页授权的作用域为snsapi_base，则本步骤中获取到网页授权access_token的同时，也获取到了openid，snsapi_base式的网页授权流程即到此为止。
//     	 * 尤其注意：由于公众号的secret和获取到的access_token安全级别都非常高，必须只保存在服务器，不允许传给客户端。后续刷新access_token、通过access_token获取用户信息等步骤，也必须从服务器发起。
//     	 * 获取code后，请求以下链接获取access_token：  https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code 
//     	 *返回说明:
//     	 *正确时返回的JSON数据包如下：
//     	 *{ "access_token":"ACCESS_TOKEN",    //网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
//     	 * "expires_in":7200,    //access_token接口调用凭证超时时间，单位（秒）
////      	 *"refresh_token":"REFRESH_TOKEN",    //用户刷新access_token
//      	 *"openid":"OPENID",    //用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
//      	 *"scope":"SCOPE" }  //用户授权的作用域，使用逗号（,）分隔
//     	 *错误时微信会返回JSON数据包如下（示例为Code无效错误）:
//     	 *{"errcode":40029,"errmsg":"invalid code"} 
//     	 *
//     	 */
//     @RequestMapping("/wapAccessToken")
//     public JsonResponse wapAccessToken(@RequestParam("code") String code,
//     		HttpServletResponse response) {
//      		JsonResponse jsonResponse = new JsonResponse();
//      		 //1、通过code换取网页授权access_token
//  	    	IAccessToken accessToken =  weiXinV2API.getWapAccessToken(code);
//         	if(accessToken == null){
//         		logger.error("获取微信用户信息失败");
//         		return jsonResponse.setResultCode(ResultCode.WAP_GET_WEIXIN_INFO_ERROR);//获取微信用户信息失败
//         	}
//         	String publicOpenId = accessToken.getOpenid();
//         	 //2、拉取用户信息
////  		String access_token = accessToken.getAccessToken();
////  		WapWeixinUser wapWeixinUser = weiXinV2API.getWapWeixinUser(access_token,weixinId);
////         	if(wapWeixinUser == null ){
////         		return jsonResponse.setResultCode(ResultCode.WAP_GET_WEIXIN_INFO_ERROR);
////         	}
//         	  //2、获取应用中用户对象     User user = userService.getUserByAllWay(weixinId);
//            User user = userService.getUserByPublicOpenId(publicOpenId);
////            if(user != null ){//用户已存在,登陆用户
//          	  //是手机用户或已经绑定手机用户则进行登录
////          	  if(UserType.PHONE.getIntValue() == user.getUserType().getIntValue() ||  StringUtils.isNotEmpty(user.getBindPhone())){
////                	return user;
////          	  }
////            }
//      		if(user != null){
//      			userService.loginUser(response,user);
//      		}
//      		Map<String,Object> data =new HashMap<String,Object>();
////          data.put("accessToken", accessToken);
//      		data.put("userId", user.getUserId());
//      		return jsonResponse.setSuccessful().setData(data);
//      }		
    
//    public User getUserByCode(String code, HttpServletResponse response) {
//    	 //1、通过code换取网页授权access_token
//  	    IAccessToken accessToken =  weiXinV2API.getWapAccessToken(code);
//         	JsonResponse jsonResponse = new JsonResponse();
//         	if(accessToken == null){
////         		return jsonResponse.setResultCode(ResultCode.WAP_GET_WEIXIN_INFO_ERROR);//获取微信用户信息失败
//         		logger.error("获取微信用户信息失败");
//         	}
//         	String publicOpenId = accessToken.getOpenid();
//         	 //2、拉取用户信息
////  		String access_token = accessToken.getAccessToken();
////  		WapWeixinUser wapWeixinUser = weiXinV2API.getWapWeixinUser(access_token,weixinId);
////         	if(wapWeixinUser == null ){
////         		return jsonResponse.setResultCode(ResultCode.WAP_GET_WEIXIN_INFO_ERROR);
////         	}
//         	  //2、获取应用中用户对象     User user = userService.getUserByAllWay(weixinId);
//            User user = userService.getUserByPublicOpenId(publicOpenId);
//            if(user != null ){//用户已存在,登陆用户
//          	  //是手机用户或已经绑定手机用户则进行登录
//          	  if(UserType.PHONE.getIntValue() == user.getUserType().getIntValue() ||  StringUtils.isNotEmpty(user.getBindPhone())){
//                	return user;
//          	  }
//            }
//            return null;
//	}

	
    
    /**
     * 微信绑定手机发送短信验证码（暂时适用于wap）
     * @param code
     * @param phone
     * @param userDetail
     * @param response
     * var weixinBindPhoneSendMsgUrl = "/m/login/weixinBindPhoneSendMsg.json?phone="+phone
							+"&weixinId="+weixinId
							+"&openId="+openId
							+"&nickname="+nickname
							+"&returnUrl="+returnUrl;   
     * @return
     */
    @RequestMapping(value = "/weixinBindPhoneSendMsg")
    @ResponseBody
    public JsonResponse weixinBindPhoneSendMsg(@RequestParam("phone") String phone) {
 	   	JsonResponse jsonResponse = new JsonResponse();

        // TODO 将此处校验移到绑定操作之前进行，发送验证码不做次校验
         //2、获取应用中用户对象
         int ret = userService.weixinBindPhoneSendMsg(phone);
         if(ret == -1){//手机号已经绑定微信不能再次绑定微信
         	return jsonResponse.setResultCode(ResultCode.WAP_WEIXIN_BIND_PHONE_ERROR_PHONE_ERROE);
         }else if(ret == -2){
        	return jsonResponse.setResultCode(ResultCode.PHONE_SEND_FAIL);
         }
         /*
         Map<String, Object> data = new HashMap<String, Object>();
     	 data.put("phone", phone);
     	 data.put("weixinId",weixinId);
     	 data.put("openId",openId);
     	data.put("nickname",nickname);
     	data.put("headimgurl",headimgurl);
     	 data.put("returnUrl",returnUrl);
     	System.out.println("weixinBindPhoneSendMsg returnUrl:"+returnUrl);
         jsonResponse.setData(data);*/
         return jsonResponse.setSuccessful();
    }
   
    /**
     * 微信绑定手机号（暂时适用于wap）
     * @param phone
     * @param smsCode
     * @param userSharedRecordId
     * @param weixinId
     * @param openId
     * @param nickname
     * @param headimgurl
     * @param returnUrl
     * @param response
     * @return
     */
    @RequestMapping(value = "/weixinBindPhone")
    @ResponseBody
    public JsonResponse weixinBindPhone(@RequestParam("phone") String phone,
    										@RequestParam("smsCode") String smsCode,
    										@RequestParam("userSharedRecordId") long userSharedRecordId,
    										 @RequestParam("weixinId") String weixinId,
    										 @RequestParam("openId") String openId,
    										 @RequestParam("nickname") String nickname,
    										 @RequestParam("headimgurl") String headimgurl,
                                          @RequestParam("returnUrl") String returnUrl,HttpServletResponse response,
                                          @ClientIp String ip, ClientPlatform client) {
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	if (whitePhoneService.getWhitePhone(phone) == 0){//如果手机号不在白名单
	          //1、验证短信验证码
	          if(!yunXinSmsService.verifyCode(phone, smsCode)){
	      		return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
	          }
        }

		//1、验证微信是否符合绑定条件
	    User userByWeixin = userService.getUserByAllWay(weixinId);
		if(userByWeixin != null ){
			if(StringUtils.isNotEmpty(userByWeixin.getBindPhone())){//已经绑定手机
				return jsonResponse.setResultCode(ResultCode.WAP_WEIXIN_BIND_PHONE_ERROR_WEIXIN_ERROE);	
			}
			if(userByWeixin.getUserType().getIntValue() == UserType.PHONE.getIntValue()){//已经绑定手机
				return jsonResponse.setResultCode(ResultCode.WAP_WEIXIN_BIND_PHONE_ERROR_WEIXIN_ERROE);	
			}
		}
		nickname = EncodeUtil.decodeURL(nickname);
    	int ret = userService.weixinBindPhone(userSharedRecordId,phone, weixinId,openId,nickname,headimgurl,ip,client,response);
    	if(ret == -1){//微信已经绑定此手机号
        	return jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_ALREADY_BINDED_LOGIN);
        }else if(ret == -2){//微信已经被他人绑定,请用手机号登录
        	return jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_BINDED_LOGIN);
        }else if(ret == -3){//手机已被注册或绑定，请使用其他手机号
        	return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
        }
    	
    	 //绑定手机成功之后进行登陆
       	//User loginUser = userService.getUserByAllWay(weixinId);
       	//loginDelegator.loginUser(response,loginUser);
    	 Map<String, Object> data = new HashMap<String, Object>();
     	 data.put("phone", phone);
     	 data.put("returnUrl",returnUrl);
         jsonResponse.setData(data);
        return jsonResponse.setSuccessful();
    }
    
    
    
    
    
    

    @RequestMapping("/form")
    public String loginForm(@RequestParam("target_url") String targetUrl, Map<String, Object> model) {
        targetUrl = securityService.getSafeRedirectUrl(targetUrl);
        model.put("targetUrl", targetUrl);

        return "wap/login/form.ftl";
    }

    @RequestMapping(value = "/submit")
    @ResponseBody
    public JsonResponse submitLogin(@RequestParam("username") String username,
                                    @RequestParam("password") String password, HttpServletResponse response
                                    ,@ClientIp String ip,ClientPlatform client) {
        return loginDelegator.submitLogin(username, password, response,ip,client);
    }
    
   

    @RequestMapping(value = "/ext/oauth")
    public String externalOAuthLogin(@RequestAttribute(WapExternalOAuthController.ATTR_NAME_TOKEN_CREDENTIALS) IRawDataV2TokenCredentials tokenCredentials,
                                     @RequestAttribute(WapExternalOAuthController.ATTR_NAME_TARGET_URL) String targetUrl,
                                     @RequestParam("vendor_type") String vendorType, @ClientIp String ip,
                                     Map<String, Object> model, HttpServletResponse response, ClientPlatform clientPlatform) {
        ISnsResponse<ISnsEndUser> snsResponse = weiXinV2API.getEndUser(tokenCredentials, ip);
        if (snsResponse.getResponseType() != SnsResponseType.SUCCESS) {
            logger.error("fail to get user, snsResponse:{}", snsResponse);
            return Constants.ERROR_MAINTENANCE;
        }
        ISnsEndUser endUser = snsResponse.getData();

        UserType userType = UserType.WEIXIN;
//        User user = userService.getUserByRelatedName(, userType);
        User user = userService.getUserByAllWay(endUser.getPlatformIndependentId());
        String nickName = escapeNickName(endUser.getNickName());
        if (user == null) {
            long time = System.currentTimeMillis();
            user = new User();
            user.setUserName(endUser.getPlatformIndependentId());
            user.setUserRelatedName(endUser.getPlatformIndependentId());
            user.setUserType(userType);
            user.setUserNickname(nickName);
            user.setUserIcon(endUser.getAvatar());
            user.setUserPassword(DigestUtils.md5Hex("nopassword"));
            user.setStatus(UserStatus.NORMAL);
            user.setCreateTime(time);
            user.setUpdateTime(time);
            user.setRegistrationSource(UserRegSource.WEB.getIntValue());

            registerFacade.addUser(user, null, clientPlatform);
        } else {
            boolean needUpdate = false;
            String userNickName = user.getUserNickname();
            if (!StringUtils.equals(userNickName, nickName)) {
                needUpdate = true;
                userNickName = nickName;
            }
            /* 头像不需要同步
            String userIcon = user.getUserIcon();
            if (!StringUtils.equals(userIcon, endUser.getAvatar())) {
                needUpdate = true;
                userIcon = endUser.getAvatar();
            }
            */

            if (needUpdate) {
                userService.updateUserNickName(user.getUserId(), userNickName);
            }
        }
        //添加用户信息到Header中
        userService.addSetCookie(response, user);
//        String cookieValue = LoginUtil.buildLoginCookieValue(user.getUserRelatedName(), user.getUserType());
//        response.addHeader("Set-Cookie", LoginUtil.buildLoginCookieHeaderValue(cookieValue));

        targetUrl = securityService.getSafeRedirectUrl(targetUrl);
        return ControllerUtil.redirect(targetUrl);
    }

    private String escapeNickName(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (!Character.isHighSurrogate(ch) && !Character.isLowSurrogate(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    @RequestMapping("/context")
    @ResponseBody
    @Login
    public JsonResponse getContext(UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();

        User user = userDetail.getUser();
        data.put("user", user);

        return jsonResponse.setSuccessful().setData(data);
    }
}

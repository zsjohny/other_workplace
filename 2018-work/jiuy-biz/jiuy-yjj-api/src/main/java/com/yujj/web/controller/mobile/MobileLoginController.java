package com.yujj.web.controller.mobile;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.account.UserRegSource;
import com.jiuyuan.constant.account.UserStatus;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.account.UserCoin;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.service.SecurityService;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.service.common.YunXinSmsService;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.oauth.common.credential.IRawDataCredentials;
import com.jiuyuan.util.oauth.common.credential.RawDataCredentials;
import com.jiuyuan.util.oauth.sns.common.response.ISnsResponse;
import com.jiuyuan.util.oauth.sns.common.response.SnsResponseType;
import com.jiuyuan.util.oauth.sns.common.user.ISnsEndUser;
import com.jiuyuan.util.oauth.sns.weixin.WeiXinV2API;
import com.jiuyuan.util.oauth.v2.V2Constants;
import com.jiuyuan.util.spring.ControllerUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.RegisterFacade;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.UserCoinService;
import com.yujj.business.service.UserService;
import com.yujj.entity.account.User;
import com.yujj.entity.account.UserDetail;
import com.yujj.util.uri.UriBuilder;
import com.yujj.web.controller.delegate.LoginDelegator;

@Controller
@RequestMapping("/mobile/login")
public class MobileLoginController {
    
    private static final Logger logger = LoggerFactory.getLogger(MobileLoginController.class);
    private static final Logger loggerAttack = LoggerFactory.getLogger("ATTACK");
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private MemcachedService memcachedService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private RegisterFacade registerFacade;

    @Autowired
    private LoginDelegator loginDelegator;

    @Autowired
    private GlobalSettingService globalSettingService;
    
    @Autowired
    private YunXinSmsService yunXinSmsService;
    
    @Autowired
    @Qualifier("weiXinV2API4App")
    private WeiXinV2API weiXinV2API;
    
    @RequestMapping(value = "/startad", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse startAd() {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	
    	String startAdPageJson = globalSettingService.getSetting(GlobalSettingName.START_PAGE_AD);
        Object adPageVO = JSON.parse(startAdPageJson);
        
        long updateTime = globalSettingService.getUpdateTime(GlobalSettingName.START_PAGE_AD);
        
        data.put("adPageVO", adPageVO);
        data.put("updateTime", updateTime);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    /**
     * 用户登录接口
     * @RequestMapping(value = "/submit", method = RequestMethod.POST)
     */
    @RequestMapping(value = "/submit")
    @ResponseBody
    public JsonResponse submitLogin(@RequestParam("username") String username,
                                    @RequestParam("password") String password, HttpServletResponse response ,@ClientIp String ip,ClientPlatform client) {
        return loginDelegator.submitLogin(username, password, response ,ip , client);
    }

    @RequestMapping(value = "/ext/oauth", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse externalOAuthLogin(@RequestParam("openid") String openId,
                                           @RequestParam("openuid") String openuid,
                                           @RequestParam("access_token") String accessToken,
                                           @RequestParam("user_type") UserType userType, @ClientIp String ip,ClientPlatform client,
                                           HttpServletResponse response) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        jsonResponse.setData(data);

//        if (userType != UserType.WEIXIN) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(V2Constants.ACCESS_TOKEN, accessToken);
        map.put("openid", openId);
        IRawDataCredentials tokenCredentials = new RawDataCredentials(map);

        ISnsResponse<ISnsEndUser> snsResponse = weiXinV2API.getEndUser(tokenCredentials, ip);
        if (snsResponse.getResponseType() != SnsResponseType.SUCCESS) {
            logger.error("fail to get user, snsResponse:{}", snsResponse);
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        }

        ISnsEndUser endUser = snsResponse.getData();
        if (!StringUtils.equals(openuid, endUser.getPlatformIndependentId())) {
            logger.error("openuid do not equals, submit:{}, get from weixin:{}", openuid,
                endUser.getPlatformIndependentId());
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        }
//        User user = userService.getUserByRelatedName(endUser.getPlatformIndependentId(), userType);
        User user = userService.getUserByAllWay(endUser.getPlatformIndependentId());

        	if (user == null) {//如果数据库中没有该用户则添加该用户
        		long time = System.currentTimeMillis();
        		user = new User();
        		user.setUserName(endUser.getPlatformIndependentId());
//        		user.setUserRelatedName(client.getPlatform().getValue());
        		user.setUserRelatedName(endUser.getPlatformIndependentId());
        		user.setUserType(userType);
        		user.setUserNickname(endUser.getNickName());
        		user.setUserIcon(endUser.getAvatar());
        		user.setUserPassword(DigestUtils.md5Hex("nopassword"));
        		user.setStatus(UserStatus.NORMAL);
        		user.setCreateTime(time);
        		user.setUpdateTime(time);
        		if(client != null && client.getPlatform() != null && client.getPlatform().isIOS()){
                	user.setRegistrationSource(UserRegSource.IPHONE.getIntValue());
                }else if(client != null && client.getPlatform() != null && client.getPlatform().isAndroid()){
                	user.setRegistrationSource(UserRegSource.ANDROID.getIntValue());
                }else if(client != null && client.getPlatform() != null && client.getPlatform().is("web")){
                	user.setRegistrationSource(UserRegSource.WEB.getIntValue());
                }else{
                	user.setRegistrationSource(UserRegSource.OTHER.getIntValue());
                }
        		
        		registerFacade.addUser(user, null, client);
        		
        		UserCoin coins = userCoinService.getUserCoin(user.getUserId());
        		data.put("gainCoins", coins == null ? 0 : coins.getUnavalCoins());

        	} else {//如果用户存在则判断是否需要绑定手机号

        		String userNickName = user.getUserNickname();
        		long userId = user.getUserId();
        		userService.synNickName(userId,userNickName, endUser.getNickName());

        		if (client.isAndroid()) {
        			if (user.getBindPhone() == null || user.getBindPhone().length() != 11) {
        				data.put("needBindPhone", "YES");
        			}else{
        				data.put("needBindPhone", "NO");	
        			}
        		}
        		else {
        			data.put("needBindPhone", "NO");
        		}
        	}

//        	//添加用户信息到Header中
//        	loginDelegator.addSetCookie(response, user);
//        	//记录用户登录日志
//        	loginDelegator.addUserLoginLog(ip, client, user);
        	userService.loginUser(response, user, ip,client);
        return jsonResponse.setSuccessful();
    }
    
    //8月16号
    @RequestMapping(value = "/ext/oauth186", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse externalOAuthLogin186(@RequestParam("openid") String openId,
    		@RequestParam("openuid") String openuid,
    		@RequestParam("access_token") String accessToken,
    		@RequestParam("user_type") UserType userType, @ClientIp String ip,ClientPlatform client,
    		HttpServletResponse response) {
    	//1、获取用户信息
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	jsonResponse.setData(data);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put(V2Constants.ACCESS_TOKEN, accessToken);
    	map.put("openid", openId);
    	IRawDataCredentials tokenCredentials = new RawDataCredentials(map);
    	ISnsResponse<ISnsEndUser> snsResponse = weiXinV2API.getEndUser(tokenCredentials, ip);
    	if (snsResponse.getResponseType() != SnsResponseType.SUCCESS) {
    		logger.error("fail to get user, snsResponse:{}", snsResponse);
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
    	}

    	ISnsEndUser endUser = snsResponse.getData();
    	if (!StringUtils.equals(openuid, endUser.getPlatformIndependentId())) {
    		logger.error("openuid do not equals, submit:{}, get from weixin:{}", openuid,endUser.getPlatformIndependentId());
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
    	}
//    	User user = userService.getUserByBindWeixinId(endUser.getPlatformIndependentId());
//    	if(user != null ){
//    		return jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_BINDED_LOGIN);
//    	}

//    	user = userService.getUserByRelatedName(endUser.getPlatformIndependentId(), userType);
//    	User user = userService.getUserByBindWeixinId(endUser.getPlatformIndependentId());
//    	if(user != null ){
//    		return jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_BINDED_LOGIN);
//    	}
//    	User user = userService.getUserByRelatedName(endUser.getPlatformIndependentId(), userType);
    	//2.获取应用用户信息
    	User user = userService.getUserByAllWay(endUser.getPlatformIndependentId());
    	String weiXinNickName = endUser.getNickName();

    	if (user != null) {

    		String userNickName = user.getUserNickname();
    		long userId = user.getUserId();
    		//同步昵称

    		userService.synNickName(userId,userNickName, weiXinNickName);


//    		//添加用户信息到Header中
//        	loginDelegator.addSetCookie(response, user);
//        	//记录用户登录日志
//        	loginDelegator.addUserLoginLog(ip, client, user);
    		userService.loginUser(response, user, ip,client);

    		if (!user.getUserType().equals(UserType.PHONE) && (user.getBindPhone() == null || user.getBindPhone().length() != 11)) {
    			data.put("needBindPhone", "YES");
    		}else{
    			data.put("needBindPhone", "NO");	
    		}
    		data.put("needRegFlag", "NO");
    	} else {
    		//return jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR_ALREADY_BINDED);
    		data.put("needRegFlag", "YES");
    	}
    	
    	return jsonResponse.setSuccessful();
    }


    /**
     * 同步昵称
     * @param user
     * @param nickName
     */
	private void synNickName(long userId,String userNickName, String weiXinNickName) {
		//同步昵称
		boolean needUpdate = false;
		if (!StringUtils.equals(userNickName, weiXinNickName)) {
			needUpdate = true;
			userNickName = weiXinNickName;
		}
		if (needUpdate) {
			userService.updateUserNickName(userId, userNickName);
		}
		/* 头像不需要同步
		String userIcon = user.getUserIcon();
		if (!StringUtils.equals(userIcon, endUser.getAvatar())) {
		    needUpdate = true;
		    userIcon = endUser.getAvatar();
		}
		 */
	}


    
    //手机用户 绑定微信时发送短信
    @RequestMapping(value = "/ext/weixinBind/sendMsg", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse extWeixinSendMsg(@RequestParam("openid") String openId,
    		@RequestParam("openuid") String openuid,
    		@RequestParam("access_token") String accessToken,
    		@RequestParam("user_type") UserType userType, @ClientIp String ip, UserDetail userDetail,
    		HttpServletResponse response) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	jsonResponse.setData(data);
    	
//        if (userType != UserType.WEIXIN) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put(V2Constants.ACCESS_TOKEN, accessToken);
    	map.put("openid", openId);
    	IRawDataCredentials tokenCredentials = new RawDataCredentials(map);
    	
    	ISnsResponse<ISnsEndUser> snsResponse = weiXinV2API.getEndUser(tokenCredentials, ip);
    	if (snsResponse.getResponseType() != SnsResponseType.SUCCESS) {
    		logger.error("fail to get user, snsResponse:{}", snsResponse);
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
    	}
    	
    	ISnsEndUser endUser = snsResponse.getData();
    	if (!StringUtils.equals(openuid, endUser.getPlatformIndependentId())) {
    		logger.error("openuid do not equals, submit:{}, get from weixin:{}", openuid,
    				endUser.getPlatformIndependentId());
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
    	}
    	
    	User user = userService.getUserByAllWay(endUser.getPlatformIndependentId());
    	String sendPhone = "";
    	
    	//绑定微信号
    	if(userDetail != null && userDetail.getUser() != null){
    		data.put("weiXinId", endUser.getPlatformIndependentId());
    		if (user != null) {
    			data.put("bindFailMsg", "微信绑定失败，您使用的微信账号可能已经被其他俞姐姐账号绑定，请尝试使用其他微信账号。");
    			return jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_ERROR);
    		} else {
    			if(userDetail.getUser().getUserType() == UserType.EMAIL){
    				if(userDetail.getUser().getBindPhone() == null || userDetail.getUser().getBindPhone().length() != 11){
    					return jsonResponse.setResultCode(ResultCode.WEIXIN_BIND_NOPHONE_ERROR);
    				} else { 
    					sendPhone = userDetail.getUser().getBindPhone();
    				}
    				
    			}else if(!userDetail.getUser().getUserType().equals(UserType.PHONE) && userDetail.getUser().getUserName().length() == 11){
    				sendPhone = userDetail.getUser().getUserName();
    			}

    			if(sendPhone != null && sendPhone.length() == 11){
    				data.put("sendPhone", sendPhone);
    				boolean result = yunXinSmsService.sendCode(sendPhone,1);
    				if(result){
    					data.put("sendResult", "SUCCESS");
    					data.put("sendResultMsg", "短信发送成功！");
    				}else{
    					data.put("sendResult", "FAIL");
    					data.put("sendResultMsg", "短信发送失败！");
    					return jsonResponse.setResultCode(ResultCode.PHONE_SEND_FAIL);
    				}
    			}else{
    				data.put("sendResult", "ERROR_NUMBER");
    				data.put("sendResult", "用户手机号码有误！");
    				return jsonResponse.setResultCode(ResultCode.PHONE_ERROR_NUMBER);
    			}
    		}
    	} else{
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
    	}
    	
    	return jsonResponse.setSuccessful();
    }
    
    //微信新用户注册  绑定手机号发送验证码
    @RequestMapping(value = "/weixinReg/sendVerifyCode", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse weixinRegSendPhoneVerifyCode(@RequestParam("phone") String phone) {
    	return userService.weixinRegSendCode(phone);
    }
    
    
    @RequestMapping(value = "/phone/send_verify_code", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse verifyCodeLoginSendCode(@RequestParam("phone") String phone,
            ClientPlatform clientPlatform, @ClientIp String ip) {

    	JsonResponse jsonResponse = new JsonResponse();
    	if (clientPlatform.isAndroid() || clientPlatform.isIphone())
    		return loginDelegator.verifyCodeLoginSendCode(phone);
    	else {
        	loggerAttack.info("MobileRegisterController sendPhoneSimpleVerifyCode clientPlatform:{}, ip:{}, phone:{}", clientPlatform.getPlatform(), ip, phone);
    	}
    	return jsonResponse;
    }
    
    
    @RequestMapping(value = "/phone/verify_code_commit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse verifyCodeLoginCommit(@RequestParam("phone") String phone, @RequestParam("verify_code") String verifyCode, HttpServletResponse response,
    		ClientPlatform clientPlatform, @ClientIp String ip) {
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	if (clientPlatform.isAndroid() || clientPlatform.isIphone())
    		return loginDelegator.verifyCodeLoginCommit(phone, verifyCode, clientPlatform, ip, response);
    	else {
    		loggerAttack.info("MobileRegisterController sendPhoneSimpleVerifyCode clientPlatform:{}, ip:{}, phone:{}", clientPlatform.getPlatform(), ip, phone);
    	}
    	return jsonResponse;
    }
    
    
    
    //新微信用户注册提交（附加手机绑定）
    @RequestMapping(value = "/weiXinRegPhoneBind/commit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse weiXinRegphoneNumberCommit(@RequestParam("phone") String phone,
                                          @RequestParam("verify_code") String verifyCode,
                                          @RequestParam("openid") String openId,
                                  		@RequestParam("openuid") String openuid,
                                  		@RequestParam("access_token") String accessToken,
                                  		HttpServletResponse response,
                                  		@RequestParam("user_type") UserType userType, @ClientIp String ip, ClientPlatform client) {
        return userService.weixinRegPhoneNumberCommit(phone, verifyCode, openId, openuid, accessToken, userType , ip ,response ,client );
    }

    @RequestMapping(value = "/token", method = RequestMethod.GET)
    public String tokenLogin(@RequestParam("token") String token, @RequestParam("target_url") String targetUrl
    		,ClientPlatform clientPlatform,@ClientIp String ip
    		,HttpServletResponse response) {
        targetUrl = securityService.getSafeRedirectUrl(targetUrl);

        Object obj = memcachedService.get(MemcachedKey.GROUP_KEY_TOKEN_LOGIN, token);
        boolean valid = obj != null;

        User user = null;
        if (valid) {
            user = userService.getUser((Long) obj);
            if (user == null) {
                valid = false;
            }
            memcachedService.remove(MemcachedKey.GROUP_KEY_TOKEN_LOGIN, token);
        }

        if (!valid) {
            UriBuilder builder = new UriBuilder(Constants.SERVER_URL_HTTPS + "/login.do");
            builder.add("target_url", targetUrl);
            return ControllerUtil.redirect(builder.toUri());
        }
//      //添加用户信息到Header中
//    	loginDelegator.addSetCookie(response, user);
			userService.loginUser(response,user,ip,clientPlatform);
//        String cookieValue = LoginUtil.buildLoginCookieValue(user.getUserRelatedName(), user.getUserType());
//        response.addHeader("Set-Cookie", LoginUtil.buildLoginCookieHeaderValue(cookieValue));
//        logger.debug("cookie :{}", LoginUtil.buildLoginCookieHeaderValue(cookieValue));
        return ControllerUtil.redirect(targetUrl);
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

	//设置俞姐姐号
//	List<YJJNumber> yjjNumberList = null;
//	// getMemCache
//	String groupKey = MemcachedKey.GROUP_KEY_YJJNUMBER;
//	String key = "yjjnumber";
//	Object obj = memcachedService.get(groupKey, key);
//	if (obj != null && ((List<YJJNumber>)obj).size()>0) {
//		yjjNumberList = (List<YJJNumber>)obj;
//	}
//	else {
//		//分段去取10组1000个未使用俞姐姐号放入内存使用
//		yjjNumberList = userService.getYJJNumberList(1, 100000, 1000 , 0);
//		List<YJJNumber> yjjNumberList2 = userService.getYJJNumberList(100000, 200000, 1000 , 0);
//		List<YJJNumber> yjjNumberList3 = userService.getYJJNumberList(200000, 300000, 1000 , 0);
//		List<YJJNumber> yjjNumberList4 = userService.getYJJNumberList(300000, 400000, 1000 , 0);
//		List<YJJNumber> yjjNumberList5 = userService.getYJJNumberList(400000, 500000, 1000 , 0);
//		List<YJJNumber> yjjNumberList6 = userService.getYJJNumberList(500000, 600000, 1000 , 0);
//		List<YJJNumber> yjjNumberList7 = userService.getYJJNumberList(600000, 700000, 1000 , 0);
//		List<YJJNumber> yjjNumberList8 = userService.getYJJNumberList(700000, 800000, 1000 , 0);
//		List<YJJNumber> yjjNumberList9 = userService.getYJJNumberList(800000, 900000, 1000 , 0);
//		List<YJJNumber> yjjNumberList10 = userService.getYJJNumberList(900000, 1000000, 1000 , 0);
//		
//		if(yjjNumberList2 != null && yjjNumberList2 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList2);
//		}
//		if(yjjNumberList3 != null && yjjNumberList3 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList3);
//		}
//		if(yjjNumberList4 != null && yjjNumberList4 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList4);
//		}
//		if(yjjNumberList5 != null && yjjNumberList5 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList5);
//		}
//		if(yjjNumberList6 != null && yjjNumberList6 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList6);
//		}
//		if(yjjNumberList7 != null && yjjNumberList7 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList7);
//		}
//		if(yjjNumberList8 != null && yjjNumberList8 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList8);
//		}
//		if(yjjNumberList9 != null && yjjNumberList9 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList9);
//		}
//		if(yjjNumberList10 != null && yjjNumberList10 .size() > 0){
//			yjjNumberList.addAll(yjjNumberList10);
//		}
//		memcachedService.set(groupKey, key, DateConstants.SECONDS_FOREVER, yjjNumberList);
//	}
//	
//	
//	
//	int randomNum = (int) (Math.random() * yjjNumberList.size()) ;
//	
//	YJJNumber number = yjjNumberList.get(randomNum);
//	int k = 0;
//    while(number.getNumber() <= 0 && k++ < 5){
//    	yjjNumberList.remove(randomNum);
//    	randomNum = (int) (Math.random() * yjjNumberList.size()) ;
//    	number = yjjNumberList.get(randomNum);
//    }
//	
//	user.setyJJNumber(number.getNumber());
    
    
//	yjjNumberList.remove(randomNum);
//	userService.updateYjjNumberUsed(number.getNumber());
//	
//	// setMemCache
//	memcachedService.set(groupKey, key, DateConstants.SECONDS_FOREVER, yjjNumberList);
}

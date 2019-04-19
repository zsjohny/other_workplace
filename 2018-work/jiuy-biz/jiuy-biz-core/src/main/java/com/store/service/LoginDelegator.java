package com.store.service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.account.UserLoginLog;
import com.jiuyuan.util.LoginUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.service.UserService;
import com.yujj.entity.StoreBusiness;
import com.yujj.entity.account.User;
/**
 * UserService需要换为门店service
 * @author Administrator
 *
 */
@Service
public class LoginDelegator {
    private static final Logger logger = LoggerFactory.getLogger(LoginDelegator.class);

//    @Autowired UserService需要换为门店service
    private UserService userService = null;
//    @Autowired
//    private UcpaasService ucpaasService;

    /**
     * 手机登陆，判断用户名和密码是否为空
     * @param username
     * @param password
     * @param response
     * @param ip
     * @param client
     * @return
     */
    public JsonResponse mobileSubmitLogin(String username, String password, HttpServletResponse response, String ip, ClientPlatform client) {
    	JsonResponse jsonResponse = new JsonResponse();
    	if(checkNull(username)){
    		return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_USERNAME_NULL);
    	}
    	if(checkNull(password)){
    		return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_PASSWORD_NULL);
    	}
    	return submitLogin(username,password,response,ip,client);
    }
    
    /**
     * 云之讯发送短信/语音验证码
     * @param username
     * @param password
     * @param response
     * @param ip
     * @param client
     * @return
     */
    public JsonResponse ucpaasSendCode(String phone, String sendType) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	Boolean  sendFlag = true;
    //	Boolean  sendFlag = ucpaasService.send(phone, sendType);
    	data.put("sendFlag", sendFlag);
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    /**
     * 登录验证
     * @param username
     * @param password
     * @param response
     * @param ip
     * @param client
     * @return
     */
    public JsonResponse submitLogin(String username, String password, HttpServletResponse response, String ip, ClientPlatform client) {
    	JsonResponse jsonResponse = new JsonResponse();
    	String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,2,5-9])|(177))\\d{8}$";
        Pattern pattern = Pattern.compile(regex);
        boolean flag = pattern.matcher(username).matches();//判断用户是否用已注册手机号登陆
        
        StoreBusiness storeBusiness = null;
        if(flag){
        	storeBusiness = userService.getStoreBusinessByPhone(username);
        }else{
        	storeBusiness = userService.getStoreBusiness4Login(username);
        }

//        User user = userService.getUser4Login(username);
//        if (user == null) {
//            return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_USER_NOT_EXISTS);
//        } else if (!StringUtils.equals(user.getUserPassword(), DigestUtils.md5Hex(password))) {
//            return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_INVALID_PASSWORD);
//        } 
        
        if (storeBusiness == null) {
        	return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_USER_NOT_EXISTS);
        } else if (!StringUtils.equals(storeBusiness.getUserPassword(), DigestUtils.md5Hex(password))) {
        	return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_INVALID_PASSWORD);
        }
        
        Map<String, Object> data = new HashMap<String, Object>();
        if(storeBusiness.getActiveTime() == 0){
        	data.put("activeFlag", "0");
        	
        }else {
        	data.put("activeFlag", "1");	
        }
        if(storeBusiness.getProtocolTime() == 0){
        	data.put("protocolFlag", "0");
        	
        }else {
        	data.put("protocolFlag", "1");	
        }
        
        String cookieValue = LoginUtil.buildLoginCookieValue(storeBusiness.getUserName(), UserType.PHONE);
        response.addHeader("Set-Cookie", LoginUtil.buildLoginCookieHeaderValue(cookieValue));
        logger.debug("cookie :{}", LoginUtil.buildLoginCookieHeaderValue(cookieValue));
      //记录用户登录日志
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setBusinessId(storeBusiness.getId());
        if(client != null){
        	userLoginLog.setClientType(client.getPlatform().getValue());
        	userLoginLog.setClientVersion(client.getVersion());	
        }
        userLoginLog.setIp(ip);
        userLoginLog.setCreateTime(System.currentTimeMillis());
        userService.addUserLoginLog(userLoginLog);
        return jsonResponse.setSuccessful().setData(data);
    }
    public JsonResponse submitLogin(String username, String password, HttpServletResponse response) {
    	
    	return submitLogin(username,  password,  response, "", null);
    }
    
    /**
     * 判断是否为空或者空字符串
     * @param str
     * @return
     */
    public boolean checkNull(String str){
    	if(str==null||"".equals(str)){
    		return true;
    	}
    	return false;
    }
}

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
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.WhitePhoneService;
import com.jiuyuan.service.common.UcpaasService;
import com.jiuyuan.service.common.YunXinSmsService;
import com.jiuyuan.util.LoginUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.store.enumerate.StoreAuditStatusEnum;
import com.yujj.business.service.UserService;

import com.yujj.entity.account.User;

@Service
public class MemberLoginDelegator {
    private static final Logger logger = LoggerFactory.getLogger(MemberLoginDelegator.class);

    @Autowired
    private StoreUserService userService;
    
    @Autowired
    private UcpaasService ucpaasService;
    
    @Autowired
    private StoreUserService storeUserService;
    
    @Autowired
    private StoreAuditServiceShop storeAuditService;
    
    
    @Autowired
    private WhitePhoneService whitePhoneService;
    
	@Autowired
	private YunXinSmsService yunXinSmsService;

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
     * 手机号码验证码登陆
     * @param username
     * @param password
     * @param response
     * @param ip
     * @param client
     * @return
     */
    public JsonResponse verifyCommit(String phone, String verifyCode, HttpServletResponse response, String ip, ClientPlatform client) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	if (whitePhoneService.getWhitePhone(phone) == 0){//如果手机号不在白名单
	        if (verifyCode == null || !yunXinSmsService.verifyCode(phone, verifyCode)) {
	        	return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
	        } 
    	}
        StoreBusiness storeBusiness = userService.getStoreBusinessByPhone(phone);
        
        String nextStep = "index";
        if (storeBusiness == null) {
        	//long storeId = storeUserService.addPhoneUser(phone);
        	data.put("needBindWeixin", "YES");
        	data.put("needFillData", "YES");
        	data.put("waitAudit", "YES");
        	//创建手机用户？
        } else{
        	
//        	int auditFailNum = storeAuditService.getAuditCount(storeBusiness.getId(), StoreAuditStatusEnum.fail.getIntValue());
        	if(storeBusiness.getBindWeixinId().trim().length() > 0){
        		data.put("needBindWeixin", "NO");
        		int auditPassNum = storeAuditService.getAuditCount(storeBusiness.getId(), StoreAuditStatusEnum.pass.getIntValue());
        		if(auditPassNum > 0){
        			data.put("waitAudit", "NO");
        		}else{
//        			int auditWaitNum = storeAuditService.getAuditCount(storeBusiness.getId(), StoreAuditStatusEnum.submit.getIntValue());
//        			if(auditWaitNum > 0){
//        				data.put("waitAudit", "YES");
//        			}else{
//        				data.put("needFillData", "YES");
//        			}
        			if(storeBusiness.getLegalPerson() != null && storeBusiness.getLegalPerson().trim().length() > 0){
        				data.put("needFillData", "NO");
        			}else{
        				data.put("needFillData", "YES");
        			}
        		}
        	}else{
        		data.put("needBindWeixin", "YES");
        		data.put("needFillData", "YES");
        		data.put("waitAudit", "YES");
        	}
        	loginUser(storeBusiness, response, ip, client);
        }
       
       
        if(data.get("needBindWeixin") != null && data.get("needBindWeixin").equals("YES")){
    		nextStep = "bindWeixin";
    	}else if(data.get("needFillData") != null && data.get("needFillData").equals("YES")){
    		nextStep = "fillData";
    	}
        data.put("nextStep", nextStep);
        
       // loginUser(storeBusiness, response, ip, client);
        return jsonResponse.setSuccessful().setData(data);
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
//    	Boolean  sendFlag = true;
    	Boolean  sendFlag = ucpaasService.send(phone, sendType, 2);
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
        
        Map<String, Object> data = loginUser(storeBusiness, response, ip, client);
        return jsonResponse.setSuccessful().setData(data);
    }
    
    public Map<String, Object> loginUser(StoreBusiness storeBusiness, HttpServletResponse response, String ip, ClientPlatform client) {
    	
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
         
         String cookieValue = LoginUtil.buildLoginCookieValue(storeBusiness.getBusinessNumber() + "", UserType.PHONE);
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
         return data;
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

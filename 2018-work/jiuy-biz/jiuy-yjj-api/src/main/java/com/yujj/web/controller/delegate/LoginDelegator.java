package com.yujj.web.controller.delegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.account.CodeUseage;
import com.jiuyuan.constant.account.UserRegSource;
import com.jiuyuan.constant.account.UserStatus;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.util.ClientUtil;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.account.UserCoin;
import com.jiuyuan.entity.account.UserInviteRecord;
import com.jiuyuan.entity.account.UserLoginLog;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.service.common.YunXinSmsService;
import com.yujj.business.facade.RegisterFacade;
import com.yujj.business.handler.account.GrantCoinHandler;
import com.yujj.business.handler.account.UserHandler;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.OrderCouponService;
import com.yujj.business.service.UserCoinService;
import com.yujj.business.service.UserInviteService;
import com.yujj.business.service.UserMemberService;
import com.yujj.business.service.UserService;
import com.yujj.dao.mapper.UserInviteMapper;
import com.yujj.dao.mapper.UserMapper;
import com.yujj.entity.account.User;
import com.yujj.exception.ParameterErrorException;
import com.jiuyuan.ext.spring.web.method.ClientIp;
/**
 * 登录代理服务类
 * @author Administrator
 *
 */
@Service
public class LoginDelegator {
    private static final Logger logger = LoggerFactory.getLogger(LoginDelegator.class);
    String groupKey = MemcachedKey.GROUP_KEY_LOGIN_PASSWORD_ERROR_LOG;
    int exceedConfineTime = DateConstants.SECONDS_PER_HOUR * 3;

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserInviteService userInviteService;
    
    @Autowired
    private YunXinSmsService yunXinSmsService;
    
    @Autowired
    private RegisterFacade registerFacade;
    
    @Autowired
    private UserCoinService userCoinService;
    
    @Autowired
    private GlobalSettingService globalSettingService;
    
    
    @Autowired
    private OrderCouponService orderCouponService;
    
    @Autowired
    private UserMapper userMapper;

    @Autowired(required = false)
    private List<UserHandler> userHandlers;
    
    @Autowired
    private UserInviteMapper userInviteMapper;
    
    @Autowired
    private MemcachedService memcachedService;
    
    @Autowired
    private UserMemberService userMemberService;
    
//    /**
//	 * 登陆用户（后期多处登陆修改为统一调用该方法）
//	 * 操作1：添加用户新到Headr中
//	 * 操作2：记录用户登陆日志
//	 * @param response
//	 * @param user
//	 */
//	public void loginUser(HttpServletResponse response, User user) {
//		loginUser(response,user,null,null) ;
//	}
//    
//    /**
//	 * 登陆用户（后期多处登陆修改为统一调用该方法）
//	 * 操作1：添加用户新到Headr中
//	 * 操作2：记录用户登陆日志
//	 * @param response
//	 * @param user
//	 */
//	public void loginUser(HttpServletResponse response, User user,String ip, ClientPlatform client) {
//		userService.loginUser(response, user, ip,client);
//	}
    
    /**
     * 用户登陆
     * @param username	账号
     * @param password	密码
     * @param response
     * @param ip IP
     * @param client
     * @return
     */
    public JsonResponse submitLogin(String username, String password, HttpServletResponse response, String ip, ClientPlatform client) {
        JsonResponse jsonResponse = new JsonResponse();
        //1、验证账号密码信息
        User user = userService.getUserByAllWay(username);

        if(client.getVersion().length() > 0 && ClientUtil.compareTo(client.getVersion(), "2.1.3") >= 0  ) {//&& client.isAndroid()
        	if (password != null && password.length() < 6) {//验证用户密码是否小于6位
                return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_PASSWORD_SHORT);
            }else if (user == null) {//验证用户是否存在
                return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_USER_NOT_EXISTS);        
        	}else  if(checkPasswordErrorExceedConfine(user.getUserId())){//验证密码错误次数是否超过限制,如果验证验证码通过之后会把密码错误次数减一次
            	return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_INVALID_PASSWORD_ERROR_EXCEED_CONFINE);
        	} else if (!StringUtils.equals(user.getUserPassword(), DigestUtils.md5Hex(password))) {//验证用户密码是否正确
        		savePasswordErrorInfo(user.getUserId());//保存密码错误信息
                return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_INVALID_PASSWORD);
            }
        }else{//TODO 兼容原来老版本,待之前版本放弃后，则将一下return打开,进行提示不支持此版本
//        	return new JsonResponse().setResultCode(ResultCode.ORDER_ERROR_VERSION_NOT_MATCH);
        	if (password != null && password.length() < 6) {//验证用户密码是否小于6位
                return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_PASSWORD_SHORT);
            }else if (user == null) {//验证用户是否存在
                return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_USER_NOT_EXISTS);
        	} else if (!StringUtils.equals(user.getUserPassword(), DigestUtils.md5Hex(password))) {//验证用户密码是否正确
                return jsonResponse.setResultCode(ResultCode.LOGIN_ERROR_INVALID_PASSWORD);
            }
        }
        long userId = user.getUserId();

        //2、生成cockie值放入Header中
//        userService.addSetCookie(response, user);

        //临时活动代码段2017-02-14至2017-02-28，2017-02-28之后可去除
        //lingShiHuoDongDaiMaDuan(username, client, user);
        userService.loginUser(response,user,ip,client);
        
        //4、清除登陆密码错误记录
        clearAllPasswordErrorExceedConfine(user.getUserId());
        
        logger.debug("用户UserName{}登陆成功，userId：{}",user.getUserName(),user.getUserId());

        return jsonResponse.setSuccessful();
    }

//    /**
//     * 添加用户信息到Header中
//     * @param response
//     * @param user
//     */
//	public void addSetCookie(HttpServletResponse response, User user) {
//		String cookieValue = LoginUtil.buildLoginCookieValue(user.getUserRelatedName(), user.getUserType());
//        response.addHeader("Set-Cookie", LoginUtil.buildLoginCookieHeaderValue(cookieValue));
//        //logger.debug("cookie :{},userId:{}", LoginUtil.buildLoginCookieHeaderValue(cookieValue),user.getUserId());
//	}
	
	
	
//    /**
//     * 记录用户登录日志
//     * @param ip
//     * @param client
//     * @param user
//     */
//	public void addUserLoginLog(String ip, ClientPlatform client, User user) {
//		UserLoginLog userLoginLog = new UserLoginLog();
//        userLoginLog.setUserId(user.getUserId());
//        if(client != null){
//        	userLoginLog.setClientType(client.getPlatform().getValue());
//        	userLoginLog.setClientVersion(client.getVersion());	
//        }
//        userLoginLog.setIp(ip);
//        userLoginLog.setCreateTime(System.currentTimeMillis());
//        userService.addUserLoginLog(userLoginLog);
//	}
//    

    
    /**
     * 保存密码错误信息
     * @param user
     */
    private void savePasswordErrorInfo(long userId) {
    	Object obj1 = memcachedService.get(groupKey, userId+"_1");
    	Object obj2 = memcachedService.get(groupKey, userId+"_2");
    	Object obj3 = memcachedService.get(groupKey, userId+"_3");
    	
    	if(obj1 == null){
    		memcachedService.set(groupKey, userId+"_1", exceedConfineTime, 1);
    		//logger.debug("第1次密码错误保存了密码错误记录到缓存了，userId："+userId);
    	}else if(obj2 == null){
    		memcachedService.set(groupKey, userId+"_2", exceedConfineTime, 1);
    		//logger.debug("第2次密码错误保存了密码错误记录到缓存了，userId："+userId);
    	}else if(obj3 == null){
    		memcachedService.set(groupKey, userId+"_3", exceedConfineTime, 1);
    		//logger.debug("第3次密码错误保存了密码错误记录到缓存了，userId："+userId);
    	}else{
    		//logger.debug("密码错误已经超过3次不能保存密码错误记录到缓存了，userId："+userId);
    	}
	}
    
	
	/**
     * 清除登陆密码错误记录
     */
    private void clearAllPasswordErrorExceedConfine(long userId) {
  	    memcachedService.remove(groupKey, userId+"_1");
  	    memcachedService.remove(groupKey, userId+"_2");
  	    memcachedService.remove(groupKey, userId+"_3");
  	   //logger.debug("清除登陆密码错误记录，userId："+userId);
	}
    
    /**
     * 清除一次登陆密码错误记录
     */
    public void clearOnePasswordErrorExceedConfine(String userId) {
  	    memcachedService.remove(groupKey, userId+"_1");
  	   //logger.debug("清除登陆密码错误记录，userId："+userId);
	}
    
	/**
     * 验证密码错误次数是否超过限制
     * 3小时内联系错误三次则验证未通过
     * @return
     */
    private boolean checkPasswordErrorExceedConfine(long userId) {
    	Object obj1 = memcachedService.get(groupKey, userId+"_1");
    	Object obj2 = memcachedService.get(groupKey, userId+"_2");
    	Object obj3 = memcachedService.get(groupKey, userId+"_3");
    	if (obj3 != null && obj2 != null && obj1 != null ) {
    		 //logger.debug("验证密码错误次数是否超过限制：密码错误三次，userId："+userId);
    		 return true;
 		}else{
 			//logger.debug("验证密码错误次数是否超过限制：密码错误没超过三次，userId："+userId);
 			return false;
 		}
	}

	
	
    /**
     * 临时活动代码段2017-02-14至2017-02-28，2017-02-28之后可去除
     * @param userName
     * @param user
     */
	private void lingShiHuoDongDaiMaDuan(String username, ClientPlatform client, User user) {
		//活动临时代码
        long nowTime = System.currentTimeMillis();
        long startTime = 0;
    	long endTime = 0;
    	try {
			startTime = DateUtil.parseStrTime2Long("2017-02-20 00:00:00");
//			startTime = DateUtil.parseStrTime2Long("2017-02-20 00:00:00");
			endTime = DateUtil.parseStrTime2Long("2017-02-28 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	if(nowTime >= startTime && nowTime <= endTime && user.getCreateTime() >= startTime && user.getCreateTime() <= endTime){
    		//判断是否第一次登录
    		UserLoginLog userLoginLog = userService.getUserNewestLoginLog(username);
    		if(userLoginLog == null ){
    			//再判断是否是被邀请的
    			UserInviteRecord userInviteRecord = userInviteService.getByInvitedUserId(user.getUserId());
    			
    			if(userInviteRecord != null && userInviteRecord.getUserId() > 0){
    				User hostUser = userMapper.getUser(userInviteRecord.getUserId());
    				if (userHandlers != null && hostUser != null) {
    					for (UserHandler handler : userHandlers) {
//    						System.out.println("@!!!@");
    						handler.handleInvite(user, hostUser.getyJJNumber(), nowTime, client);
    					}
    				}
    				
    			}
    		}
    	}
	}
    
    
    
    
    @SuppressWarnings("unchecked")
    public JsonResponse verifyCodeLoginSendCode(String phone)  {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	if(phone != null && phone.length() == 11){

//    		User user = userMapper.getUserByAllWay(phone);
//    		if (user != null) {
//    			return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
//    		}
    		
    		
    		boolean result = yunXinSmsService.sendCode(phone,1);
    		if(result){
    			data.put("sendResult", "SUCCESS");
    			
    		}else{
    			data.put("sendResult", "FAIL");
    			return jsonResponse.setResultCode(ResultCode.PHONE_SEND_FAIL);
    		}
    		
    	}else{
    		data.put("sendResult", "ERROR_NUMBER");
    		data.put("sendResultMsg", "手机号码有误！");
    		return jsonResponse.setResultCode(ResultCode.PHONE_ERROR_NUMBER);
    	}
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @SuppressWarnings("unchecked")
    public JsonResponse verifyCodeLoginCommit(String phone, String verifyCode
    		,ClientPlatform clientPlatform, String ip
    		,HttpServletResponse response)  {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	if(phone != null && phone.length() == 11){
    		if (!registerFacade.verifyPhoneVerifyCode(phone, verifyCode, true, CodeUseage.PHONE_REGISTER)) {
                return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
            }
    		User user = userMapper.getUserByAllWay(phone);
    		if (user != null) {
    			userService.loginUser(response,user,ip,clientPlatform);
    	        //4、清除登陆密码错误记录
    	        clearAllPasswordErrorExceedConfine(user.getUserId());
    	        
    	        
    	        logger.debug("用户UserName{}登陆成功，userId：{}",user.getUserName(),user.getUserId());
    		}else{
    			long time = System.currentTimeMillis();
    	        user = new User();
    	        user.setUserName(phone);
    	        user.setUserRelatedName(phone);
    	        user.setUserType(UserType.PHONE);
    	        user.setUserPassword("");
    	        user.setStatus(UserStatus.NORMAL);
    	        user.setCreateTime(time);
    	        user.setUpdateTime(time);
    	        if(clientPlatform != null && clientPlatform.getPlatform() != null && clientPlatform.getPlatform().isIOS()){
    	        	user.setRegistrationSource(UserRegSource.IPHONE.getIntValue());
    	        }else if(clientPlatform != null && clientPlatform.getPlatform() != null && clientPlatform.getPlatform().isAndroid()){
    	        	user.setRegistrationSource(UserRegSource.ANDROID.getIntValue());
    	        }else if(clientPlatform != null && clientPlatform.getPlatform() != null && clientPlatform.getPlatform().is("web")){
    	        	user.setRegistrationSource(UserRegSource.WEB.getIntValue());
    	        }else{
    	        	user.setRegistrationSource(UserRegSource.OTHER.getIntValue());
    	        }
    	        try {
    	        	registerFacade.addUser(user, -1, clientPlatform);
    			} catch (ParameterErrorException e) {
    				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
    			}
    	      //新注册用户赠送优惠券
    	        userMemberService.sendUserCoupon(user.getUserId(), 1);	//新后台配置
//    	        JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.REGISTER_COUPONS);
//    	        JSONArray jsonArray = jsonObject.getJSONArray("coupons");
//    	        for (Object object2 : jsonArray) {
//    				JSONObject jObject = (JSONObject)object2;
//    				Integer count = jObject.getInteger("count");
//    				Long templateId = jObject.getLong("template_id");
//    				try {
//    					orderCouponService.getCoupon(templateId, count, user.getUserId(), CouponGetWay.REGISTER, true);
//    				} catch (Exception e) {
//    					logger.error(e.getMessage());
//    				}
//    			}
    	        
    	        
    	    	userService.loginUser(response, user, ip,clientPlatform);
    	    	UserCoin coins = userCoinService.getUserCoin(user.getUserId());
    	     
    	        data.put("gainCoins", coins == null ? 0 :coins.getUnavalCoins());
    	        data.put("passwordSetTips", "您可在帐户中心设置登录密码。");
    	       // data.put("noPasswordFlag", "YES");
    			
    		}
    		if(user.getUserPassword() != null && user.getUserPassword().trim().length() > 0){
	        	data.put("noPasswordFlag", "false");
	        }else{
	        	data.put("noPasswordFlag", "true");
	        }
    		
    	
    	}else{
    		data.put("sendResult", "ERROR_NUMBER");
    		data.put("sendResultMsg", "手机号码有误！");
    		return jsonResponse.setResultCode(ResultCode.PHONE_ERROR_NUMBER);
    	}
    	return jsonResponse.setSuccessful().setData(data);
    }
}

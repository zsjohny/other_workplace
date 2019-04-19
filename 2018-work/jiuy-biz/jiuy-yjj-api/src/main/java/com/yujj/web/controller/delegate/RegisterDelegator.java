package com.yujj.web.controller.delegate;

import java.util.HashMap;
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
import com.jiuyuan.constant.CaptchaType;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.account.CodeUseage;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.constant.account.UserRegSource;
import com.jiuyuan.constant.account.UserStatus;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.entity.Activity;
import com.jiuyuan.entity.CaptchaParams;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.account.UserCoin;
import com.jiuyuan.entity.account.UserMember;
import com.jiuyuan.entity.favorite.UserFavorite;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.CaptchaFacade;
import com.yujj.business.facade.RegisterFacade;
import com.yujj.business.service.ActivityService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.OrderCouponService;
import com.yujj.business.service.UserCoinService;
import com.yujj.business.service.UserFavoriteService;
import com.yujj.business.service.UserMemberService;
import com.yujj.business.service.UserService;
import com.yujj.dao.mapper.StoreBusinessMapper;
import com.yujj.dao.mapper.UserFavoriteMapper;
import com.yujj.dao.mapper.UserMemberMapper;
import com.yujj.dao.mapper.UserVisitMapper;
import com.yujj.entity.StoreBusiness;
import com.yujj.entity.account.User;
import com.yujj.exception.ParameterErrorException;
import com.jiuyuan.ext.spring.web.method.ClientIp;

@Service
public class RegisterDelegator {
	
	private static final Logger logger = LoggerFactory.getLogger("Register");
	
	private static  int GRANT_COINS = 99 * 5;
	
    //短信出错开启
	private boolean cancelVerifyCode = false;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private RegisterFacade registerFacade;

    @Autowired
    private CaptchaFacade captchaFacade;    

    @Autowired
    private MemcachedService memcachedService;

    @Autowired
    private GlobalSettingService globalSettingService;
    
    @Autowired
    private OrderCouponService orderCouponService;
    
    @Autowired
    private UserMemberMapper userMemberMapper;
    
    @Autowired
    private LoginDelegator loginDelegator;
    
    @Autowired
    private StoreBusinessMapper storeBusinessMapper;
    
    @Autowired
    private UserMemberService userMemberService;
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private UserFavoriteService userFavoriteService;
    
    @Autowired
    private UserFavoriteMapper userFavoriteMapper;
    
    @Autowired
    private UserVisitMapper userVisitMapper;
    
    public JsonResponse sendPhoneVerifyCode(String phone, CaptchaParams captchaParams, boolean needCaptcha, long userId) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        if (needCaptcha) {
            CaptchaType captchaType = CaptchaType.REGISTER;
            String cacheKey = captchaFacade.getAndValidateCacheKey(userId, captchaParams);
            if (!captchaFacade.verifyCode(captchaParams.getCode(), captchaType, cacheKey, false)) {
                return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_CAPTCHA_INVALID);
            }
        }

        // User user = userService.getUserByRelatedName(phone, UserType.PHONE);
        User user = userService.getUserByAllWay(phone);
        if (user != null) {
            return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
        }

        boolean success = registerFacade.sendPhoneVerifyCode(phone, CodeUseage.PHONE_REGISTER);
        
        if(success){
			data.put("sendResult", "SUCCESS");
			
		}else{
			data.put("sendResult", "FAIL");
		}
        jsonResponse.setData(data);
        
        if (cancelVerifyCode)
        	success = true;
        
        return jsonResponse.setResultCode(success ? ResultCode.COMMON_SUCCESS : ResultCode.PHONE_SEND_FAIL);
    }

    public JsonResponse phoneNumberCommit(String phone, String password, String verifyCode, String inviteCode, 
                                          HttpServletResponse response, String ip, ClientPlatform client) {
        JsonResponse jsonResponse = new JsonResponse();

        if (StringUtils.length(password) < 6) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        } else if (!cancelVerifyCode && !registerFacade.verifyPhoneVerifyCode(phone, verifyCode, true, CodeUseage.PHONE_REGISTER)) {
            return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
        }

        // User user = userService.getUserByRelatedName(phone, UserType.PHONE);
        User user = userService.getUserByAllWay(phone);
        if (user != null) {
            return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
        }

        long time = System.currentTimeMillis();
        user = new User();
        user.setUserName(phone);
        user.setUserRelatedName(phone);
        user.setUserType(UserType.PHONE);
        user.setUserPassword(DigestUtils.md5Hex(password));
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
        
        //设置俞姐姐号
//        List<YJJNumber> yjjNumberList = null;
//        // getMemCache
//        String groupKey = MemcachedKey.GROUP_KEY_YJJNUMBER;
//        String key = "yjjnumber";
//        Object obj = memcachedService.get(groupKey, key);
//        if (obj != null && ((List<YJJNumber>)obj).size()>0) {
//        	yjjNumberList = (List<YJJNumber>)obj;
//        }
//        else {
//        	//分段去取10组1000个未使用俞姐姐号放入内存使用
//        	yjjNumberList = userService.getYJJNumberList(1, 100000, 1000 , 0);
//            List<YJJNumber> yjjNumberList2 = userService.getYJJNumberList(100000, 200000, 1000 , 0);
//            List<YJJNumber> yjjNumberList3 = userService.getYJJNumberList(200000, 300000, 1000 , 0);
//            List<YJJNumber> yjjNumberList4 = userService.getYJJNumberList(300000, 400000, 1000 , 0);
//            List<YJJNumber> yjjNumberList5 = userService.getYJJNumberList(400000, 500000, 1000 , 0);
//            List<YJJNumber> yjjNumberList6 = userService.getYJJNumberList(500000, 600000, 1000 , 0);
//            List<YJJNumber> yjjNumberList7 = userService.getYJJNumberList(600000, 700000, 1000 , 0);
//            List<YJJNumber> yjjNumberList8 = userService.getYJJNumberList(700000, 800000, 1000 , 0);
//            List<YJJNumber> yjjNumberList9 = userService.getYJJNumberList(800000, 900000, 1000 , 0);
//            List<YJJNumber> yjjNumberList10 = userService.getYJJNumberList(900000, 1000000, 1000 , 0);
//            
//            if(yjjNumberList2 != null && yjjNumberList2 .size() > 0){
//            	yjjNumberList.addAll(yjjNumberList2);
//            }
//            if(yjjNumberList3 != null && yjjNumberList3 .size() > 0){
//            	yjjNumberList.addAll(yjjNumberList3);
//            }
//            if(yjjNumberList4 != null && yjjNumberList4 .size() > 0){
//            	yjjNumberList.addAll(yjjNumberList4);
//            }
//            if(yjjNumberList5 != null && yjjNumberList5 .size() > 0){
//            	yjjNumberList.addAll(yjjNumberList5);
//            }
//            if(yjjNumberList6 != null && yjjNumberList6 .size() > 0){
//            	yjjNumberList.addAll(yjjNumberList6);
//            }
//            if(yjjNumberList7 != null && yjjNumberList7 .size() > 0){
//            	yjjNumberList.addAll(yjjNumberList7);
//            }
//            if(yjjNumberList8 != null && yjjNumberList8 .size() > 0){
//            	yjjNumberList.addAll(yjjNumberList8);
//            }
//            if(yjjNumberList9 != null && yjjNumberList9 .size() > 0){
//            	yjjNumberList.addAll(yjjNumberList9);
//            }
//            if(yjjNumberList10 != null && yjjNumberList10 .size() > 0){
//            	yjjNumberList.addAll(yjjNumberList10);
//            }
//            
//            memcachedService.set(groupKey, key, DateConstants.SECONDS_FOREVER, yjjNumberList);
//        }
//        
//        int randomNum = (int) (Math.random() * yjjNumberList.size()) ;
//   
//        YJJNumber number = yjjNumberList.get(randomNum);
//        
//        int k = 0;
//	    while(number.getNumber() <= 0 && k++ < 5){
//        	yjjNumberList.remove(randomNum);
//        	randomNum = (int) (Math.random() * yjjNumberList.size()) ;
//        	number = yjjNumberList.get(randomNum);
//        }
//        
//        user.setyJJNumber(number.getNumber());

        registerFacade.addUser(user, inviteCode, client);
        
//        yjjNumberList.remove(randomNum);
//        userService.updateYjjNumberUsed(number.getNumber());
//
//        // setMemCache
//        memcachedService.set(groupKey, key, DateConstants.SECONDS_FOREVER, yjjNumberList);
        
        
//        //添加用户信息到Header中
//    	loginDelegator.addSetCookie(response, user);
//    	//记录用户登录日志
//    	loginDelegator.addUserLoginLog(ip, client, user);
    	userService.loginUser(response, user, ip,client);
    	
//        String cookieValue = LoginUtil.buildLoginCookieValue(user.getUserRelatedName(), user.getUserType());
//        response.addHeader("Set-Cookie", LoginUtil.buildLoginCookieHeaderValue(cookieValue));
//        
//        //记录用户登录日志
//        UserLoginLog userLoginLog = new UserLoginLog();
//        userLoginLog.setUserId(user.getUserId());
//        if(client != null){
//        	userLoginLog.setClientType(client.getPlatform().getValue());
//        	userLoginLog.setClientVersion(client.getVersion());	
//        }
//        userLoginLog.setIp(ip);
//        userLoginLog.setCreateTime(time);
//        userService.addUserLoginLog(userLoginLog);

        UserCoin coins = userCoinService.getUserCoin(user.getUserId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("gainCoins", coins == null ? 0 :coins.getUnavalCoins());
        return jsonResponse.setSuccessful().setData(data);
    }

	public JsonResponse phoneNumberCommit18(String phone, String password, String verifyCode, long yJJNumber, long storeId, long productId,
			HttpServletResponse response, ClientPlatform clientPlatform,String ip) {
		JsonResponse jsonResponse = new JsonResponse();

		if (StringUtils.length(password) < 6) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
		}
		
        String groupKey_verifyCode = MemcachedKey.GROUP_KEY_PHONE_VERIFY_CODE;
        String key_verifyCode = phone;
		Object object = memcachedService.get(groupKey_verifyCode, key_verifyCode);
		
		if (object == null) {
			if (!cancelVerifyCode && !registerFacade.verifyPhoneVerifyCode(phone, verifyCode, true, CodeUseage.PHONE_REGISTER)) {
	            return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
	        }
		} else {
			if (!cancelVerifyCode && !(StringUtils.equals((String)object, verifyCode))) {
				return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
			}
		}

        User user = userService.getUserByAllWay(phone);
        if (user != null) {
            return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_REGISTED);
        }

        long time = System.currentTimeMillis();
        user = new User();
        user.setUserName(phone);
        user.setUserRelatedName(phone);
        user.setUserType(UserType.PHONE);
        user.setUserPassword(DigestUtils.md5Hex(password));
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
        	user.setRegistrationSource(UserRegSource.WEB.getIntValue());
        }
        
        //设置俞姐姐号
//        List<YJJNumber> yjjNumberList = null;
//        // getMemCache
//        String groupKey = MemcachedKey.GROUP_KEY_YJJNUMBER;
//        String key = "yjjnumber";
//        Object obj = memcachedService.get(groupKey, key);
//        if (obj != null && ((List<YJJNumber>)obj).size()>0) {
//        	yjjNumberList = (List<YJJNumber>)obj;
//        }
//        else {
//        	//分段去取10组1000个未使用俞姐姐号放入内存使用
//        	yjjNumberList = userService.getYJJNumberList(1, 100000, 1000 , 0);
//            List<YJJNumber> yjjNumberList2 = userService.getYJJNumberList(100000, 200000, 1000 , 0);
//            List<YJJNumber> yjjNumberList3 = userService.getYJJNumberList(200000, 300000, 1000 , 0);
//            List<YJJNumber> yjjNumberList4 = userService.getYJJNumberList(300000, 400000, 1000 , 0);
//            List<YJJNumber> yjjNumberList5 = userService.getYJJNumberList(400000, 500000, 1000 , 0);
//            List<YJJNumber> yjjNumberList6 = userService.getYJJNumberList(500000, 600000, 1000 , 0);
//            List<YJJNumber> yjjNumberList7 = userService.getYJJNumberList(600000, 700000, 1000 , 0);
//            List<YJJNumber> yjjNumberList8 = userService.getYJJNumberList(700000, 800000, 1000 , 0);
//            List<YJJNumber> yjjNumberList9 = userService.getYJJNumberList(800000, 900000, 1000 , 0);
//            List<YJJNumber> yjjNumberList10 = userService.getYJJNumberList(900000, 1000000, 1000 , 0);
//            
//            if(yjjNumberList2 != null && yjjNumberList2.size() > 0){
//            	yjjNumberList.addAll(yjjNumberList2);
//            }
//            if(yjjNumberList3 != null && yjjNumberList3.size() > 0){
//            	yjjNumberList.addAll(yjjNumberList3);
//            }
//            if(yjjNumberList4 != null && yjjNumberList4.size() > 0){
//            	yjjNumberList.addAll(yjjNumberList4);
//            }
//            if(yjjNumberList5 != null && yjjNumberList5.size() > 0){
//            	yjjNumberList.addAll(yjjNumberList5);
//            }
//            if(yjjNumberList6 != null && yjjNumberList6.size() > 0){
//            	yjjNumberList.addAll(yjjNumberList6);
//            }
//            if(yjjNumberList7 != null && yjjNumberList7.size() > 0){
//            	yjjNumberList.addAll(yjjNumberList7);
//            }
//            if(yjjNumberList8 != null && yjjNumberList8.size() > 0){
//            	yjjNumberList.addAll(yjjNumberList8);
//            }
//            if(yjjNumberList9 != null && yjjNumberList9.size() > 0){
//            	yjjNumberList.addAll(yjjNumberList9);
//            }
//            if(yjjNumberList10 != null && yjjNumberList10.size() > 0){
//            	yjjNumberList.addAll(yjjNumberList10);
//            }
//            
//            memcachedService.set(groupKey, key, DateConstants.SECONDS_FOREVER, yjjNumberList);
//        }
//        
//        int randomNum = (int) (Math.random() * yjjNumberList.size()) ;
//   
//        YJJNumber number = yjjNumberList.get(randomNum);
//        
//        int k = 0;
//	    while(number.getNumber() <= 0 && k++ < 5){
//        	yjjNumberList.remove(randomNum);
//        	randomNum = (int) (Math.random() * yjjNumberList.size()) ;
//        	number = yjjNumberList.get(randomNum);
//        }
//        
//        user.setyJJNumber(number.getNumber());
        try {
        	registerFacade.addUser(user, yJJNumber, clientPlatform);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
        
//        yjjNumberList.remove(randomNum);
//        userService.updateYjjNumberUsed(number.getNumber());
//
//        // setMemCache
//        memcachedService.set(groupKey, key, DateConstants.SECONDS_FOREVER, yjjNumberList);
        
        if (storeId != -1) {
        	bindStoreRelation(user, storeId, null, "", clientPlatform);
		} else if (yJJNumber != -1){
			User invitor = userService.getUserByYJJNumber(yJJNumber);
			UserMember userMember = userMemberMapper.getByUserId(invitor.getUserId());
			if (userMember != null) {
				userMemberMapper.addDistributionPartners(userMember.getId());
				String inviteRelation = "";
				if(userMember.getInviteRelation() != null ){
					inviteRelation = userMember.getInviteRelation()+ invitor.getUserId() +",";
				}else{
					inviteRelation = "," + invitor.getUserId() +",";
				}
				if (userMember.getBelongStoreId() != null && userMember.getBelongStoreId() > 0) {
					bindStoreRelation(user, userMember.getBelongStoreId(), invitor.getUserId(), inviteRelation , clientPlatform);
				}
			}
		}
        
        
        
      //添加用户信息到Header中
//    	loginDelegator.addSetCookie(response, user);
//        String cookieValue = LoginUtil.buildLoginCookieValue(user.getUserRelatedName(), user.getUserType());
//        response.addHeader("Set-Cookie", LoginUtil.buildLoginCookieHeaderValue(cookieValue));
        
		userService.loginUser(response,user,ip,clientPlatform);

        Map<String, Object> data = new HashMap<String, Object>();

        UserCoin coins = userCoinService.getUserCoin(user.getUserId());
        data.put("gainCoins", coins.getUnavalCoins());
        
        //新注册用户赠送优惠券
        sendRegisterUserCoupon(user.getUserId());
//        JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.REGISTER_COUPONS);
//        JSONArray jsonArray = jsonObject.getJSONArray("coupons");
//        for (Object object2 : jsonArray) {
//			JSONObject jObject = (JSONObject)object2;
//			Integer count = jObject.getInteger("count");
//			Long templateId = jObject.getLong("template_id");
//			try {
//				orderCouponService.getCoupon(templateId, count, user.getUserId(), CouponGetWay.REGISTER, true);
//			} catch (Exception e) {
//				logger.error(e.getMessage());
//			}
//		}
        
        //用户若是通过商品分享注册的，加入收藏及足迹
        if (productId != -1) {
        	UserFavorite uFavorite = new UserFavorite();
        	uFavorite.setCreateTime(time);
        	uFavorite.setUpdateTime(time);
        	uFavorite.setRelatedId(productId);
        	uFavorite.setType(0);
        	uFavorite.setUserId(user.getUserId());
        	uFavorite.setStatus(0);
        	userFavoriteMapper.addFavorite(uFavorite);
        	
        	Long[] ids = {productId};
        	userVisitMapper.addVisitHistory(user.getUserId(), ids, time);
		}
        
        return jsonResponse.setSuccessful().setData(data);																
	}
	
	private void sendRegisterUserCoupon(long userId){
		userMemberService.sendUserCoupon(userId, 1);
	}

	private void bindStoreRelation(User user, long storeId, Long parentDistribution,String inviteRelation, ClientPlatform clientPlatform) {
		StoreBusiness storeBusiness = storeBusinessMapper.getById(storeId);
		
		long time = System.currentTimeMillis();
		UserMember userMember = new UserMember(user.getUserId(), time, time, 0, parentDistribution, 0, storeBusiness.getBusinessName(), storeId ,inviteRelation);
		userMemberService.add(userMember, time);
		
		Activity activity = activityService.getActivity("collectstore");
		if (activity != null ) {		
			GRANT_COINS = activity.getGrantAmountRandom();
		}
		
		userCoinService.updateUserCoin(user.getUserId(), 0, GRANT_COINS, activity.getActivityCode(), System.currentTimeMillis(), UserCoinOperation.ACTIVITY, null, clientPlatform.getVersion());
	}

	public JsonResponse verifyCode18(String phone, String verifyCode, HttpServletResponse response) {
		JsonResponse jsonResponse = new JsonResponse();

		if (!cancelVerifyCode && !registerFacade.verifyPhoneVerifyCode(phone, verifyCode, true, CodeUseage.PHONE_REGISTER)) {
            return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_PHONE_VERIFY_CODE_INVALID);
        }
		
        String groupKey = MemcachedKey.GROUP_KEY_PHONE_VERIFY_CODE;
        String key = phone;
		memcachedService.set(groupKey, key, DateConstants.SECONDS_FIVE_MINUTES, verifyCode);
 		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
}

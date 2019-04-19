package com.store.service;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.account.CodeUseage;
import com.jiuyuan.entity.Activity;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.service.WhitePhoneService;
import com.jiuyuan.service.common.MemcachedService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.yujj.business.handler.account.UserHandler;
import com.yujj.business.service.ActivityService;
import com.yujj.entity.account.User;

@Service
public class RegisterFacade {
	 private static final Log logger = LogFactory.get();
	 
    private static final boolean SMS_SERVICE_FLAG = true;
    
    @Autowired
    private WhitePhoneService whitePhoneService;

//
//
//    @Autowired
//    private YunXinSmsService yunXinSmsService;

    @Autowired
    private ShopUserService userService;
    
    @Autowired
    private MemcachedService memcachedService;
    
//    @Autowired
//    private ActivityService activityService;

    @Autowired(required = false)
    private List<UserHandler> userHandlers;

    /* added by Dongzhong 2016-03-20*/
    public boolean sendPhoneInitPassword(String phone, String password) {
    	
    	int coinNum = 999;
    	 Activity activity = new Activity();// activityService.getActivity("reg2step");
    	// Activity activity = activityService.getActivity("reg2step");
     	if (activity != null && activity.getGrantAmountRandom() > 0) {		
     		coinNum = activity.getGrantAmountRandom();
 		}
    	if (!SMS_SERVICE_FLAG) {
    		String content = "[俞姐姐]恭喜您获得" + coinNum + "个玖币，您初始登陆密码为"+password+"，下载APP点击：http://t.cn/RGD4rwR，打开即可使用";
    	
    		return true;//smsService.send(phone, content);
    	} else {
    		JSONArray params = new JSONArray();
    		params.add(coinNum + "");
    		params.add(password);
    		
    		return true;//yunXinSmsService.send(phone, params, 8224); // Fix me
    	}
    	
    }
    
    public boolean sendPhoneVerifyCode(String phone, CodeUseage codeUseage) {
    	/*
        String groupKey = MemcachedKey.GROUP_KEY_PHONE_VERIFY_CODE;
        String key = codeUseage + "#" + phone;

        String verifyCode = RandomStringUtils.randomNumeric(6);
        long expireTime = System.currentTimeMillis() + DateUtils.MILLIS_PER_MINUTE * 10; // 失效时间
        int invalidateCount = 0;// 增加校验次数，输错5次后验证码失效，防止被暴力破解
        String cacheValue = verifyCode + "#" + expireTime + "#" + invalidateCount;

        memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, cacheValue);

        if (!SMS_SERVICE_FLAG) {
            String content = "【俞姐姐】您的验证码是" + verifyCode + ", 10分钟内使用有效。如非本人操作，请忽略本短信";
            return smsService.send(phone, content);
        } else {
            JSONArray params = new JSONArray();
            params.add(verifyCode);
            params.add("10");
            return yunXinSmsService.send(phone, params, 6110);
        }
        */
    	
    	if (whitePhoneService.getWhitePhone(phone) > 0) return true;
    	boolean success =true;// yunXinSmsService.sendCode(phone);
        return success;
        
    }

    public boolean verifyPhoneVerifyCode(String phone, String verifyCode, boolean removeIfSuccess, CodeUseage codeUseage) {
      /*
    	String groupKey = MemcachedKey.GROUP_KEY_PHONE_VERIFY_CODE;
        String key = codeUseage + "#" + phone;
        Object obj = memcachedService.get(groupKey, key);
        if (obj == null) {
            return false;
        }

        try {
            String[] parts = StringUtils.split((String) obj, "#");
            long time = System.currentTimeMillis();
            if (time > Long.parseLong(parts[1])) { // 过期
                return false;
            }

            if (StringUtils.equals(parts[0], verifyCode)) {
                if (removeIfSuccess) {
                    memcachedService.remove(groupKey, key);
                }
                return true;
            }
    
            int invalidateCount = Integer.parseInt(parts[2]);
            if (++invalidateCount > 5) {
                memcachedService.remove(groupKey, key);
            } else {
                parts[2] = String.valueOf(invalidateCount);
                memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, StringUtils.join(parts, "#"));
            }
        } catch (Exception e) {
            memcachedService.remove(groupKey, key);
        }
        return false;
       	*/

    	if (whitePhoneService.getWhitePhone(phone) > 0) return true;
    	return true;//yunXinSmsService.verifyCode(phone, verifyCode);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addUser(User user, String inviteCode, ClientPlatform clientPlatform) {
        userService.addUser(user);

        if (userHandlers != null) {
            for (UserHandler handler : userHandlers) {
                handler.postUserCreation(user, inviteCode, clientPlatform);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addUser(User user, String inviteCode, String relatedId, ClientPlatform clientPlatform) {
        userService.addUser(user);

        if (userHandlers != null) {
            for (UserHandler handler : userHandlers) {
                handler.postUserCreation(user, inviteCode, relatedId, clientPlatform);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
	public void addUser(User user, long yJJNumber, ClientPlatform clientPlatform) {
    	userService.addUser(user);

        if (userHandlers != null) {
            for (UserHandler handler : userHandlers) {
                handler.postUserCreation(user, yJJNumber, clientPlatform);
            }
        }
        
	}

}

package com.yujj.web.controller.mobile;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.entity.Activity;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.util.AESUtil;
import com.jiuyuan.util.DateUtil;

import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;import com.jiuyuan.util.spring.ControllerUtil;
import com.jiuyuan.web.help.JsonResponse;
//import com.jiuyuan.web.interceptor.UriBuilder;
import com.yujj.business.service.ActivityService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.UserCoinService;
import com.yujj.business.service.UserService;
import com.yujj.entity.account.User;
import com.yujj.entity.account.UserDetail;
import com.yujj.util.uri.UriBuilder;

@Controller
@Login
@RequestMapping("/mobile/grant")
public class MobileGrantController {

    @Autowired
    private UserCoinService userCoinService;
    
    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private GlobalSettingService globalSettingService;
    
    private static final String SHARE_TOKEN_ENCRYPT_PASSWORD = "J&3jj#2~p=";

    public static String getUserShareUrl(long userId) {
        String token = AESUtil.encrypt(String.valueOf(userId), "UTF-8", SHARE_TOKEN_ENCRYPT_PASSWORD);
        UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/mobile/grant/share.do");
        builder.set("token", token);
        return builder.toUri();
    }

    /**
     * 手机app点击调用
     * @param token
     * @param userDetail
     * @param clientPlatform
     * @return
     */
    @RequestMapping(value = "/share", method = RequestMethod.GET)
    @NoLogin
    public String shareView(String token, UserDetail userDetail, ClientPlatform clientPlatform) {
        long targetUserId = 0;
        if (StringUtils.isNotBlank(token)) {
            targetUserId = NumberUtils.toLong(AESUtil.decrypt(token, "UTF-8", SHARE_TOKEN_ENCRYPT_PASSWORD), 0);
        }

        if (targetUserId > 0) {
            String virtualDeviceId = userDetail.getVirtualDeviceId();
            UserCoinOperation operation = UserCoinOperation.SHARE_CALLBACK;
            if (userCoinService.getUserCoinLogCountByRelatedId(targetUserId, virtualDeviceId, operation) <= 0) {
            	 JSONObject jiucoin_global_setting = globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING);
             	JSONObject promoteShareSetting = jiucoin_global_setting.getJSONObject("promoteShareSetting");
             	int maxCountEachShareCycle = promoteShareSetting.getInteger("maxCountEachShareCycle");
             	int shareCycle = promoteShareSetting.getInteger("shareCycle");
             	
             	long time = System.currentTimeMillis();
             	long startTime = DateUtil.getTodayEnd() - shareCycle * DateUtils.MILLIS_PER_DAY;
             	long endTime = DateUtil.getTodayEnd();

                int count = userCoinService.getUserCoinLogCount(targetUserId, startTime, endTime, operation);
                
                if (count >= maxCountEachShareCycle) {
                    return "";
                }
                Activity activity = activityService.getActivity("shareVisit");
                int grantCount = 50;
                if (activity != null ) {
        			
            		grantCount = activity.getGrantAmountRandom();
        		}
                
//                if (VersionUtil.compareVersion(clientPlatform.getVersion(), "1.8.11") < 0) {
//                	userCoinService.updateUserCoin(targetUserId, 0, grantCount, "share", time, operation);
//                } else {
//                	userCoinService.updateUserCoin(targetUserId, 0, 0, "share", time, operation);
//                }
                int eachShareObtain = promoteShareSetting.getInteger("eachShareObtain");
                userCoinService.updateUserCoin(targetUserId, 0, eachShareObtain, "share", time, operation, null, clientPlatform.getVersion());
                
            }
        }

        return ControllerUtil.redirect(Constants.SERVER_URL_HTTPS + "/static/app/login/registershare.html");
    }

    @RequestMapping(value = "/share", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse grantShare(UserDetail userDetail, ClientPlatform clientPlatform) {
        JsonResponse jsonResponse = new JsonResponse();
        long userId = userDetail.getUserId();
        
        JSONObject jiucoin_global_setting = globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING);
    	JSONObject promoteShareSetting = jiucoin_global_setting.getJSONObject("promoteShareSett ing");
    	int maxCountEachClickCycle = promoteShareSetting.getInteger("maxCountEachClickCycle");
    	int clickCycle = promoteShareSetting.getInteger("clickCycle");
    	
        long time = System.currentTimeMillis();
        long startTime = DateUtil.getTodayEnd() - clickCycle * DateUtils.MILLIS_PER_DAY;
        long endTime = DateUtil.getTodayEnd();
        
//        long time = System.currentTimeMillis();
//        long startTime = DateUtil.getDayZeroTime(time);
//        long endTime = startTime + DateUtils.MILLIS_PER_DAY;
        UserCoinOperation operation = UserCoinOperation.SHARE_GRANT;
        
        int count = userCoinService.getUserCoinLogCount(userDetail.getUserId(), startTime, endTime, operation);
        if (count >= maxCountEachClickCycle) {  //每天分享不能超过10次
        	return jsonResponse.setResultCode(ResultCode.GRANT_ERROR_DAY_GRANT_LIMIT);
        }
        Activity activity = activityService.getActivity("share");
        int grantCount = 100;
        if (activity != null ) {
			
    		grantCount = activity.getGrantAmountRandom();
		}
        
//        if (VersionUtil.compareVersion(clientPlatform.getVersion(), "1.8.11") < 0) {
//        	userCoinService.updateUserCoin(userId, 0, grantCount, "share", time, operation);
//        } else {
//        	userCoinService.updateUserCoin(userId, 0, 0, "share", time, operation);
//        }
        int eachClickObtain = promoteShareSetting.getInteger("eachClickObtain");
        userCoinService.updateUserCoin(userId, 0, eachClickObtain, "share", time, operation, null, clientPlatform.getVersion());
        
        return jsonResponse.setSuccessful();
    }
    
    @RequestMapping(value = "/scanCode", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse grantScanCode(UserDetail userDetail, ClientPlatform clientPlatform) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	long userId = userDetail.getUserId();
    	long time = System.currentTimeMillis();
    	long startTime = DateUtil.getDayZeroTime(time);
    	long endTime = startTime + DateUtils.MILLIS_PER_DAY;
    	UserCoinOperation operation = UserCoinOperation.EXCHANGE;
    	
    	int count = userCoinService.getUserCoinLogCount(userId, startTime, endTime, operation);
    	if (count >= 1) {
    		return jsonResponse.setResultCode(ResultCode.GRANT_ERROR_DAY_GRANT_LIMIT);
    	}
    	Activity activity = activityService.getActivity("scanCode");
    	
    	int grantCount = 495;
    	if (activity != null ) {
			
    		grantCount = activity.getGrantAmountRandom();
		}
		userCoinService.updateUserCoin(userId, 0, grantCount, "scanCode", time, operation, null, clientPlatform.getVersion());
    	
    	return jsonResponse.setSuccessful();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse grantShare(ClientPlatform clientPlatform) {
        String[] names = new String[]{}; // keep empty when commit
        JsonResponse jsonResponse = new JsonResponse();

        long time = System.currentTimeMillis();
        UserCoinOperation operation = UserCoinOperation.SHARE_GRANT;
        
        Activity activity = activityService.getActivity("batch");
        int grantCount = 250;
        if (activity != null ) {
			
    		grantCount = activity.getGrantAmountRandom();
		}

        for (String name : names) {
            User user = userService.getUserByAllWay(name);
            if (user == null) {
                continue;
            }
            
    		userCoinService.updateUserCoin(user.getUserId(), 0, grantCount, "share", time, operation, null, clientPlatform.getVersion());
        }

        return jsonResponse.setSuccessful();
    }
}

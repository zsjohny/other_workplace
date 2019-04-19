package com.yujj.web.controller.wap;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.constant.account.UserRegSource;
import com.jiuyuan.constant.account.UserStatus;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.entity.Activity;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.visit.AccessLog;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.RegisterFacade;
import com.yujj.business.service.AccessLogService;
import com.yujj.business.service.ActivityService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.UserCoinService;
import com.yujj.business.service.UserService;
import com.yujj.entity.account.User;
import com.yujj.entity.account.UserDetail;
import com.yujj.web.controller.mobile.MobileGrantController;

@Controller
@RequestMapping("/m/grant")
public class WapGrantController {

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private RegisterFacade registerFacade;
    
    @Autowired
    private AccessLogService accessLogService;
    
    @Autowired
    private GlobalSettingService globalSettingService;

    
    @Autowired
    private MemcachedService memcachedService;
    
//    private static final String SHARE_TOKEN_ENCRYPT_PASSWORD = "J&3jj#2~p=";
  
    @RequestMapping(value = "/activity", method = RequestMethod.GET)
    public String grantForm(HttpServletRequest request, @ClientIp String ip) {
    	
        // 增加活动页面访问量
    	long time = System.currentTimeMillis();
        AccessLog accessLog = new AccessLog();
        accessLog.setAccessUrl(request.getRequestURI());  
        accessLog.setFromIp(ip);
        accessLog.setAccessMemo("神州专车摇一摇活动访问页面");
        accessLog.setCreateDate(time);
        accessLogService.addAccessLog(accessLog);
        
    	return "wap/ucar_reg";
    }

    @RequestMapping(value = "/activity", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse grantShare2Step(ClientPlatform clientPlatform, @RequestParam("activityCode") String activityCode,
                                   @RequestParam("username") String username) {
        JsonResponse jsonResponse = new JsonResponse();
        
        int newcoins = 0;
        String redirectUrl = "/m/grant/activity/success.do?username=" + username + "&newcoins=";
        
        Activity activity = activityService.getActivity(activityCode);
        if (activity == null) {
            return jsonResponse.setResultCode(ResultCode.ACTIVITY_ERROR_ACTIVITY_NOT_EXISTS);
        } else if (!activity.isEffective()) {
            return jsonResponse.setResultCode(ResultCode.ACTIVITY_ERROR_ACTIVITY_NOT_EFFECTIVE);
        }

        User user = userService.getUserByAllWay(username);
    	
        if (user == null) {
        	String password = RandomStringUtils.randomNumeric(6);
        	long time = System.currentTimeMillis();
        	user = new User();
        	user.setUserName(username);
        	user.setUserPassword(DigestUtils.md5Hex(password));
        	user.setUserNickname(username);
        	user.setUserRelatedName(username);
        	user.setUserType(UserType.PHONE);
        	user.setStatus(UserStatus.NORMAL);
        	user.setCreateTime(time);
        	user.setUpdateTime(time);
        	user.setRegistrationSource(UserRegSource.WEB.getIntValue());
        	
        	registerFacade.addUser(user, null, activityCode, clientPlatform);
        	registerFacade.sendPhoneInitPassword(username, password);
        	
        	int coinNum = 999;
        	activity = activityService.getActivity("reg2step");
        	if (activity != null && activity.getGrantAmountRandom() > 0) {		
        		coinNum = activity.getGrantAmountRandom();
    		}
        	
        	newcoins = coinNum;
            redirectUrl += newcoins + "&rangecoins=" + coinNum;
        }
        else {

            UserCoinOperation operation = UserCoinOperation.ACTIVITY;
            long userId = user.getUserId();
            if (!activity.isUnlimit()) {
                int count = userCoinService.getUserCoinLogCountByRelatedId(userId, activityCode, operation);
                if (count >= activity.getLimitCount()) {
                	redirectUrl = "/m/grant/activity/out.do?count=" + count;
                }
                else {                    
                    long time = System.currentTimeMillis();
                    int grantAmount = RandomUtils.nextInt(activity.getGrantAmountMin(), activity.getGrantAmountMax());
                    
            		userCoinService.updateUserCoin(userId, 0, grantAmount, activityCode, time, operation, null, clientPlatform.getVersion());

                    newcoins = grantAmount;
                    redirectUrl += newcoins + "&rangecoins=" + newcoins;
                }
            } 
        }

        
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("redirectUrl", redirectUrl);

        return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping(value = "/activity/success", method = RequestMethod.GET)
    public String grantSuccess(@RequestParam("username") String username, @RequestParam("newcoins") String newcoins,@RequestParam("rangecoins") String rangecoins, Map<String, Object> model) {

    	model.put("rangecoins", rangecoins);
    	model.put("newcoins", newcoins);
    	model.put("phonetail", username.substring(username.length() - 4));
    	return "wap/ucar_success";
    }
    
    @RequestMapping(value = "/activity/out", method = RequestMethod.GET)
    public String grantOut(@RequestParam("count") String count, Map<String, Object> model) {
    	
    	model.put("count", count);
    	return "wap/ucar_out";
    }

    @RequestMapping(value = "/activity2Step", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse grantShare(@RequestParam("activityCode") String activityCode,
    		@RequestParam("username") String username, @RequestParam(value = "inviteCode", required = false) String inviteCode, ClientPlatform clientPlatform) {
        JsonResponse jsonResponse = new JsonResponse();
              
        Activity activity = activityService.getActivity(activityCode);
        if (activity == null) {
            return jsonResponse.setResultCode(ResultCode.ACTIVITY_ERROR_ACTIVITY_NOT_EXISTS);
        } else if (!activity.isEffective()) {
            return jsonResponse.setResultCode(ResultCode.ACTIVITY_ERROR_ACTIVITY_NOT_EFFECTIVE);
        }

        User user = userService.getUserByAllWay(username);
    	
        if (user == null) {
        	String password = RandomStringUtils.randomNumeric(6);
        	long time = System.currentTimeMillis();
        	user = new User();
        	user.setUserName(username);
        	user.setUserPassword(DigestUtils.md5Hex(password));
        	user.setUserNickname(username);
        	user.setUserRelatedName(username);
        	user.setUserType(UserType.PHONE);
        	user.setStatus(UserStatus.NORMAL);
        	user.setCreateTime(time);
        	user.setUpdateTime(time);
        	user.setRegistrationSource(UserRegSource.WEB.getIntValue());
        	registerFacade.addUser(user, inviteCode, activityCode, clientPlatform);
        	registerFacade.sendPhoneInitPassword(username, password);
        }
        else
        {
        	return jsonResponse.setResultCode(ResultCode.REGISTER_ERROR_USER_NAME_EXISTS);
        }

        return jsonResponse.setSuccessful();
    }
    
    @Login
    @RequestMapping(value = "/activity/scratchoff", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse scratchoff(@RequestParam("orderNo") String orderNo, UserDetail userDetail,ClientPlatform clientPlatform) {
        JsonResponse jsonResponse = new JsonResponse();
        
    	//确认刚刚成功下过订单 采用在支付订单成功的时候根据订单号生成唯一memoryCache Key(保存一天时间)  获取成功删除Key
        String groupKey = MemcachedKey.GROUP_KEY_ACTIVITY;
        Object obj = memcachedService.get(groupKey, orderNo);
        if (obj == null) {
        	return jsonResponse.setResultCode(ResultCode.ACTIVITY_ERROR_GRANT_INVALID);
        }
        
        memcachedService.remove(groupKey, orderNo);
    	 
    	String activityCode = "scratchoff";

        Activity activity = activityService.getActivity("scratchoff");

        long time = System.currentTimeMillis();
        UserCoinOperation operation = UserCoinOperation.ACTIVITY;
        int grantAmount = 0;
        if (RandomUtils.nextInt(0, 100) < 20)
        	grantAmount = RandomUtils.nextInt(activity.getGrantAmountMin(), activity.getGrantAmountMax() - 10);
        else
        	grantAmount = RandomUtils.nextInt(activity.getGrantAmountMax() - 10, activity.getGrantAmountMax());
        
        
    	userCoinService.updateUserCoin(userDetail.getUserId(), 0, grantAmount, activityCode, time, operation, null, clientPlatform.getVersion());
    	
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("grantAmount", grantAmount);
        
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping(value = "/activity/scratchshare", method = RequestMethod.GET)
    @ResponseBody
    @NoLogin
    public JsonResponse shareUrl(UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long userId = userDetail.getUserId();
    	Map<String, Object> result = new HashMap<String, Object>();
    	
        String title = "20元品牌时代来临，不用剁手买买买，还不上车？";
        String description = "下载俞姐姐客户端，品牌女装全场1折，靓衣20元抢不停，注册更送价值千元玖币！";
        String imageUrl = "/static/img/share-icon.png";
        String shareTitle = "规则说明";
        String shareRule = "分享链接单次点击可得50枚玖币";
        JSONArray jsonArrayConfirm = globalSettingService.getJsonArray(GlobalSettingName.AD_TITLES);
		for(Object obj : jsonArrayConfirm) {
			if(((JSONObject)obj).get("title") != null ){
				title = (String) ((JSONObject)obj).get("title");
			}
			if(((JSONObject)obj).get("description") != null){
				description = (String) ((JSONObject)obj).get("description");
			}
		    if(((JSONObject)obj).get("imageUrl") != null){
		    	imageUrl = (String) ((JSONObject)obj).get("imageUrl");
			}
		    if(((JSONObject)obj).get("shareTitle") != null){
		    	shareTitle = (String) ((JSONObject)obj).get("shareTitle");
			}
		    if(((JSONObject)obj).get("shareRule") != null){
		    	shareRule = (String) ((JSONObject)obj).get("shareRule");
			}
			
		}
        result.put("title", title);
        result.put("description", description);
        result.put("imageUrl", Constants.SERVER_URL + imageUrl);
        result.put("url", MobileGrantController.getUserShareUrl(userId));
        result.put("shareTitle", shareTitle);
        result.put("shareRule", shareRule);
    	
//    	result.put("title", "20元品牌时代来临，不用剁手买买买，还不上车？");
//        result.put("description", "下载俞姐姐客户端，品牌女装全场1折，靓衣20元抢不停，注册更送价值千元玖币！");
//        result.put("imageUrl", Constants.SERVER_URL + "/static/img/share-icon.png");
//        result.put("url", MobileGrantController.getUserShareUrl(userId));
//        result.put("shareTitle", "分享得玖币");
//        result.put("shareRule", "");

        return jsonResponse.setSuccessful().setData(result);
    }

    @RequestMapping(value = "/activity/scratchshare", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse grantShare(UserDetail userDetail, ClientPlatform clientPlatform) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        
    	String activityCode = "scratchshare";

        Activity activity = activityService.getActivity("scratchshare");

        long userId = userDetail.getUserId();
        long time = System.currentTimeMillis();
        UserCoinOperation operation = UserCoinOperation.ACTIVITY;

        int grantCount = 0;
        grantCount = activity.getGrantAmountRandom();
        userCoinService.updateUserCoin(userId, 0, grantCount, activityCode, time, operation, null, clientPlatform.getVersion());
        
        //分享朋友圈玖币数10
        int shareCoin = 10;
        data.put("grantCount", grantCount+shareCoin);

        return jsonResponse.setSuccessful().setData(data);
    }

}

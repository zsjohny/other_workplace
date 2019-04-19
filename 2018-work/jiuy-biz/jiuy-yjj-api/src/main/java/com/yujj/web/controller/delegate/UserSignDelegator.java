package com.yujj.web.controller.delegate;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.Activity;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserSign;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.UserSignFacade;
import com.yujj.business.service.ActivityService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.UserSignService;

@Service
public class UserSignDelegator {

    @Autowired
    private UserSignService userSignService;

    @Autowired
    private UserSignFacade userSignFacade;
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private GlobalSettingService globalSettingService;

    public JsonResponse signin(long userId, ClientPlatform clientPlatform) {
        JsonResponse jsonResponse = new JsonResponse();

        long time = System.currentTimeMillis();
        DateTime dateTime = new DateTime(time);
        int mondayTime = Integer.parseInt(dateTime.dayOfWeek().withMinimumValue().toString("yyyyMMdd"));

        Map<Integer, UserSign> userSignMap = userSignService.getUserSignOfWeek(userId, mondayTime);
        int weekDay = dateTime.getDayOfWeek();
        if (userSignMap.containsKey(weekDay)) {
            return jsonResponse.setResultCode(ResultCode.SIGN_ERROR_SIGNED);
        }

        int continueDays = 0;
        for (int i = weekDay - 1; i >= 1; i--) {
            if (!userSignMap.containsKey(i)) {
                break;
            }
            continueDays++;
        }

        JSONObject jiucoin_global_setting = globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING);
    	JSONObject signInSetting = jiucoin_global_setting.getJSONObject("signInSetting");
    	int dailyObtain = signInSetting.getInteger("dailyObtain");
        int grantCoins = dailyObtain + getGrantCoins(continueDays + 1);
        
        /****** 临时添加10.18过后即废弃该代码   *********/
    	long startTime = 0;
    	long endTime = 0;
    	try {
			startTime = DateUtil.parseStrTime2Long("2016-10-18 00:00:00");
			endTime = DateUtil.parseStrTime2Long("2016-10-18 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	long now = System.currentTimeMillis();
    	if(startTime < now && now < endTime) {
    		grantCoins = 1018;
    	}
    	/****** 临时添加10.18过后即废弃该代码   *********/
    	
        userSignFacade.signin(userId, dateTime, grantCoins ,clientPlatform.getVersion());

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("addCoins", grantCoins);
        return jsonResponse.setSuccessful().setData(data);
    }

    public JsonResponse getSignState(long userId) {
        JsonResponse jsonResponse = new JsonResponse();
        long time = System.currentTimeMillis();
        int dayTime = Integer.parseInt(DateUtil.format(time, "yyyyMMdd"));
        UserSign userSign = userSignService.getUserSign(userId, dayTime);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("signed", userSign != null);
        return jsonResponse.setSuccessful().setData(data);
    }

    public JsonResponse getSignInfo(long userId) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();

        long time = System.currentTimeMillis();
        DateTime dateTime = new DateTime(time);
        int weekDay = dateTime.getDayOfWeek();
        data.put("weekDay", weekDay);

        int mondayTime = Integer.parseInt(dateTime.dayOfWeek().withMinimumValue().toString("yyyyMMdd"));
        Map<Integer, UserSign> userSignMap = userSignService.getUserSignOfWeek(userId, mondayTime);
        int[] signStates = new int[7];
        for (int i = 1; i <= weekDay; i++) {
            if (userSignMap.containsKey(i)) {
                signStates[i - 1] = 1;
            }
        }
        data.put("signStates", signStates);

        int continueDays = 0;
        for (int i = weekDay - 1; i >= 1; i--) {
            if (!userSignMap.containsKey(i)) {
                break;
            }
            continueDays++;
        }
        /****** 临时添加10.18过后即废弃该代码   *********/
    	long startTime = 0;
    	long endTime = 0;
    	try {
			startTime = DateUtil.parseStrTime2Long("2016-10-17 00:00:00");
			endTime = DateUtil.parseStrTime2Long("2016-10-17 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	/****** 临时添加8.20过后即废弃该代码   *********/

    	JSONObject jiucoin_global_setting = globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING);
    	JSONObject signInSetting = jiucoin_global_setting.getJSONObject("signInSetting");
    	int dailyObtain = signInSetting.getInteger("dailyObtain");
    	
    	long now = System.currentTimeMillis();
    	if(startTime < now && now < endTime) {
    		data.put("tomorrowSignCoins", 1018);
    	} else {
    		data.put("tomorrowSignCoins", dailyObtain + getGrantCoins(continueDays + 2));
    	}
    	
    	//过了8.20恢复代码
//        data.put("tomorrowSignCoins", getGrantCoins(continueDays + 1));

        data.put("totalSignCount", userSignService.getTotalSignCount(userId));
        data.put("totalSignCoins", userSignService.getTotalSignCoins(userId));
        
        data.put("rule", signInSetting);

        return jsonResponse.setSuccessful().setData(data);
    }

    private int getGrantCoins(int continueDays) {
    	//old: 20  20  50  100
    	
    	JSONObject jiucoin_global_setting = globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING);
    	JSONObject signInSetting = jiucoin_global_setting.getJSONObject("signInSetting");
    	JSONArray continuousSetting = signInSetting.getJSONArray("continuousSetting");
    	for (Object object : continuousSetting) {
			JSONObject item = (JSONObject)object;
			if (item.getInteger("days") == continueDays) {
				return item.getInteger("extraJiuCoin");
			}
    	}
    	return 0;
    	
        /*int signDaily = 100;
        int signTwice = 100;
        int sign4times = 250;
        int signWeek = 500;
        int grantCoins = signDaily;
        
        Activity activity = activityService.getActivity("signDaily");
    	if (activity != null ) {
    		signDaily = activity.getGrantAmountRandom();
		}
    	activity = activityService.getActivity("signTwice");
    	if (activity != null ) {
    		signTwice = activity.getGrantAmountRandom();
		}
    	activity = activityService.getActivity("sign4times");
    	if (activity != null ) {
    		sign4times = activity.getGrantAmountRandom();
		}
    	activity = activityService.getActivity("signWeek");
    	if (activity != null ) {
    		signWeek = activity.getGrantAmountRandom();
		}
    	
        switch (continueDays) {
            case 1:
                grantCoins += signTwice;
                break;
            case 3:
                grantCoins += sign4times;
       //         grantCoins += 8;
                break;
            case 6:
                grantCoins += signWeek;
      //          grantCoins += 15;
                break;
        }
        return grantCoins;*/
    }
    
//    private int getGrantCoins(int continueDays) {
//    	//old: 20  20  50  100
//    	int grantCoins = 20;
//    	switch (continueDays) {
//    	case 1:
//    		grantCoins += 20;
//    		break;
//    	case 3:
//    		grantCoins += 50;
//    		//         grantCoins += 8;
//    		break;
//    	case 6:
//    		grantCoins += 100;
//    		//          grantCoins += 15;
//    		break;
//    	}
//    	return grantCoins;
//    }

}

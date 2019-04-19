package com.yujj.business.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.UserFetchGiftStatus;
import com.jiuyuan.entity.UserGiftLog;
import com.yujj.dao.mapper.UserGiftLogMapper;

@Service
public class UserGiftLogService {
	
	@Autowired
	private UserGiftLogMapper userGiftLogMapper;
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	public UserFetchGiftStatus getFetchStatus(Long userId) {
		JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.GIFT_CONTENT);
		JSONArray jsonArray = (JSONArray) jsonObject.get("content");
		if (jsonArray == null) {
			return UserFetchGiftStatus.NO_GIFT;
		}
		Long startTime = Long.parseLong(jsonObject.get("start_time").toString());
		Long endTime = Long.parseLong(jsonObject.get("end_time").toString());
		
		long currentTime = System.currentTimeMillis();
		if (startTime > currentTime || endTime < currentTime) {
			return UserFetchGiftStatus.NO_GIFT;
		}
		
		UserGiftLog userGiftLog = userGiftLogMapper.searchOne(userId, startTime, endTime);
		if (userGiftLog == null) {
			return UserFetchGiftStatus.UNFETCHED;
		} else {
			return UserFetchGiftStatus.FETCHED;
		}
	
	}


}

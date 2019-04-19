package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 社区管理
 * @author Jeff.Zhan
 *
 */
@RequestMapping("/community")
@Controller
public class CommunityController {

	@Autowired
	private GlobalSettingService globalSettingService;
	
	@RequestMapping("/global")
	@ResponseBody
	public JsonResponse getGlobalInfo() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();
		
		JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.COMMUNITY_CAROUSEL);
		data.put("community_carousel", jsonObject);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/navigation")
	@ResponseBody
	public JsonResponse getNavigationInfo() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();
		
		JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.COMMUNITY_NAVIGATION);
		data.put("communityNavigation", jsonObject);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
}

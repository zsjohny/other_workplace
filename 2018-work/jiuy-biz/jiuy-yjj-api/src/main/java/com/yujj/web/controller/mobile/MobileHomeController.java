package com.yujj.web.controller.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.FloorType;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.ActivityPlace;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.HomeFacade;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.StatisticsService;
import com.yujj.dao.mapper.ActivityPlaceMapper;
import com.yujj.entity.account.UserDetail;

@Controller
@RequestMapping("/mobile/home")
public class MobileHomeController {
	
	@Autowired
	private HomeFacade homeFacade;
	
	@Autowired
	private StatisticsService statisticsService;
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	@Autowired
	private ActivityPlaceMapper activityPlaceMapper;
	
	@RequestMapping
	@ResponseBody
	public JsonResponse homeFloor(PageQuery pageQuery) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		List<JSONObject> modules = new ArrayList<JSONObject>();
		
//        int totalCount = homeFacade.getHomeFloorCount();
//        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
//        data.put("pageQuery", pageQueryResult);
//		modules = homeFacade.getJsonListBefore185(pageQuery);
		
		int totalCount = homeFacade.getHomeFloorCount187(FloorType.ACTIVITY_PLACE, 0L);
		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
		data.put("pageQuery", pageQueryResult);
		modules = homeFacade.getJsonList187(pageQuery, FloorType.ACTIVITY_PLACE, 0L);
		
		data.put("modules", modules);
		return jsonResponse.setData(data);
	}
	
	@RequestMapping("/show185")
	@ResponseBody
	public JsonResponse homeFloor185(PageQuery pageQuery) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		List<JSONObject> modules = new ArrayList<JSONObject>();
		
//		int totalCount = homeFacade.getHomeFloorCount185();
//		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
//		data.put("pageQuery", pageQueryResult);
//		modules = homeFacade.getJsonList(pageQuery);

		int totalCount = homeFacade.getHomeFloorCount187(FloorType.ACTIVITY_PLACE, 0L);
		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
		data.put("pageQuery", pageQueryResult);
		modules = homeFacade.getJsonList187(pageQuery, FloorType.ACTIVITY_PLACE, 0L);
		
		data.put("modules", modules);
		return jsonResponse.setData(data);
	}
	
	/**************************** yjj_HomeFloor2 *******************/
	/**
	 * 活动专场
	 * @param activityPlaceId
	 * @param pageQuery
	 * @return
	 */
	@RequestMapping("/activity/place/show186")
	@ResponseBody
	public JsonResponse homeFloor186(@RequestParam(value = "activity_place_id") Long activityPlaceId, 
			PageQuery pageQuery) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		List<JSONObject> modules = new ArrayList<JSONObject>();
		if(activityPlaceId == null){
			activityPlaceId = (long) 0;
		}
		int totalCount = homeFacade.getHomeFloorCount187(FloorType.ACTIVITY_PLACE, activityPlaceId);
		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
		data.put("pageQuery", pageQueryResult);
		
		modules = homeFacade.getJsonList187(pageQuery, FloorType.ACTIVITY_PLACE, activityPlaceId);
		data.put("modules", modules);
		
		ActivityPlace activityPlace =  activityPlaceMapper.getById(activityPlaceId);
		data.put("activity_place_name", activityPlace != null ? activityPlace.getName() : "");
		
		
		return jsonResponse.setData(data);
	}
	
	/**
	 * 首页模板数据加载
	 * @param pageQuery
	 * @return
	 */
	@RequestMapping("/show187")
	@ResponseBody
	public JsonResponse homeFloor186(PageQuery pageQuery) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		List<JSONObject> modules = new ArrayList<JSONObject>();
		
		int totalCount = homeFacade.getHomeFloorCount187(FloorType.ACTIVITY_PLACE, 0L);
		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
		data.put("pageQuery", pageQueryResult);
		
		modules = homeFacade.getJsonList187(pageQuery, FloorType.ACTIVITY_PLACE, 0L);
		
    	data.put("tipList", globalSettingService.getJsonObject(GlobalSettingName.SEARCH_TIP));
		data.put("modules", modules);
		
		return jsonResponse.setData(data);
	}
	
	/**
	 * 积分商城
	 * @param pageQuery
	 * @return
	 */
	@RequestMapping("/pointMall")
	@ResponseBody
	public JsonResponse pointMall(PageQuery pageQuery) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		List<JSONObject> modules = new ArrayList<JSONObject>();
		
		int totalCount = homeFacade.getHomeFloorCount187(FloorType.POINT_MALL, 0L); 
		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
		data.put("pageQuery", pageQueryResult);
		
		modules = homeFacade.getJsonList187(pageQuery, FloorType.POINT_MALL, 0L);
		
//		data.put("tipList", globalSettingService.getJsonObject(GlobalSettingName.SEARCH_TIP));
		data.put("modules", modules);
		
		return jsonResponse.setData(data);
	}
	
	@RequestMapping("/show/content")
	@ResponseBody
	public JsonResponse homeFloor186(PageQuery pageQuery, 
			@RequestParam("string_type") String stringType,
			@RequestParam("related_id") Long relatedId) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		List<JSONObject> modules = new ArrayList<JSONObject>();
		
		FloorType floorType = FloorType.getByStringValue(stringType);
		if (floorType == null) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("类型描述字符串不存在对应类型！");
		}
		
		int totalCount = homeFacade.getHomeFloorCount187(floorType, relatedId);
		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
		data.put("pageQuery", pageQueryResult);
		
		modules = homeFacade.getJsonList187(pageQuery, floorType, relatedId);
		
		data.put("modules", modules);
		return jsonResponse.setData(data);
	}
	
	
	@RequestMapping("/click/statistics")
	@ResponseBody
	public JsonResponse statisticsClick(UserDetail userDetail, @RequestParam("statistics_id") Long statisticsId) {
		JsonResponse jsonResponse = new JsonResponse();
		if (userDetail.getUser() != null) {
			statisticsService.updateUserClick(statisticsId);
		} else {
			statisticsService.updateUnKnownClick(statisticsId);
		}
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
}

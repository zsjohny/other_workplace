package com.jiuy.store.api.tool.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.FloorType;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.HomeFacade;

@Controller
@RequestMapping("/mobile/home")
public class MobileHomeController {
	   private static final Logger logger = Logger.getLogger(MobileHomeController.class);
	@Autowired
	private HomeFacade homeFacade;
	
	/**
	 * 分类下的字列表
	 * @param pageQuery
	 * @param stringType
	 * @param relatedId
	 * @return
	 */
	@RequestMapping("/show/content")
	@ResponseBody
	@Cacheable("cache")
	public JsonResponse homeFloor186(PageQuery pageQuery, 
			@RequestParam("string_type") String stringType,
			@RequestParam("related_id") Long relatedId) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		List<JSONObject> modules = new ArrayList<JSONObject>();
		
		FloorType floorType = FloorType.getByStringValue(stringType);
		if (floorType == null) {
			logger.info("floorType不能为空");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("类型描述字符串不存在对应类型！");
		}
		
		int totalCount = homeFacade.getHomeFloorCount187(floorType, relatedId);
		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
		data.put("pageQuery", pageQueryResult);
		
		modules = homeFacade.getJsonList187(pageQuery, floorType, relatedId);
		
		data.put("modules", modules);
		return jsonResponse.setSuccessful().setData(data);
	}	
	
}
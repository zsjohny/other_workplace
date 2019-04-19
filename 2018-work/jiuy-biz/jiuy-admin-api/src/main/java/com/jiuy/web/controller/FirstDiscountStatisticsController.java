package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.dao.FirstDiscountStatisticsDao;
import com.jiuy.core.service.FirstDiscountStatisticsService;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.entity.FirstDiscountStatisticsDayBean;
import com.jiuyuan.entity.GlobalSetting;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

/**
* @author WuWanjian
* @version 创建时间: 2017年4月6日 上午9:49:08
*/

@Controller
@RequestMapping("/firstDiscountStatistics")
@Login
public class FirstDiscountStatisticsController {
	
	@Resource
	private FirstDiscountStatisticsService firstDiscountStatisticsService;
	
	@Autowired
	private FirstDiscountStatisticsDao firstDiscountStatisticsDao;
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	@RequestMapping(value="/dayStatistics")
	@ResponseBody	
	public JsonResponse dayStatistics(){
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();
		
		List<FirstDiscountStatisticsDayBean> list = firstDiscountStatisticsService.searchDayStatistics();
		data.put("list", list);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	//时间区间查询
	@RequestMapping(value="/timeIntervalStatistics")
	@ResponseBody
	public JsonResponse timeIntervalStatistics(@RequestParam(value="start_time",required = false,defaultValue="0")long startTime,
			@RequestParam(value="end_time",required = false, defaultValue="0")long endTime){
		Map<String, Object> data = new HashMap<String, Object>();
		JsonResponse jsonResponse = new JsonResponse();
		
		if(endTime == 0){
			endTime = System.currentTimeMillis();
		}
		
		FirstDiscountStatisticsDayBean resultData = firstDiscountStatisticsService.timeIntervalStatistics(startTime, endTime);
		data.put("data", resultData);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 查询首单优惠记录列表
	 * @param startTime
	 * @param endTime
	 * @param orderNo
	 * @param yjjNumber
	 * @param minMoney
	 * @param maxMoney
	 * @return
	 */
	@RequestMapping(value="/record")
	@ResponseBody
	public JsonResponse searchRecord(@RequestParam(value="start_time",required = false, defaultValue="0")long startTime,
			@RequestParam(value="end_time", required = false, defaultValue="0")long endTime,
			@RequestParam(value="order_no", required = false, defaultValue="0")long orderNo,
			@RequestParam(value="yjjnumber",required = false, defaultValue="")String yjjNumber,
			@RequestParam(value="min_money",required = false, defaultValue ="0")double minMoney,
			@RequestParam(value="max_money",required = false, defaultValue = "-1")double maxMoney,
			PageQuery pageQuery){
		JsonResponse jsonResponse = new JsonResponse();
		
		if(endTime == 0){
			endTime = System.currentTimeMillis();
		}
		
		Map<String, Object> data = firstDiscountStatisticsService.searchRecord(orderNo, yjjNumber, minMoney, maxMoney, startTime, endTime,pageQuery);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value = "/setting/update")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse add(@RequestParam(value = "property_value") String propertyValue,
			@RequestParam(value = "group_id", required = false, defaultValue = "0") int groupId,
			@RequestParam(value = "group_value", required = false, defaultValue = "") String groupName,
			@RequestParam(value = "description", required = false, defaultValue = "") String description){
		JsonResponse jsonResponse = new JsonResponse();
		
		GlobalSetting globalSetting = new GlobalSetting();
		globalSetting.setPropertyName(GlobalSettingName.FIRST_DISCOUNT_SETTING.getStringValue());
		globalSetting.setPropertyValue(propertyValue);
		globalSetting.setGroupId(groupId);
		globalSetting.setGroupName(groupName);
		globalSetting.setDescription(description);
		
		globalSettingService.add(globalSetting);
		
		return jsonResponse.setSuccessful();
	}
	
	@RequestMapping(value = "/setting/load")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse settingLoad(){
		JsonResponse jsonResponse = new JsonResponse();
		HashMap<String, Object> data = new HashMap<String,Object>();
		
		String setting = globalSettingService.getSetting(GlobalSettingName.getByStringValue(GlobalSettingName.FIRST_DISCOUNT_SETTING.getStringValue()));
		JSONObject parseObject = JSON.parseObject(setting);
		data.put("setting", parseObject);
		
		return jsonResponse.setSuccessful().setData(data);
	}
}

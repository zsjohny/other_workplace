package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.service.ShopPropertyValueDelegator;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyWithValue;
import com.jiuyuan.util.BizUtil;
import com.util.LocalMapUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jiuy.core.service.ActivityPlaceService;
import com.jiuyuan.entity.ActivityPlace;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@RequestMapping("/activity/place")
@Controller
@Login
public class ActivityPlaceController {
	private static Logger logger = Logger.getLogger(ActivityPlaceController.class);
	@Autowired
	private ActivityPlaceService activityPlaceService;
	@Autowired
	private ShopPropertyValueDelegator shopPropertyValueDelegator;


	@RequestMapping(value = "/add")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse add(@RequestParam(value = "name") String name,
			@RequestParam(value = "description", required = false, defaultValue = "") String description) {
		JsonResponse jsonResponse = new JsonResponse();
		logger.info("添加模板name："+name+",description:"+description);
		if(StringUtils.isEmpty(description)){
			description = "未填写模板描述";
		}
		logger.info("添加模板name："+name+",description:"+description);
		activityPlaceService.add(name, description);
		return jsonResponse.setSuccessful();
	}
	
	@RequestMapping(value = "/update")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse update(@RequestParam(value = "id") long id,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "description") String description){
		JsonResponse jsonResponse = new JsonResponse();
		
		activityPlaceService.update(id, name, description);
		
		return jsonResponse.setSuccessful();
	}
	
	@RequestMapping(value = "/search")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse search(@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "type", required = true) int type,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "page_size", required = false, defaultValue = "10") int pageSize) {
		
		logger.info("搜索模板name："+name+",type:"+type+",page:"+page+",page_size:"+pageSize);
		JsonResponse jsonResponse = new JsonResponse();
		PageQuery pageQuery = new PageQuery(page, pageSize);
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);
		
		Map<String, Object> data = new HashMap<String, Object>();
		List<ActivityPlace> activityPlaces = activityPlaceService.search(name, type,pageQuery);
		int count = activityPlaceService.searchCount(name,type);
		pageQueryResult.setRecordCount(count);
		
		data.put("list", activityPlaces);
		data.put("total", pageQueryResult);
		logger.info("搜索模板data:"+JSON.toJSONString(data)+"name："+name+",type:"+type+",page:"+page+",page_size:"+pageSize);
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 删除专场
	 * @param activityPlaceId
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse delete(@RequestParam(value = "activityPlaceId") long activityPlaceId){
		JsonResponse jsonResponse = new JsonResponse();
		try {
			int record = activityPlaceService.delete(activityPlaceId);
			if(record!=1){
				return jsonResponse.setError("删除专场错误：activityPlaceId："+activityPlaceId);
			}
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			return jsonResponse.setError("删除专场:"+e.getMessage());
		}
	}
	
	/**
	 * 恢复专场
	 * @param activityPlaceId
	 * @return
	 */
	@RequestMapping(value = "/restore")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse restore(@RequestParam(value = "activityPlaceId") long activityPlaceId){
		JsonResponse jsonResponse = new JsonResponse();
		try {
			int record = activityPlaceService.restore(activityPlaceId);
			if(record!=1){
				return jsonResponse.setError("恢复专场错误：activityPlaceId："+activityPlaceId);
			}
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			return jsonResponse.setError("恢复专场:"+e.getMessage());
		}
	}


	/**
	 * 查询categoryId及其子孙类的所有PropertyValue
	 * @param categoryId
	 * @return
	 * @Author Charlie(唐静)
	 * @Date 18/05/10
	 */
	@RequestMapping( "/dPropertyByCategory" )
	@ResponseBody
	public JsonResponse propertyValueByCategory(Long categoryId) {

		try {
			List<DynamicPropertyWithValue> datas = shopPropertyValueDelegator.propertyValueByCategory(categoryId);
			return JsonResponse.getInstance().setSuccessful().setData(datas);
		} catch (Exception e) {
			e.printStackTrace();
			return BizUtil.exceptionHandler(e);
		}
	}
}
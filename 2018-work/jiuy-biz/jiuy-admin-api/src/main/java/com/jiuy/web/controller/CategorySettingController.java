package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.service.category.CategorySettingService;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.CategorySetting;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/setting/category")
public class CategorySettingController {
	
	@Autowired
	private CategorySettingService categorySettingService;
	
	@RequestMapping("/search")
	@ResponseBody
	public JsonResponse search(PageQuery pageQuery, @RequestParam(value = "name", required = false) String name) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		int recordCount = categorySettingService.searchCount(name);
		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, recordCount);
		
		List<CategorySetting> categorySettings = categorySettingService.search(pageQuery, name);
		
		data.put("list", categorySettings);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public JsonResponse add(@RequestBody CategorySetting categorySetting) {
		JsonResponse jsonResponse = new JsonResponse();
		
		categorySettingService.add(categorySetting);
		
		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping("/{id}/remove")
	@ResponseBody
	public JsonResponse remove(@PathVariable("id") Long id) {
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			categorySettingService.remove(id);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping("/{id}/detail")
	@ResponseBody
	public JsonResponse detail(@PathVariable("id") Long id) {
		JsonResponse jsonResponse = new JsonResponse();
		
		CategorySetting categorySetting = categorySettingService.search(id);
		Map<String, Object> data = new HashMap<>();
		data.put("categorySetting", categorySetting);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public JsonResponse update(@RequestBody CategorySetting categorySetting) {
		JsonResponse jsonResponse = new JsonResponse();
		
		categorySettingService.update(categorySetting);
		
		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping("/{id}/content/update")
	@ResponseBody
	public JsonResponse contentUpdate(@PathVariable("id") Long id, @RequestParam("content") String content) {
		JsonResponse jsonResponse = new JsonResponse();
		
		categorySettingService.update(id, content);
		
		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping("/noPageQuery/search")
	@ResponseBody
	public JsonResponse contentUpdate(@RequestParam("link_type") Integer linkType) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<CategorySetting> categorySettings = categorySettingService.search(linkType);
		data.put("list", categorySettings);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
}

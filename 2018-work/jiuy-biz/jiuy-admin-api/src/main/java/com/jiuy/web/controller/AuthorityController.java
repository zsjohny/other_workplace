package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.dao.mapper.AuthorityDao;
import com.jiuy.core.meta.admin.Authority;
import com.jiuy.core.meta.admin.AuthorityVO;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.web.help.JsonResponse;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@RequestMapping("/authority")
@Controller
public class AuthorityController {
	
	@Autowired
	private AuthorityDao authorityDao;
	
	@RequestMapping("/nopagequery/search")
	@ResponseBody
	public JsonResponse seach(@RequestParam(value = "parent_id", required = false) Long parentId,
			@RequestParam(value = "module_name", required = false) String moduleName) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();
		
		List<AuthorityVO> authorities = authorityDao.searchVO(null, parentId, moduleName);
		
		data.put("list", authorities);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/search")
	@ResponseBody
	public JsonResponse seach(PageQuery pageQuery, 
			@RequestParam(value = "parent_id", required = false) Long parentId,
			@RequestParam(value = "module_name", required = false) String moduleName) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();
		
		int recordCount = authorityDao.searchCount(parentId, moduleName);
		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, recordCount);
		
		List<AuthorityVO> authorities = authorityDao.searchVO(pageQuery, parentId, moduleName);
		
		data.put("list", authorities);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/remove")
	@ResponseBody
	public JsonResponse remove(@RequestParam("id") Long id) {
		JsonResponse jsonResponse = new JsonResponse();
		
		authorityDao.remove(id);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public JsonResponse add(@RequestParam("module_name") String moduleName,
			@RequestParam("parent_id") Long parentId,
			@RequestParam("url") String url,
			@RequestParam(value = "menu_name", required = false) String menuName,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "weight", required = false, defaultValue = "0") int weight ) {
		JsonResponse jsonResponse = new JsonResponse();
		long time = System.currentTimeMillis();
		Authority authority = new Authority(null, moduleName, parentId, url, menuName, description, weight);
		authority.setCreateTime(time);
		authority.setUpdateTime(time);
		
		authorityDao.add(authority);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public JsonResponse update(@RequestParam("id") Long id,
			@RequestParam("module_name") String moduleName,
			@RequestParam("parent_id") Long parentId,
			@RequestParam("url") String url,
			@RequestParam(value = "menu_name", required = false) String menuName,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "weight", required = false, defaultValue = "0") int weight) {
		JsonResponse jsonResponse = new JsonResponse();
		long time = System.currentTimeMillis();
		Authority authority = new Authority(id, moduleName, parentId, url, menuName, description, weight);
		authority.setUpdateTime(time);
		
		authorityDao.update(authority);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}

}

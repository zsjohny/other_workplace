package com.jiuy.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.business.facade.RoleFacade;
import com.jiuy.core.dao.AdminUserDao;
import com.jiuy.core.dao.mapper.RoleDao;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.admin.Authority;
import com.jiuy.core.meta.admin.RoleVO;
import com.jiuy.core.service.admin.RoleAuthorityService;
import com.jiuy.core.service.admin.RoleService;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@RequestMapping("/role")
@Controller
public class RoleAuthorityController {
	
	@Resource
	private RoleAuthorityService roleAuthorityServiceImpl;
	
	@Autowired
	private RoleFacade roleFacade;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private AdminUserDao adminUserDao;
	
	@RequestMapping("/search")
	@ResponseBody
	public JsonResponse search(PageQuery pageQuery,
				@RequestParam(value = "name", required = false) String name) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();
		
		int recordCount = roleService.searchCount(name);
		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, recordCount);
		List<RoleVO> roles = roleFacade.search(pageQuery, name);

		data.put("total", pageQueryResult);
		data.put("list", roles);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/search/noquery")
	@ResponseBody
	public JsonResponse searchNoQuery(PageQuery pageQuery,
				@RequestParam(value = "name", required = false) String name) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();
		List<RoleVO> roles = roleFacade.search(null, name);
		data.put("list", roles);
		
		return jsonResponse.setSuccessful().setData(data);
	}

	@AdminOperationLog
	@RequestMapping("/add")
	@ResponseBody
	public JsonResponse add(@RequestParam("name") String name,
			@RequestParam(value = "description", required = false) String description) {
		JsonResponse jsonResponse = new JsonResponse();

		if (roleDao.getByName(name).size() > 0) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("角色名字已存在！");
		}
		
		roleDao.add(name , description);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@AdminOperationLog
	@RequestMapping("/{id}/update")
	@ResponseBody
	public JsonResponse update(@PathVariable("id") Long id, 
			@RequestParam("name") String name,
			@RequestParam("description") String description) {
		JsonResponse jsonResponse = new JsonResponse();
		
		roleDao.update(id, name , description);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@AdminOperationLog
	@RequestMapping("/{id}/remove")
	@ResponseBody
	public JsonResponse update(@PathVariable("id") Long id) {
		JsonResponse jsonResponse = new JsonResponse();
		
		roleDao.update(id, -1);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	/*
	'data' : [{
	"id":1,"text":"Root node","children":[
	  {"id":11,"text":"Child node 11",
	  "children":[{"id":111,"text":"Child node 111","state":{"selected":1}},{"id":112,"text":"Child node 112"}]},
	  {"id":12,"text":"Child node 12"}
		]
	},
	{
	"id":2,"text":"Root node","children":[
	  {"id":21,"text":"Child node 21",
	  "children":[{"id":211,"text":"Child node 211"},{"id":212,"text":"Child node 212"}]},
	  {"id":22,"text":"Child node 22"}
		]
	}]
*/
	void getTreeJSON(JSONArray root, Collection<Authority> authorities) {
				
		for (Authority authority : authorities) {

			JSONObject node = new JSONObject();
			node.put("id", authority.getId());
			node.put("text", authority.getModuleName());

			int selected = 0;
			
			List<Authority> authorityList = authority.getAuthorities();
			if (authorityList.size() > 0) {
				JSONArray children = new JSONArray();
				getTreeJSON(children, authorityList);
				node.put("children", children);
				selected = 0;
			} else {
				selected = authority.getSelected();
			}
			
			JSONObject state = new JSONObject();
			state.put("selected", selected);
			state.put("opened", true);
			node.put("state", state);
			root.add(node);
		}
		
		return;
	}
	
//	@AdminOperationLog
	@RequestMapping(value="/authority/load")
	@ResponseBody
	public JsonResponse load(@RequestParam(value = "role_id", required = false) Long roleId) {
		JsonResponse jsonResponse = new JsonResponse();
		
		Collection<Authority> authorities = roleFacade.loadAuthority(roleId);
		
		JSONArray root = new JSONArray();
		getTreeJSON(root, authorities);
		
		return jsonResponse.setSuccessful().setData(root);
	}
	
//	@AdminOperationLog
	@RequestMapping(value="/specify/authority/load")
	@ResponseBody
	public JsonResponse specifyLoad(@RequestParam("role_id") Long roleId, @RequestParam("parent_id") Long parentId) {
		JsonResponse jsonResponse = new JsonResponse();
		
		Collection<Authority> authorities = roleFacade.getSubAuthorities(roleId, parentId);
		
		return jsonResponse.setSuccessful().setData(authorities);
	}
	
	/*
	 * 添加权限
	 * @Param authority_ids: 以逗号隔开
	 */
	@AdminOperationLog
	@RequestMapping(value="/authority/add")
	@ResponseBody
	public JsonResponse addAuthority(@RequestParam("role_id") long roleId, 
									@RequestParam("authority_ids") String authorityIdsStr) {
		JsonResponse jsonResponse = new JsonResponse();
		
		List<String> authorityIdStrs = Arrays.asList(authorityIdsStr.split(","));
		List<Long> authorityIds = new ArrayList<>();
		for (String string : authorityIdStrs) {
			authorityIds.add(Long.parseLong(string));
		}
		
		roleAuthorityServiceImpl.add(roleId, authorityIds);

		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	Collection<Authority> trimAuthority(Collection<Authority> authorityList) {
		Collection<Authority> authorities = new ArrayList<Authority>();
		
		for (Authority authority : authorityList) {
			if (authority.getDisplayed() == 1) authorities.add(authority);				
		}
		
		return authorities;
	}
	
//	@AdminOperationLog
	@RequestMapping(value="/user/authority/load")
	@ResponseBody
	@Login
	public JsonResponse load(HttpServletRequest request, @RequestParam(value = "parent_id", required = false) Long parentId) {
		JsonResponse jsonResponse = new JsonResponse();
		HttpSession session = request.getSession();
		Long userId = Long.parseLong(session.getAttribute("userid").toString());
		AdminUser adminUser = adminUserDao.getUser(userId);
		
		Collection<Authority> authorities = null;
		if (parentId == null) {
			authorities = trimAuthority(roleFacade.loadAuthority(adminUser.getRoleId()));
		} else {
			authorities = roleFacade.getSubAuthorities(adminUser.getRoleId(), parentId);
		}

		return jsonResponse.setSuccessful().setData(authorities);
	}
	
}

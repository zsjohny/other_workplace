package com.jiuy.web.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.AdminUserFacade;
import com.jiuy.core.meta.admin.AdminLog;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.admin.AdminUserVO;
import com.jiuy.core.service.admin.AdminLogService;
import com.jiuy.core.service.admin.AdminUserService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/admin")
@Login
public class AdminUserController {
	
	@Autowired
	private AdminUserService adminUserService;
	
    @Autowired
    private AdminUserFacade adminUserFacade;    

    @Autowired 
	private AdminLogService alService;

    @RequestMapping("/search")
	@ResponseBody
	public JsonResponse search(PageQuery pageQuery,
			@RequestParam(value = "user_id", required = false) Long userId, 
			@RequestParam(value = "user_name", required = false) String userName, 
			@RequestParam(value = "role_id", required = false) Long roleId,
			@RequestParam(value = "user_phone", required = false) String userPhone) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		PageQueryResult queryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, queryResult);
		
		List<AdminUserVO> list = adminUserFacade.search(pageQuery, userId, userName, roleId, userPhone);
		int count = adminUserService.searchCount(userId, userName, roleId, userPhone);
        queryResult.setRecordCount(count);
        
		data.put("list", list);
		data.put("total", queryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}

    @AdminOperationLog
	@RequestMapping("/add")
	@ResponseBody
	public JsonResponse add(@RequestParam(value = "user_name") String userName, 
			@RequestParam(value = "role_id") Long roleId, 
			@RequestParam(value = "user_real_name") String userRealName,
			@RequestParam(value = "user_department") String userDepartment,
			@RequestParam(value = "user_job") String userJob,
			@RequestParam(value = "user_phone") String userPhone) {
		JsonResponse jsonResponse = new JsonResponse();
		
		AdminUser adminUser = new AdminUser(userName, roleId, userRealName, userDepartment, userJob, userPhone);
		
		adminUserService.add(adminUser);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}

    @AdminOperationLog
	@RequestMapping("/update")
	@ResponseBody
	public JsonResponse updateUser(@RequestParam("user_id") long userId, 
			@RequestParam("role_id") long roleId, 
			@RequestParam(value = "user_phone", required = false) String userPhone) {
		JsonResponse jsonResponse = new JsonResponse();
		
		adminUserService.update(userId, roleId, userPhone);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}

    @AdminOperationLog
    @RequestMapping("/remove")
	@ResponseBody
	public JsonResponse removeUser(@RequestParam("user_id") long userId) {
		JsonResponse jsonResponse = new JsonResponse();
		
		adminUserService.remove(userId);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}

    @AdminOperationLog
    @RequestMapping("/reset")
    @ResponseBody
    public JsonResponse resetPassword(@RequestParam(value = "user_id") long userId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	adminUserService.resetPassword(userId);
    	
    	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
    }
    
	
    @RequestMapping(value = "/searchAdminLog")
	@ResponseBody
	public JsonResponse searchAdminLog(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "page_size", required = false, defaultValue = "20") int pageSize,
			@RequestParam(value = "user_id", defaultValue = "-1", required = false) long userId, 
			@RequestParam(value = "user_name", defaultValue = "", required = false) String userName, 
			@RequestParam(value = "operate_model", defaultValue = "", required = false) String operateModel, // 用户操作模块
			@RequestParam(value = "operate_content", defaultValue = "", required = false) String operateContent, // 用户操作内容  
			@RequestParam(value="starttime", required=false, defaultValue = "1970-1-1 00:00:00")String startTime,
			@RequestParam(value="endtime", required=false, defaultValue="") String endTime) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		PageQuery query = new PageQuery(page, pageSize);
		PageQueryResult queryResult = new PageQueryResult();
		BeanUtils.copyProperties(query, queryResult);
		
		AdminLog adminLog = new AdminLog();
		adminLog.setUserId(userId);
		adminLog.setUserName(userName);
		adminLog.setOperateModel(operateModel);
		adminLog.setOperateContent(operateContent);
		
    	long endTimeL = 0L;
    	long startTimeL = 0L;
    	try {
			startTimeL = DateUtil.convertToMSEL(startTime);
			if(StringUtils.equals(endTime, "")) {
				endTimeL = System.currentTimeMillis();
			} else {
				endTimeL = DateUtil.convertToMSEL(endTime);
			}
		} catch (ParseException e) {
			jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("startTime:" + startTime + " endTime:" + endTime);
		}
    	
		List<AdminLog> list = alService.searchAdminLog(adminLog, startTimeL, endTimeL, queryResult);
		int count = alService.searchAdminLogCount(adminLog, startTimeL, endTimeL);
        queryResult.setRecordCount(count);
        
		data.put("adminLogs", list);
		data.put("total", queryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}    
    
}

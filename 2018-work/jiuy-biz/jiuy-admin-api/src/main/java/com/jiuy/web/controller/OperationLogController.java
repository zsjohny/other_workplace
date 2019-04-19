package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.meta.operationLog.OperationLogVO;
import com.jiuy.core.service.operationLog.OperationLogService;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/operationLog")
public class OperationLogController {
	
    public final int PAGE_SIZE = 2;
	
	@Resource
	private OperationLogService operationLogServiceImpl;
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
    @AdminOperationLog
	public String operationLogPage() {
		return "page/backend/OperationLogManagement";
	}
	
	@RequestMapping(value="/search")
	@ResponseBody
	public JsonResponse search(@RequestParam(value="page", required=false, defaultValue="1") int page,
			@RequestParam(value="user_id", required=false, defaultValue="-1") long userId,
			@RequestParam(value="user_name", required=false, defaultValue="") String userName,
			@RequestParam(value="work_no", required=false, defaultValue="") String workNo,
			@RequestParam(value="role_id", required=false, defaultValue="-1") int roleId,
			@RequestParam(value="start_time", required=false, defaultValue="1970-1-1 00:00:00") String startTime,
			@RequestParam(value="end_time", required=false, defaultValue="") String endTime) {
		Map<String, Object> data = new HashMap<String, Object>();
		OperationLogVO op = new OperationLogVO();
		JsonResponse jsonResponse = new JsonResponse();
		PageQuery query = new PageQuery(page, PAGE_SIZE);
		PageQueryResult queryResult = new PageQueryResult();
		BeanUtils.copyProperties(query, queryResult);
		
		op.setRoleId(roleId);
		op.setUserId(userId);
		op.setUserName(userName);
		op.setWorkNo(workNo);
		
		List<Map<String, Object>> list = operationLogServiceImpl.search(query, op, startTime, endTime);
		int count = operationLogServiceImpl.searchCount(op, startTime, endTime);
        queryResult.setRecordCount(count);
        
		data.put("list", list);
		data.put("total", queryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
}

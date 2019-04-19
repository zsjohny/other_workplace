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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.meta.UserQuestion;
import com.jiuy.core.service.UserQuestionService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/userquestion")
@Login
public class UserQuestionController {
	
	@Autowired
	private UserQuestionService userQuestionService;
	
	@RequestMapping(value = "/search" , method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse search(@RequestParam(value = "question_content", required = false, defaultValue="")String content,
			@RequestParam(value = "yjj_number", required = false, defaultValue = "-1")long yJJNumber,
			@RequestParam(value = "start_time", required =false, defaultValue = "1970-1-1 00:00:00")String startTime,
			@RequestParam(value = "end_time", required = false, defaultValue = "")String endTime,
			@RequestParam(value = "page", required = false, defaultValue = "1")int page,
			@RequestParam(value = "page_size", required = false, defaultValue = "10")int pageSize){
		
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		PageQuery query = new PageQuery(page, pageSize);
		PageQueryResult queryResult = new PageQueryResult();
		BeanUtils.copyProperties(query, queryResult);
		
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
    	
    	List<UserQuestion> userQuestions = userQuestionService.search(content, yJJNumber, startTimeL, endTimeL,queryResult);
    	int count = userQuestionService.searchCount(content, yJJNumber, startTimeL, endTimeL);
    	
    	queryResult.setRecordCount(count);
    	
    	data.put("userQuestions", userQuestions);
		data.put("total", queryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
}

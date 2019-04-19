package com.jiuy.web.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.meta.notification.Notification;
import com.jiuy.core.service.notifacation.NotifacationService;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@RequestMapping("/notification")
@Controller
@Login
public class NotifacationController {
	
	private final int PAGE_SIZE = 4;
	
	@Resource
	private NotifacationService notificationServiceImpl;

    @AdminOperationLog
	@RequestMapping("/index")
	public String notificationPage() {
		return "page/backend/notification";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse searchNotification(@RequestParam(value = "title", defaultValue = "", required = false)String title, 
			@RequestParam(value = "page", defaultValue = "1", required = false)int page) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		PageQuery pageQuery = new PageQuery(page, PAGE_SIZE);
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);
		
		List<Notification> notificationList = notificationServiceImpl.searchNotification(title, pageQuery);
		int count = notificationServiceImpl.searchNotificationCount(title);
		pageQueryResult.setRecordCount(count);
		
		data.put("notification", notificationList);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    @AdminOperationLog
	@ResponseBody
	public JsonResponse addNotification(@RequestBody Notification notification) throws ParseException {
		JsonResponse jsonResponse = new JsonResponse();
		

//		ResultCode resultCode = notificationServiceImpl.addNotificationObj(notification);

		ResultCode resultCode = notificationServiceImpl.addNotificationBasicInfo(notification);

		
		return jsonResponse.setResultCode(resultCode);
	}
	
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
    @AdminOperationLog
	@ResponseBody
	public JsonResponse rmNotification(@RequestParam(value = "id")long id) {
		JsonResponse jsonResponse = new JsonResponse();
		
		ResultCode resultCode = notificationServiceImpl.rmNotification(id);
		
		return jsonResponse.setResultCode(resultCode);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @AdminOperationLog
	@ResponseBody
	public JsonResponse updateNotification(@RequestBody Notification notification) {
		JsonResponse jsonResponse = new JsonResponse();
	
		ResultCode resultCode = notificationServiceImpl.updateNotification(notification);
		
		return jsonResponse.setResultCode(resultCode);
	}
}

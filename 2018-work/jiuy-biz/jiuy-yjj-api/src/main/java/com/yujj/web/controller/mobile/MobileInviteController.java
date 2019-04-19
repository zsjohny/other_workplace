package com.yujj.web.controller.mobile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.InviteFacade;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.InvitedUserActionLogService;
import com.yujj.business.service.UserInviteRewardLogService;
import com.yujj.business.service.UserService;
import com.yujj.entity.account.User;
import com.yujj.entity.account.UserDetail;
import com.yujj.exception.ParameterErrorException;

@Controller
@RequestMapping("/mobile/invite")
@Login
public class MobileInviteController {
	
	@Autowired
	private InviteFacade inviteFacade;
	
	@Autowired
	private UserInviteRewardLogService userInviteRewardService;
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	@Autowired
	private InvitedUserActionLogService invitedUserActionLogService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/index")
	@ResponseBody
	public JsonResponse index(@RequestParam(value = "page", defaultValue = "1") int page, 
			@RequestParam(value = "page_size", defaultValue = "3") int pageSize,
			UserDetail userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
    	if (userDetail == null) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setData("未登录");
		}
    	long userId = userDetail.getUserId();
    	Map<String, Object> map = new HashMap<String, Object>();
    	try {
    		 map = inviteFacade.remainInvite(userId);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
    	
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("invite_info", map);
    	
    	int recordCount = userInviteRewardService.searchCount(userId);
    	PageQuery pageQuery = new PageQuery(page, pageSize);
    	PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, recordCount);
    	List<String> rewards = inviteFacade.search(pageQuery, userId);
    	data.put("rewards", rewards);
    	data.put("pageQuery", pageQueryResult);
    	
    	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
	}
	
	@RequestMapping("/shareContent")
	@ResponseBody
	public JsonResponse index(UserDetail userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		if (userDetail == null) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setData("未登录");
		}
		Map<String, Object> data = new HashMap<String, Object>();
		JSONObject inviteSetting = globalSettingService.getJsonObject(GlobalSettingName.INVITE_GIFT_SETTING);
		String shareTitle = inviteSetting.getString("shareTitle");
		String shareDesc = inviteSetting.getString("shareDesc");
		String shareImage = inviteSetting.getString("shareImage");
		data.put("shareTitle", shareTitle);
		data.put("shareDesc", shareDesc);
		data.put("shareImage", shareImage);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
	}
	
	@RequestMapping("/rules")
	@ResponseBody
	public JsonResponse rules(UserDetail userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
    	if (userDetail == null) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setData("未登录");
		}
    	Map<String, Object> data = new HashMap<String, Object>();
    	StringBuilder builder = inviteFacade.loadInviteRules();
    	data.put("rulers", builder);
    	
    	User user = userService.getUser(userDetail.getUserId());
    	data.put("yjj_number", user.getyJJNumber());
    	
    	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
	}
	
	@RequestMapping("/logs")
	@ResponseBody
	public JsonResponse logs(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "page_size", defaultValue = "5") int pageSize,
			UserDetail userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
    	if (userDetail == null) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setData("未登录");
		}
    	Map<String, Object> data = new HashMap<String, Object>();
    	PageQuery pageQuery = new PageQuery(page, pageSize);
    	
    	int recordCount = invitedUserActionLogService.searchCount(userDetail.getUserId());
    	PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, recordCount);
    	List<Map<String, Object>> list = inviteFacade.loadLogs(pageQuery, userDetail.getUserId());
    	data.put("logs", list);
    	data.put("total", pageQueryResult);
    	
    	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
	}
	
	@RequestMapping("/logs/rewards")
	@ResponseBody
	public JsonResponse logsRewards(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "page_size", defaultValue = "5") int pageSize,
			UserDetail userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
    	if (userDetail == null) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setData("未登录");
		}
    	Map<String, Object> data = new HashMap<String, Object>();
    	PageQuery pageQuery = new PageQuery(page, pageSize);
    	
    	int recordCount = userInviteRewardService.searchCount(userDetail.getUserId());
    	PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, recordCount);
    	List<Map<String, Object>> list = inviteFacade.loadRewards(pageQuery, userDetail.getUserId());
    	data.put("logs", list);
    	data.put("total", pageQueryResult);
    	
    	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
	}
	
	/**
	 * 
	 * @param type 0:分享到朋友圈，1：分享给朋友
	 * @return
	 */
	@RequestMapping("/share/statistics")
	@ResponseBody
	public JsonResponse shareStatistics(@RequestParam("type") int type, UserDetail userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		
		inviteFacade.shareStatistics(type, userDetail);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
}

package com.yujj.web.controller.mobile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.UserFetchGiftStatus;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.UserGiftFacade;
import com.yujj.business.service.UserGiftLogService;
import com.yujj.entity.account.UserDetail;
import com.yujj.exception.ParameterErrorException;

@Controller
@RequestMapping("/mobile/gift")
@Login
public class MobileUserGiftController {
	

    private static final Logger logger = LoggerFactory.getLogger("MobileUserGiftController");
	
	@Autowired
	private UserGiftLogService userGiftLogService;
	
	@Autowired
	private UserGiftFacade userGiftFacade;
	
    @RequestMapping(value = "/month")
    @ResponseBody
    public JsonResponse userGiftInfo(UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	if (userDetail == null) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setData("未登录");
		}
    	
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("title", "暂无可领取礼包,敬请期待");
    	
    	UserFetchGiftStatus status = userGiftLogService.getFetchStatus(userDetail.getUserId());
    	if (status != UserFetchGiftStatus.NO_GIFT) {
			data.put("title", userGiftFacade.getGiftTitle());
		}
    	if (status == UserFetchGiftStatus.UNFETCHED) {
    		List<Map<String, Object>> list = userGiftFacade.getGiftInfo();
			data.put("fetch", "YES");
			data.put("description", "");
			data.put("content", list);
		} else if (status == UserFetchGiftStatus.FETCHED) {
			data.put("fetch", "NO");
			data.put("description", "已领取奖品，欢迎下次参与");
		} else if(status == UserFetchGiftStatus.NO_GIFT) {
			data.put("fetch", "NO");
			data.put("description", "暂无可领取奖品，敬请期待");
		}
    	
    	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
    }
    
    @RequestMapping(value = "/fetch")
    @ResponseBody
    public JsonResponse fetchGift(UserDetail userDetail, ClientPlatform clientPlatform) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	if (userDetail == null) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setData("未登录");
		}
    	UserFetchGiftStatus status = userGiftLogService.getFetchStatus(userDetail.getUserId());
		if (status != UserFetchGiftStatus.UNFETCHED) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("暂无可领取奖品(已领取或无活动)!");
		}
		
		Map<String, Object> data = new HashMap<>();
		try {
			data = userGiftFacade.fetch(userDetail.getUserId(), clientPlatform);
		} catch (ParameterErrorException e) {
			data.put("fetch_count", 0);
			
			logger.error(e.getMessage());
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setData(data).setError("领取失败,请联系客服！");
		}
    	
    	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
    }
}

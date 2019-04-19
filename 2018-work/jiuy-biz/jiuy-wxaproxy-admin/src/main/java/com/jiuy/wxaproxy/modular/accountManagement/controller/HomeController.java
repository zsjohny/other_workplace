package com.jiuy.wxaproxy.modular.accountManagement.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.core.base.controller.BaseController;
import com.jiuy.wxaproxy.core.shiro.ShiroKit;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.service.common.IProxyUserService;
import com.jiuyuan.util.TipsMessageException;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Controller
@RequestMapping("/home")
public class HomeController extends BaseController {

	private static final Log logger = LogFactory.get("HomeController");

	@Autowired
	private IProxyUserService proxyUserService;


	
	@ResponseBody
	@RequestMapping("")
	public JsonResponse home() {
		JsonResponse jsonResponse = new JsonResponse();
		long proxyUserId = ShiroKit.getUser().getId();
		try {
			Map<String,Object> data = proxyUserService.home(proxyUserId);
			return jsonResponse.setSuccessful().setData(data);
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
		
	}

    
}

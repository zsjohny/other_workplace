package com.jiuy.operator.modular.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.operator.core.shiro.ShiroKit;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.service.common.IOperatorArticleService;
import com.jiuyuan.util.TipsMessageException;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/home")
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
    @Autowired
    private IOperatorArticleService operatorArticleService;
	
	@RequestMapping("")
	@ResponseBody
	public Object homePage(){
		JsonResponse jsonResponse = new JsonResponse();
		long operatorUserId = ShiroKit.getUser().getId();
		try {
			int count = operatorArticleService.homePage(operatorUserId);
			return jsonResponse.setSuccessful().setData(count);
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
	}

}

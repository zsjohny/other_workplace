package com.jiuy.store.api.tool.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.MarketingSMSService;
import com.jiuyuan.service.common.YunXinSmsService;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/dongzhong")
public class MarketingSMSController {
	@Autowired
	YunXinSmsService smsService;

	@Autowired
	MarketingSMSService testService;

	@RequestMapping("/sendphonestore")
	public JsonResponse testSendInfostore() {

		JsonResponse jsonResponse = new JsonResponse();

		// 获取需要发送的所有门店用户
		List<StoreBusiness> users = testService.getTestStores();

		// 获取需要发送的所有其他用户
		List<Map<String, String>> otherUsers = testService.getOtherStores();

		// 发送短信

		JSONArray params = new JSONArray();
		// params.add("112233");//初始密码
		// params.add("4001180099");//400电话

		boolean success = true;

		// success = smsService.send("15824400571", params, 3018002);
		int i = 0, j = 0, k = 0;
		for (StoreBusiness user : users) {
			i++;
			// success = smsService.send(user.getUserName(), params, 3018002);
			// if (StringUtils.isNotBlank(user.getPhoneNumber()))
			// success = smsService.send(user.getPhoneNumber(), params, 8224);

			if (!success) {
				j++;
			} else {
				k++;
			}
		}

		return jsonResponse.setSuccessful().setCode(0);
	}
}

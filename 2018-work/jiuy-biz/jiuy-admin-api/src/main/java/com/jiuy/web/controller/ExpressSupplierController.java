package com.jiuy.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.service.ExpressSupplierService;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/express")
public class ExpressSupplierController {
	
	@Autowired
	private ExpressSupplierService expressSupplierService;
	
	@RequestMapping("/supplier")
	@ResponseBody
	public JsonResponse loadSupplier() {
		JsonResponse jsonResponse = new JsonResponse();
		 
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("suppliers", expressSupplierService.search());
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
	}
}

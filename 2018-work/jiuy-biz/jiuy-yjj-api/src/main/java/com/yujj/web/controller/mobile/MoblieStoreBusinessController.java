package com.yujj.web.controller.mobile;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.util.QRCodeUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.dao.mapper.StoreBusinessMapper;

/**
 * @author jeff.zhan
 * @version 2016年10月27日 下午1:49:09
 * 
 */
@Controller
@RequestMapping("/mobile/storebusiness")
public class MoblieStoreBusinessController {
	
	@Autowired
	private StoreBusinessMapper storeBusinessMapper;
	
	@RequestMapping("/list")
	@ResponseBody
	public JsonResponse list() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();
		
		data.put("list", storeBusinessMapper.getAll());
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping("/{id}")
	@ResponseBody
	public JsonResponse getById(@PathVariable("id") Long id) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();
		data.put("store_business", storeBusinessMapper.getById(id));
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
	}
	
	@RequestMapping("/qrcode/src")
	@ResponseBody
	public void test9(HttpServletResponse response, HttpServletRequest request, 
			@RequestParam("id") Long id, @RequestParam("file_name") String fileName, 
			@RequestParam("width") int width, @RequestParam("height") int height) {
		QRCodeUtil.getFile(request, response, Constants.SERVER_URL + "/static/app/seller_register.html?/id=" + id + "&type=2", fileName, width, height);
	}
}

package com.jiuy.web.controller;

import java.util.List;

import com.util.ServerPathUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.TestProduct;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@Login
@RequestMapping("/testajax")
public class TestAjaxController {

//    @RequestMapping(value = "/updateproduct", method = {RequestMethod.POST })
    @RequestMapping("/updateproduct")
	@ResponseBody
	public JsonResponse updateProduct(@RequestBody List<Product> products) {
		JsonResponse jsonResponse = new JsonResponse();
		
		for (Product product : products) {
			System.out.println("product:" + product.getId() + ", " + product.getName());
		}
		
		return jsonResponse.setSuccessful();
	}
    
    @RequestMapping(value = "/search", method = {RequestMethod.GET })
	@ResponseBody
	public JsonResponse updateProduct1() {
//	public JsonResponse updateProduct1(@RequestBody TestProduct product) {
		JsonResponse jsonResponse = new JsonResponse();
		TestProduct product = new TestProduct();
		System.out.println("product1:" + product.getId() + ", " + product.getName());
		return jsonResponse.setSuccessful();
	}


	@RequestMapping( value = "testPath" )
	@ResponseBody
	public JsonResponse testPath() {
		return JsonResponse.successful (ServerPathUtil.me ().getServer2Distribution () + "/cashOutIn/dstbSuccess");
	}
}

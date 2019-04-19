package com.yujj.web.controller.mobile;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.web.help.JsonResponse;
import com.yujj.entity.account.UserDetail;

@Controller
//@Login
@RequestMapping("/mobile/h5")
public class MobileH5Controller {

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getAllNotications(UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();

    	Map<String, Object> data = new HashMap<String, Object>();
    	long i = System.currentTimeMillis();
    	    	
    	data.put("data", 123);

    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    @ResponseBody
  //  @Login 
    public JsonResponse confirmNotification(Long coins, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long i = System.currentTimeMillis();
    	return jsonResponse.setSuccessful();
    }
}
